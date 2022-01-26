package com.dana.wordsearch.ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dana.wordsearch.Constants;
import com.dana.wordsearch.Preferences;
import com.dana.wordsearch.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class Ads {

    RewardedAd mRewardedAd;
    private final String TAG=getClass().toString();

    public void loadRewardAd(Activity activity)
    {
        AdRequest adRequest = new AdRequest.Builder().build();


        RewardedAd.load(activity, activity.getString(R.string.ad_rward_id), adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);

                mRewardedAd = rewardedAd;
                Log.d(TAG, "Ad was loaded.");

                if (mRewardedAd != null) {

                    mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();

                            int currentTotal= Preferences.getInstance(activity).getIntPref(Preferences.KEY_USER_COINS,0);
                            int total=currentTotal+rewardAmount;

                            Preferences.getInstance(activity).setIntPref(Preferences.KEY_USER_COINS,total);

                        }
                    });
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d(TAG, loadAdError.getMessage());
                mRewardedAd = null;

            }
        });


    }


}
