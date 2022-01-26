package com.dana.wordsearch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dana.wordsearch.ads.Ads;
import com.dana.wordsearch.chooseImage.ChooseAvatharActivity;
import com.dana.wordsearch.game.GameActivity;


public class MainActivity extends AppCompatActivity {


    com.dana.wordsearch.databinding.ActivityMainBinding binding;

Preferences preferences;
    Ads ads=new Ads();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        preferences=Preferences.getInstance(this);


        binding.lyCoin.viewWatchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAd();
            }
        });

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onResume() {
        super.onResume();
        binding.lyCoin.tvCoins.setText(String.valueOf(preferences.getIntPref(Preferences.KEY_USER_COINS,100)));
        binding.ivAvathar.setImageResource(preferences.getIntPref(Constants.AVATHAR_ID,R.drawable.men));
    }

    int gameType=0;
    public void startGame(View view)
    {
        setgameType(view);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(Constants.EXTRA_GAME_TYPE, gameType);
        setGamePreviousIdToIntent(intent);
        startActivity(intent);

    }

    private void setGamePreviousIdToIntent(Intent intent) {
        Log.e("gameType",gameType+"");
        boolean status=preferences.getBooleanPref(String.valueOf(gameType));
        Log.e("status",status+"");
        if (status)
        {
            int gid=preferences.getIntPref(String.valueOf(gameType),0);
            Log.e("gid",gid+"");
            intent.putExtra(Constants.EXTRA_GAME_ROUND_ID, gid);
        }

    }

    private void setgameType(View view) {
        if(view.getId()==R.id.tv_easy)
        {
            gameType=Constants.IGAMETYPE.EASY;
        }
        else if(view.getId()==R.id.tv_medium)
        {
            gameType=Constants.IGAMETYPE.MEDIUM;
        }else if(view.getId()==R.id.tv_hard)
        {
            gameType=Constants.IGAMETYPE.HARD;
        }
    }

    public void chooseAvathar(View view) {

        startActivity(new Intent(this, ChooseAvatharActivity.class));
    }


    public void loadAd()
    {


        ads.loadRewardAd(this);
    }
}