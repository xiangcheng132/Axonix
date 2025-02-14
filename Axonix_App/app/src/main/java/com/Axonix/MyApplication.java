package com.Axonix;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // ARouter 初始化
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // 打开日志
            ARouter.openDebug();   // 调试模式
        }

        ARouter.init(this);
    }
}