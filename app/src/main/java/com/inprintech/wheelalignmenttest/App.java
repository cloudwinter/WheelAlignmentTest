package com.inprintech.wheelalignmenttest;

import android.app.Application;
import android.content.Context;

import com.inprintech.wheelalignmenttest.core.RunningContext;

public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        RunningContext.init(this);
    }

    public static Context getContext(){
        return sContext;
    }
}
