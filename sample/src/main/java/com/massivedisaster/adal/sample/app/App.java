package com.massivedisaster.adal.sample.app;

import android.app.Application;
import android.util.Log;

import com.massivedisaster.adal.manager.ApplicationStateManager;

public class App extends Application implements ApplicationStateManager.BackAndForegroundListener {

    private static final String TAG = App.class.getSimpleName();
    private ApplicationStateManager applicationStateManager;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationStateManager = new ApplicationStateManager(this);
        registerActivityLifecycleCallbacks(applicationStateManager);
    }

    @Override
    public void onBackground() {
        Log.d(TAG, "onBackground");
    }

    @Override
    public void onForeground() {
        Log.d(TAG, "onForeground");
    }

    public boolean isBackground() {
        return applicationStateManager.isBackground();
    }

    public boolean isForeground() {
        return applicationStateManager.isForeground();
    }
}
