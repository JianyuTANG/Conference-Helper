package com.example.myapplication;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;

public class MyApplication extends Application {

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;


        //EMClient.getInstance().init(this, null);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        //EMClient.getInstance().setDebugMode(true);
        EaseUI.getInstance().init(this, null);
        Fresco.initialize(this);
    }

    public static MyApplication getInstance(){
        return myApplication;
    }
}
