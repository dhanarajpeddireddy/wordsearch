package com.dana.wordsearch.game;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dana.wordsearch.Constant;
import com.dana.wordsearch.Preferences;
import com.dana.wordsearch.R;
import com.dana.wordsearch.SoundPlayer;
import com.dana.wordsearch.commons.DurationFormatter;
import com.dana.wordsearch.commons.Util;
import com.dana.wordsearch.custom.LetterBoard;
import com.dana.wordsearch.custom.StreakView;
import com.dana.wordsearch.databinding.ActivityGameBinding;
import com.dana.wordsearch.model.GameData;
import com.dana.wordsearch.model.UsedWord;

import java.util.List;

public class GameActivity extends AppCompatActivity {
    private ArrayLetterGridDataAdapter mLetterAdapter;
    ActivityGameBinding binding;
    Preferences preferences;
    SoundPlayer mSoundPlayer;

    public static final String EXTRA_GAME_ROUND_ID =
            "GameActivity.ID";
    public static final String EXTRA_ROW_COUNT =
            "GameActivity.ROW";
    public static final String EXTRA_COL_COUNT =
            "GameActivity.COL";

    private GamePlayViewModel mViewModel;
    private static final StreakLineMapper STREAK_LINE_MAPPER = new StreakLineMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this, R.layout.activity_game);

       initViews();

       setObservers();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(EXTRA_GAME_ROUND_ID)) {
                int gid = extras.getInt(EXTRA_GAME_ROUND_ID);
                mViewModel.loadGameRound(gid);
            } else {
                int rowCount = extras.getInt(EXTRA_ROW_COUNT,8);
                int colCount = extras.getInt(EXTRA_COL_COUNT,8);
                mViewModel.generateNewGameRound(rowCount, colCount);
            }
        }


    }



    private void initViews() {

        mViewModel = new ViewModelProvider(this).get(GamePlayViewModel.class);

        preferences=new Preferences(this);

        mSoundPlayer=new SoundPlayer(this,preferences);

        binding.letterBoard.getStreakView().setEnableOverrideStreakLineColor(preferences.grayscale());
        binding.letterBoard.getStreakView().setOverrideStreakLineColor(Color.GRAY);

    }


    private void setObservers() {

        mViewModel.getOnQlueListner().observe(this, this::showQlue);
        mViewModel.getOnTimer().observe(this, this::showDuration);
        mViewModel.getOnAnserdWordCount().observe(this, this::showAnsweredWordsCount);
        mViewModel.getOnGameState().observe(this, this::onGameStateChanged);
        mViewModel.getOnAnswerResult().observe(this, this::onAnswerResult);


        binding.letterBoard.setOnLetterSelectionListener(new LetterBoard.OnLetterSelectionListener() {
            @Override
            public void onSelectionBegin(StreakView.StreakLine streakLine, String str) {
                streakLine.setColor(Util.getRandomColorWithAlpha(170));
                binding.textSelLayout.setVisibility(View.VISIBLE);
                binding.textSelection.setText(str);
            }

            @Override
            public void onSelectionDrag(StreakView.StreakLine streakLine, String str) {
                if (str.isEmpty()) {
                    binding.textSelection.setText("...");
                } else {
                    binding.textSelection.setText(str);
                }
            }

            @Override
            public void onSelectionEnd(StreakView.StreakLine streakLine, String str) {
                mViewModel.answerWord(str, STREAK_LINE_MAPPER.revMap(streakLine), preferences.reverseMatching());
                binding.textSelLayout.setVisibility(View.GONE);
                binding.textSelection.setText(str);
            }
        });

    }

    public void getOneQlue(View view)
    {
        int coins=preferences.getIntPref(Preferences.KEY_USER_COINS,100);
        if (coins>= Constant.QLUECOST)
        {
            mViewModel.getQule();
            preferences.setIntPref(Preferences.KEY_USER_COINS,coins-Constant.QLUECOST);
        }
        else
        {
           // TODO  show watch video popup
        }
    }

    private void showQlue(UsedWord usedWord) {

            UsedWord.AnswerLine answerLine=new UsedWord.AnswerLine();

            answerLine.startCol=usedWord.getPath().getPathItems().get(0).getColumn();
            answerLine.startRow=usedWord.getPath().getPathItems().get(0).getRow();

            answerLine.endCol=usedWord.getPath().getPathItems().get(0).getColumn();
            answerLine.endRow=usedWord.getPath().getPathItems().get(0).getRow();

            answerLine.color=Color.RED;

            binding.letterBoard.addStreakLine(STREAK_LINE_MAPPER.map(answerLine));

           TextView textView = findUsedWordTextViewByUsedWordId(usedWord.getId());

        if (textView != null) {
            textView.setBackgroundColor(Color.RED);
            textView.animate()
                   .scaleX(1.2f)
                   .scaleY(1.2f)
                   .setDuration(500)
                   .setInterpolator(new DecelerateInterpolator())
                   .start();
        }

    }


    private void onGameStateChanged(GamePlayViewModel.GameState gameState) {
        showLoading(false, null);
        if (gameState instanceof GamePlayViewModel.Generating) {
            GamePlayViewModel.Generating state = (GamePlayViewModel.Generating) gameState;
            String text = "Generating " + state.rowCount + "x" + state.colCount + " grid";
           showLoading(true, text);
        } else if (gameState instanceof GamePlayViewModel.Finished) {
            showFinishGame(((GamePlayViewModel.Finished) gameState).mGameData.getId());
        } else if (gameState instanceof GamePlayViewModel.Paused) {

        } else if (gameState instanceof GamePlayViewModel.Playing) {
            onGameRoundLoaded(((GamePlayViewModel.Playing) gameState).mGameData);
        }
    }


    private void showFinishGame(int gameId) {
        int coins=preferences.getIntPref(Preferences.KEY_USER_COINS,0);
        coins+=usedWordCount*5;
        preferences.setIntPref(Preferences.KEY_USER_COINS,coins);
        Toast.makeText(getApplicationContext(),"finished",Toast.LENGTH_LONG).show();
    }

    private void showLoading(boolean enable, String text) {
        if (enable) {
          //  binding.loading.setVisibility(View.VISIBLE);
           // binding.loadingText.setVisibility(View.VISIBLE);
            binding.letterBoard.setVisibility(View.GONE);
           // binding.loadingText.setText(text);
        } else {
          //  binding.loading.setVisibility(View.GONE);
           // binding.loadingText.setVisibility(View.GONE);
            binding.letterBoard.setVisibility(View.VISIBLE);
        }
    }

    private void onAnswerResult(GamePlayViewModel.AnswerResult answerResult) {
        if (answerResult.correct) {
            TextView textView = findUsedWordTextViewByUsedWordId(answerResult.usedWordId);

            if (textView != null) {
                UsedWord uw = (UsedWord) textView.getTag();

                if (preferences.grayscale()) {
                    uw.getAnswerLine().color = Color.GRAY;
                }
                textView.setBackgroundColor(uw.getAnswerLine().color);
                textView.setText(uw.getString());
                textView.setTextColor(Color.WHITE);
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

               /* Animator anim = AnimatorInflater.loadAnimator(this, R.animator.zoom_in_out);
                anim.setTarget(textView);
                anim.start();*/

                textView.animate()
                        .scaleX(0.8f)
                        .scaleY(0.8f)
                        .setDuration(400)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }

            mSoundPlayer.play(SoundPlayer.Sound.Correct);
        }
        else {
            binding.letterBoard.popStreakLine();
            mSoundPlayer.play(SoundPlayer.Sound.Wrong);
        }
    }

    private void onGameRoundLoaded(GameData gameData) {
        if (gameData.isFinished()) {
            setGameAsAlreadyFinished();
        }

        showLetterGrid(gameData.getGrid().getArray());
        showDuration(gameData.getDuration());
        showUsedWords(gameData.getUsedWords());
        showWordsCount(gameData.getUsedWords().size());
        showAnsweredWordsCount(gameData.getAnsweredWordsCount());
        doneLoadingContent();
    }

    private void setGameAsAlreadyFinished() {
        binding.letterBoard.getStreakView().setInteractive(false);
        binding.finishedText.setVisibility(View.VISIBLE);
    }


    private void showDuration(int duration) {
        binding.textDuration.setText(DurationFormatter.fromInteger(duration));
    }

    int usedWordCount;
    private void showUsedWords(List<UsedWord> usedWords) {
        usedWordCount=usedWords.size();
        for (UsedWord uw : usedWords) {
            binding.flowLayout.addView( createUsedWordTextView(uw) );
        }
    }

    private TextView createUsedWordTextView(UsedWord uw) {
        TextView tv = new TextView(this);
        tv.setPadding(10, 5, 10, 5);
        tv.setTypeface(ResourcesCompat.getFont(this, R.font.uomobold));
        if (uw.isAnswered()) {
            if (preferences.grayscale()) {
                uw.getAnswerLine().color = Color.GRAY;
            }
            tv.setBackgroundColor(uw.getAnswerLine().color);
            tv.setText(uw.getString());
            tv.setTextColor(Color.WHITE);
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            binding.letterBoard.addStreakLine(STREAK_LINE_MAPPER.map(uw.getAnswerLine()));
        }
        else {
            StringBuilder str = new StringBuilder(uw.getString());
            if (uw.isMystery()) {
                int revealCount = uw.getRevealCount();
                String uwString = uw.getString();
                str = new StringBuilder();
                for (int i = 0; i < uwString.length(); i++) {
                    if (revealCount > 0) {
                        str.append(uwString.charAt(i));
                        revealCount--;
                    }
                    else {
                        str.append(" ?");
                    }
                }
            }
            tv.setText(str.toString());


        }

        tv.setTag(uw);
        return tv;
    }

    private void showAnsweredWordsCount(int count) {
        binding.answeredTextCount.setText(String.valueOf(count));
    }

    private void showWordsCount(int count) {
        binding.wordsCount.setText(String.valueOf(count));
    }



    private void doneLoadingContent() {
        // call tryScale() on the next render frame
        new Handler().postDelayed(this::tryScale, 100);
    }


    private void tryScale() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int boardWidth = binding.letterBoard.getWidth();
        int screenWidth = metrics.widthPixels;

        if (preferences.autoScaleGrid() || boardWidth > screenWidth) {
            float scale = (float)screenWidth / (float)boardWidth;
            binding.letterBoard.scale(scale, scale);
//            mLetterBoard.animate()
//                    .scaleX(scale)
//                    .scaleY(scale)
//                    .setDuration(400)
//                    .setInterpolator(new DecelerateInterpolator())
//                    .start();
        }
    }


    private void showLetterGrid(char[][] grid) {
        if (mLetterAdapter == null) {
            mLetterAdapter = new ArrayLetterGridDataAdapter(grid);
            binding.letterBoard.setDataAdapter(mLetterAdapter);
        }
        else {
            mLetterAdapter.setGrid(grid);
        }
    }

    private TextView findUsedWordTextViewByUsedWordId(int usedWordId) {
        for (int i = 0; i < binding.flowLayout.getChildCount(); i++) {
            TextView tv = (TextView) binding.flowLayout.getChildAt(i);
            UsedWord uw = (UsedWord) tv.getTag();
            if (uw != null && uw.getId() == usedWordId) {
                return tv;
            }
        }

        return null;
    }


}