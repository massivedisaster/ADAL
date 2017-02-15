package com.massivedisaster.adal.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by jzeferino on 2/15/17.
 */

public class ApplicationStateManager implements Application.ActivityLifecycleCallbacks {

    /**
     * How long to wait before checking onResume()/onPause() count to determine if the app has been
     * backgrounded.
     */
    private static final long sBackgroundCheckDelay = 500;

    private Handler mMainThreadHandler;
    private BackAndForegroundChecker mBackgroundChecker;
    private BackAndForegroundListener mBackAndForegroundListener;
    private boolean mIsForeground = true;
    private boolean mIsPaused;

    public ApplicationStateManager() {
        init();
    }

    public ApplicationStateManager(BackAndForegroundListener listener) {
        init();
        mBackAndForegroundListener = listener;
    }

    private void init() {
        mMainThreadHandler = new Handler();
        mBackgroundChecker = new BackAndForegroundChecker();
    }

    public boolean isForeground() {
        return mIsForeground;
    }

    public boolean isBackground() {
        return !mIsForeground;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mIsPaused = false;

        // remove any scheduled checker since we're resuming activity
        // we're definitely not going background
        mMainThreadHandler.removeCallbacks(mBackgroundChecker);

        if (!activity.isChangingConfigurations()) {
            boolean wasInBackground = !mIsForeground;
            mIsForeground = true;


            if (wasInBackground && mBackAndForegroundListener != null) {
                mBackAndForegroundListener.wentForeground();
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mIsPaused = true;

        mMainThreadHandler.removeCallbacks(mBackgroundChecker);

        // if we're changing configurations we aren't going background so
        // no need to schedule the check
        if (!activity.isChangingConfigurations()) {
            mMainThreadHandler.postDelayed(mBackgroundChecker, sBackgroundCheckDelay);
        }
    }

    public interface BackAndForegroundListener {
        void wentBackground();

        void wentForeground();
    }

    private class BackAndForegroundChecker implements Runnable {
        @Override
        public void run() {
            if (mIsForeground && mIsPaused) {

                mIsForeground = false;

                if (mBackAndForegroundListener != null) {
                    mBackAndForegroundListener.wentBackground();
                }
            }
        }
    }
}
