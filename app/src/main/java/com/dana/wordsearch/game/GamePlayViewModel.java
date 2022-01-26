package com.dana.wordsearch.game;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dana.wordsearch.GameApplication;
import com.dana.wordsearch.Preferences;
import com.dana.wordsearch.commons.SingleLiveEvent;
import com.dana.wordsearch.commons.Timer;
import com.dana.wordsearch.commons.Util;
import com.dana.wordsearch.data.entity.GameDataMapper;
import com.dana.wordsearch.data.sqlite.DbHelper;
import com.dana.wordsearch.data.sqlite.GameDataSQLiteDataSource;
import com.dana.wordsearch.model.GameData;
import com.dana.wordsearch.model.UsedWord;
import com.dana.wordsearch.model.Word;
import com.dana.wordsearch.xml.WordXmlDataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class GamePlayViewModel extends ViewModel {

    private static final int TIMER_TIMEOUT = 1000;

    public static abstract class GameState {}
    static class Generating extends GameState {
        int rowCount;
        int colCount;
        String name;
        private Generating(int rowCount, int colCount, String name) {
            this.rowCount = rowCount;
            this.colCount = colCount;
            this.name = name;
        }
    }
    static class Loading extends GameState {
        int gid;
        private Loading(int gid) {
            this.gid = gid;
        }
    }
    static class Finished extends GameState {
        GameData mGameData;
        private Finished(GameData gameData) {
            this.mGameData = gameData;
        }
    }
    static class Paused extends GameState {
        private Paused() {}
    }
    static class Playing extends GameState {
        GameData mGameData;
        private Playing(GameData gameData) {
            this.mGameData = gameData;
        }
    }

    static class AnswerResult {
        public boolean correct;
        public int usedWordId;
        AnswerResult(boolean correct, int usedWordId) {
            this.correct = correct;
            this.usedWordId = usedWordId;
        }
    }

    private final GameDataSQLiteDataSource mGameDataSource;
    private final WordXmlDataSource mwordXmlDataSource;
    private final GameDataCreator mGameDataCreator;
    private GameData mCurrentGameData;
    private final Timer mTimer;
    private int mCurrentDuration;

    private GameState mCurrentState = null;
    private MutableLiveData<UsedWord> mqlue;
    private MutableLiveData<Integer> mOnTimer;
    private MutableLiveData<Integer> mAnsewrWordCount;
    private MutableLiveData<GameState> mOnGameState;
    private SingleLiveEvent<AnswerResult> mOnAnswerResult;

    public GamePlayViewModel() {

        mwordXmlDataSource=new WordXmlDataSource(GameApplication.getContext());
        mGameDataSource=new GameDataSQLiteDataSource(new DbHelper(GameApplication.getContext()));

        mGameDataCreator = new GameDataCreator();

        mTimer = new Timer(TIMER_TIMEOUT);
        mTimer.addOnTimeoutListener(elapsedTime -> {
            mOnTimer.setValue(mCurrentDuration++);
            mGameDataSource.saveGameDataDuration(mCurrentGameData.getId(), mCurrentDuration);
        });
        resetLiveData();
    }

    private void resetLiveData() {
        mqlue=new MutableLiveData<>();
        mOnTimer = new MutableLiveData<>();
        mAnsewrWordCount = new MutableLiveData<>();
        mOnGameState = new MutableLiveData<>();
        mOnAnswerResult = new SingleLiveEvent<>();
    }

    public void stopGame() {
        mCurrentGameData = null;
        mTimer.stop();
        resetLiveData();
    }

    public void pauseGame() {
        mTimer.stop();
        setGameState(new Paused());
    }

    public void resumeGame() {
        if (mCurrentState instanceof Paused) {
            mTimer.start();
            setGameState(new Playing(mCurrentGameData));
        }
    }

    public void loadGameRound(int gid) {
        if (!(mCurrentState instanceof Generating)) {
            setGameState(new Loading(gid));

            mGameDataSource.getGameData(gid, gameRound -> {
                mCurrentGameData = new GameDataMapper().map(gameRound);
                mCurrentDuration = mCurrentGameData.getDuration();
                if (!mCurrentGameData.isFinished())
                    mTimer.start();
                setGameState(new Playing(mCurrentGameData));
            });
        }
    }

    @SuppressLint("CheckResult")
    public void generateNewGameRound(Context context,int gametype) {
        if (!(mCurrentState instanceof Generating)) {

            int rowCount= Util.getgameBoardSize(gametype);
            int colcount= rowCount;

            setGameState(new Generating(rowCount, colcount, "game"));

            Observable.create((ObservableOnSubscribe<GameData>) emitter -> {
                List<Word> wordList = mwordXmlDataSource.getWords();
                GameData gr = mGameDataCreator.newGameData(wordList, rowCount, colcount, "game");
                long gid = mGameDataSource.saveGameData(new GameDataMapper().revMap(gr));
                Preferences.getInstance(context).setBooleanPref(String.valueOf(gametype),true);
                Preferences.getInstance(context).setIntPref(String.valueOf(gametype),(int)gid);
                gr.setId((int) gid);
                emitter.onNext(gr);
                emitter.onComplete();
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(gameRound -> {
                        mCurrentDuration = 0;
                        mTimer.start();
                        mCurrentGameData = gameRound;
                        setGameState(new Playing(mCurrentGameData));
                    });
        }
    }

    public void answerWord(String answerStr, UsedWord.AnswerLine answerLine, boolean reverseMatching) {
        UsedWord correctWord = mCurrentGameData.markWordAsAnswered(answerStr, answerLine, reverseMatching);

        boolean correct = correctWord != null;
        mOnAnswerResult.setValue(new AnswerResult(correct, correctWord != null ? correctWord.getId() : -1));
        if (correct) {
            mGameDataSource.markWordAsAnswered(correctWord);
            mAnsewrWordCount.setValue(mCurrentGameData.getAnsweredWordsCount());
            if (mCurrentGameData.isFinished()) {
                setGameState(new Finished(mCurrentGameData));
            }
        }
    }

    public LiveData<Integer> getOnTimer() {
        return mOnTimer;
    }


    public LiveData<Integer> getOnAnserdWordCount() {
        return mAnsewrWordCount;
    }

    public LiveData<GameState> getOnGameState() {
        return mOnGameState;
    }

    public LiveData<AnswerResult> getOnAnswerResult() {
        return mOnAnswerResult;
    }

    private void setGameState(GameState state) {
        mCurrentState = state;
        mOnGameState.setValue(mCurrentState);
    }


    public void getQule(){

        for (UsedWord word:mCurrentGameData.getUsedWords())
        {
            if (!word.isAnswered())
            {
                mqlue.setValue(word);
                break;
            }
        }
    }

    public LiveData<UsedWord> getOnQlueListner()
    {
        return mqlue;
    }

}
