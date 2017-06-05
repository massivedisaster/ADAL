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

package com.massivedisaster.adal.fragment;

import android.os.Handler;

/**
 * Abstract class for splash screen.
 */
public abstract class AbstractSplashFragment extends AbstractRequestFragment {

    private static final long SPLASH_TIME_OUT = 3000;
    private Handler mHandler;
    private long mStartTime;

    /**
     * Method to start splash screen.
     * <p>
     * If you have not logic call 'startAppWithMethod' method inside this
     */
    protected abstract void onSplashStarted();

    @Override
    public void onStart() {
        super.onStart();

        mHandler = new Handler();
    }

    @Override
    public void onResume() {
        super.onResume();

        mStartTime = System.currentTimeMillis();

        onSplashStarted();
    }

    @Override
    public void onPause() {
        super.onPause();

        cancelAllRequests();

        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Splash timeout.
     *
     * @return the splash timeout.
     */
    protected long getSplashTimeOut() {
        return SPLASH_TIME_OUT;
    }

    /**
     * Called when splash finishes.
     *
     * @param onFinishSplashScreen splash finisghes callback.
     */
    protected void onSplashFinish(final OnFinishSplashScreen onFinishSplashScreen) {
        if (mHandler == null) {
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (onFinishSplashScreen != null) {
                    onFinishSplashScreen.onFinish();
                }
            }
        }, getTimeout());
    }

    /**
     * Gets the timeout for the splash finishes.
     *
     * @return the timeout.
     */
    private long getTimeout() {
        long diff = System.currentTimeMillis() - mStartTime;

        if (diff > getSplashTimeOut()) {
            return 0;
        }

        return getSplashTimeOut() - diff;
    }

    /**
     * Callback when splash finishes.
     */
    public interface OnFinishSplashScreen {
        /**
         * Splash finish.
         */
        void onFinish();
    }
}
