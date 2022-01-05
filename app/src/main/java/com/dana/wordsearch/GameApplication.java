package com.dana.wordsearch;

import android.app.Application;
import android.content.Context;

public class GameApplication extends Application {

    private static GameApplication instance;

    public static Context getContext() {
        return instance;
    }

    public static Application getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

    }





    }



