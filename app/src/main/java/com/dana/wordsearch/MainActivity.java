package com.dana.wordsearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dana.wordsearch.game.GameActivity;


public class MainActivity extends AppCompatActivity {


    com.dana.wordsearch.databinding.ActivityMainBinding binding;

Preferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        preferences=new Preferences(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        binding.lyCoin.tvCoins.setText(String.valueOf(preferences.getIntPref(Preferences.KEY_USER_COINS,100)));
    }

    public void startGame(View view)
    {

        int count=4;

        if(view.getId()==R.id.tv_easy)
        {
            count=6;
        }else if(view.getId()==R.id.tv_medium)
    {
        count=8;
    }else if(view.getId()==R.id.tv_hard)
        {
            count=20;
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_ROW_COUNT, count);
        intent.putExtra(GameActivity.EXTRA_COL_COUNT, count-1);
        //  intent.putExtra(GameActivity.EXTRA_GAME_ROUND_ID, 1);
        startActivity(intent);
    }

}