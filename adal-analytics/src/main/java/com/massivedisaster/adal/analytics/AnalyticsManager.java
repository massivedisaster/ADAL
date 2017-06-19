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

package com.massivedisaster.adal.analytics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Google Analytics manager.
 */
public final class AnalyticsManager {

    private static AnalyticsManager sInstance;
    private final Context mContext;
    private Tracker mTracker;

    /**
     * Creates an instance of {@link AnalyticsManager}
     *
     * @param context the context
     */
    private AnalyticsManager(@NonNull Context context) {
        mContext = context.getApplicationContext();
        getTracker();
    }

    /**
     * Initialize AnalyticsManager in your Application class
     *
     * @param context the context
     * @return an instance of {@link AnalyticsManager}
     */
    public static AnalyticsManager with(@NonNull Context context) {
        synchronized (context) {
            if (sInstance == null) {
                sInstance = new AnalyticsManager(context);
            }
            return sInstance;
        }
    }

    /**
     * Gets the google analytics tracker.
     *
     * @return the google analytics tracker.
     */
    private Tracker getTracker() {
        if (mTracker == null) {

            int checkExistence = mContext.getResources().getIdentifier("global_tracker", "xml", mContext.getPackageName());

            if (checkExistence == 0) {
                Log.e(AnalyticsManager.class.getCanonicalName(), "Please check if you have global-services.json");
            } else {
                mTracker = GoogleAnalytics.getInstance(mContext).newTracker(checkExistence);
                mTracker.enableAutoActivityTracking(false);
                mTracker.enableAdvertisingIdCollection(false);
                mTracker.enableExceptionReporting(false);
            }
        }
        return mTracker;
    }

    /**
     * Send a screen with screenId
     *
     * @param screenId from strings resources
     * @param label    label to format arguments in string resource
     */
    public void sendScreen(int screenId, String... label) {
        if (screenId == 0) {
            return;
        }
        if (label == null) {
            sendScreen(mContext.getString(screenId));
        } else {
            sendScreen(mContext.getString(screenId, (Object[]) label));
        }
    }

    /**
     * Send a screen with screenName
     *
     * @param screenName the screenName
     */
    public void sendScreen(@NonNull String screenName) {
        if (getTracker() == null) {
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.i(getClass().getCanonicalName(), "Setting Screen name: " + screenName);
        }

        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * Send a screen with screenName
     *
     * @param resScreenName the screenName
     */
    public void sendScreen(@NonNull Integer resScreenName) {
        if (getTracker() == null) {
            return;
        }

        sendScreen(mContext.getString(resScreenName));
    }

    /**
     * Send an event with categoryId and actionId
     *
     * @param categoryId from strings resources
     * @param actionId   from strings resources
     */
    public void sendEvent(int categoryId, int actionId) {
        sendEvent(categoryId, actionId, null);
    }

    /**
     * Send an event with categoryId, actionId and label
     *
     * @param categoryId from strings resources
     * @param actionId   from strings resources
     * @param label      the label
     */
    public void sendEvent(int categoryId, int actionId, String label) {
        if (getTracker() == null) {
            return;
        }

        if (categoryId == 0 || actionId == 0) {
            return;
        }

        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(mContext.getString(categoryId))
                .setAction(mContext.getString(actionId));

        if (label != null) {
            builder.setLabel(label);
        }

        if (BuildConfig.DEBUG) {
            Log.i(getClass().getCanonicalName(), "Setting Event category: " + mContext.getString(categoryId));
            Log.i(getClass().getCanonicalName(), "Setting Event action: " + mContext.getString(actionId));
        }

        mTracker.send(builder.build());
    }
}
