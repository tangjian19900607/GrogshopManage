package com.grogshop.manage.common;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by tangjian on 16/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public class AppApplication extends Application {
    private static AppApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"57afc3971021813cc69bbe440f6f50f1");
    }

    public static AppApplication getInstance(){
        if (mInstance == null)
        {
            synchronized (AppApplication.class)
            {
                if (mInstance == null)
                {
                    mInstance = new AppApplication();
                }
            }
        }
        return mInstance;
    }
}
