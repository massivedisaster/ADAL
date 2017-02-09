package com.massivedisaster.adal.fragment;

import android.os.Handler;

public abstract class AbstractSplashFragment extends AbstractRequestFragment {

    /**
     * Method to start splashscreen.
     * <p>
     * If you have not logic call 'startAppWithMethod' method inside this
     */
    protected abstract void onSplashStarted();

    private static final long SPLASH_TIME_OUT = 3000;

    private Handler mHandler;
    private long mStartTime;

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

    protected long getSplashTimeOut() {
        return SPLASH_TIME_OUT;
    }

    protected void onSplashFinish(final OnFinishSplashScreen onFinishSplashscreen) {
        if (mHandler == null) return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (onFinishSplashscreen != null) {
                    onFinishSplashscreen.onFinish();
                }
            }
        }, getTimeout());
    }

    private long getTimeout() {
        long diff = System.currentTimeMillis() - mStartTime;

        if (diff > getSplashTimeOut()) {
            return getSplashTimeOut();
        }

        return getSplashTimeOut() - diff;
    }

    public interface OnFinishSplashScreen {
        void onFinish();
    }
}