package com.dana.wordsearch;

import static com.dana.wordsearch.Constants.EXTRA_EARNED_COINS;
import static com.dana.wordsearch.Constants.EXTRA_GAME_ROUND_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.dana.wordsearch.databinding.ActivityGameCompletedBinding;

public class GameCompletedActivity extends AppCompatActivity {

    int earnedCoins;

    ActivityGameCompletedBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding= DataBindingUtil.setContentView(this,R.layout.activity_game_completed);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
                earnedCoins = extras.getInt(EXTRA_EARNED_COINS);

        }


   /*     ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(1000);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        binding.ivAvathar.startAnimation(fade_in);*/

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(6000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        binding.ivCircle.startAnimation(rotateAnimation);

        binding.lyCoin.tvCoins.setText(String.valueOf(Preferences.getInstance(this)
                .getIntPref(Preferences.KEY_USER_COINS,100)));
        binding.ivAvathar.setImageResource(Preferences.getInstance(this).getIntPref(Constants.AVATHAR_ID,R.drawable.men));
        binding.tvInfo.setText( getString(R.string.completed_info, earnedCoins));

    }
}