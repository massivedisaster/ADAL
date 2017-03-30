package com.massivedisaster.adal.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

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

    /**
     * Creates a ApplicationStateManager instance
     */
    public ApplicationStateManager() {
        init();
    }

    /**
     * Creates a ApplicationStateManager instance
     *
     * @param listener the listener for application state changes.
     */
    public ApplicationStateManager(BackAndForegroundListener listener) {
        init();
        mBackAndForegroundListener = listener;
    }

    private void init() {
        mMainThreadHandler = new Handler();
        mBackgroundChecker = new BackAndForegroundChecker();
    }

    /**
     * Tells if application is in foreground.
     *
     * @return true is application is in foreground.
     */
    public boolean isForeground() {
        return mIsForeground;
    }

    /**
     * Tells if application is in background.
     *
     * @return true is application is in background.
     */
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
                mBackAndForegroundListener.onForeground();
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
        /**
         * Application went from foreground.
         */
        void onBackground();

        /**
         * Application went from background.
         */
        void onForeground();
    }

    private class BackAndForegroundChecker implements Runnable {
        @Override
        public void run() {
            if (mIsForeground && mIsPaused) {

                mIsForeground = false;

                if (mBackAndForegroundListener != null) {
                    mBackAndForegroundListener.onBackground();
                }
            }
        }
    }
}
