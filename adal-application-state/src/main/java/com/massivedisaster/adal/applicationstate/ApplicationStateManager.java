/*
 * ADAL - A set of Android libraries to help speed up Android development.
 * Copyright (C) 2017 ADAL.
 *
 * ADAL is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or any later version.
 *
 * ADAL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with ADAL. If not, see <http://www.gnu.org/licenses/>.
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
