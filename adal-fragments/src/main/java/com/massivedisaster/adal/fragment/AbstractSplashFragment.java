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
