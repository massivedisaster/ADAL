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

package com.massivedisaster.adal.analytics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AnalyticsManager {

    private static AnalyticsManager sInstance;
    private final Context mContext;
    private Tracker mTracker;

    private AnalyticsManager(@NonNull Context context) {
        mContext = context.getApplicationContext();
        getTracker();
    }

    /**
     * Initialize AnalyticsManager in your Application class
     *
     * @param context
     * @return
     */
    public static synchronized AnalyticsManager with(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new AnalyticsManager(context);
        }
        return sInstance;
    }

    private synchronized Tracker getTracker() {
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
    public synchronized void sendScreen(int screenId, String... label) {
        if (screenId == 0) return;
        if (label == null) {
            sendScreen(mContext.getString(screenId));
        } else {
            sendScreen(mContext.getString(screenId, (Object[]) label));
        }
    }

    /**
     * Send a screen with screenName
     *
     * @param screenName
     */
    public synchronized void sendScreen(@NonNull String screenName) {
        if (getTracker() == null) return;

        if (BuildConfig.DEBUG) {
            Log.i(getClass().getCanonicalName(), "Setting Screen name: " + screenName);
        }

        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * Send a screen with screenName
     *
     * @param resScreenName
     */
    public synchronized void sendScreen(@NonNull Integer resScreenName) {
        if (getTracker() == null) return;

        sendScreen(mContext.getString(resScreenName));
    }

    /**
     * Send an event with categoryId and actionId
     *
     * @param categoryId from strings resources
     * @param actionId   from strings resources
     */
    public synchronized void sendEvent(int categoryId, int actionId) {
        sendEvent(categoryId, actionId, null);
    }

    /**
     * Send an event with categoryId, actionId and label
     *
     * @param categoryId from strings resources
     * @param actionId   from strings resources
     * @param label
     */
    public synchronized void sendEvent(int categoryId, int actionId, String label) {
        if (getTracker() == null) return;

        if (categoryId == 0 || actionId == 0) return;

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