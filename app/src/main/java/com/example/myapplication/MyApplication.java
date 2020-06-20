package com.example.myapplication;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplication extends Application {

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        Fresco.initialize(this);
    }

    public static MyApplication getInstance(){
        return myApplication;
    }
}
