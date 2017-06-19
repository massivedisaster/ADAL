/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2017 ADAL
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.massivedisaster.adal.applicationstate;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

/**
 * Manage the background and foreground state of the application.
 */
public class ApplicationStateManager implements Application.ActivityLifecycleCallbacks {

    /**
     * How long to wait before checking onResume()/onPause() count to determine if the app has been
     * backgrounded.
     */
    private static final long BACKGROUND_CHECK_DELAY = 500;
    protected boolean mIsForeground = true;
    protected boolean mIsPaused;
    protected BackAndForegroundListener mBackAndForegroundListener;
    private Handler mMainThreadHandler;
    private BackAndForegroundChecker mBackgroundChecker;

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

    /**
     * Initialize management objects.
     */
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
        // Intended.
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // Intended.
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // Intended.
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // Intended.
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // Intended.
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
            mMainThreadHandler.postDelayed(mBackgroundChecker, BACKGROUND_CHECK_DELAY);
        }


    }

    /**
     * Listener for background and foreground events.
     */
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
