package com.senierr.setask;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author zhouchunjie
 * @date 2017/6/9
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
