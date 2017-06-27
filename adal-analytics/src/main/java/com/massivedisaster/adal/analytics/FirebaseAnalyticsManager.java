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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;
import java.util.Set;

/**
 * Firebase Analytics Manager
 */
public final class FirebaseAnalyticsManager {

    /**
     * Private constructor to avoid user implement as a single instance instead of a Singleton.
     */
    private FirebaseAnalyticsManager() {
    }

    /**
     * Send a screen with screenId
     *
     * @param activity     the activity the activity
     * @param screenNameId from strings resources
     */
    public static void sendScreen(Activity activity, @StringRes int screenNameId) {
        if (screenNameId == 0) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a screenName to send the Screen");
            return;
        }
        sendScreen(activity, activity.getString(screenNameId));
    }

    /**
     * Send a screen with screenId
     *
     * @param activity     the activity the activity
     * @param screenNameId from strings resources
     * @param label        label to format arguments in string resource
     */
    public static void sendScreen(Activity activity, @StringRes int screenNameId, String label) {
        if (screenNameId == 0) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a screenName to send the Screen");
            return;
        }
        if (label == null) {
            sendScreen(activity, activity.getString(screenNameId));
        } else {
            sendScreen(activity, activity.getString(screenNameId, label));
        }
    }

    /**
     * Send a screen with screenName
     *
     * @param activity   the activity
     * @param screenName the screen name
     */
    public static void sendScreen(Activity activity, String screenName) {

        if (TextUtils.isEmpty(screenName)) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a screenName to send the Screen");
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.i(FirebaseAnalyticsManager.class.getCanonicalName(), "Setting Screen name: " + screenName);
        }

        FirebaseAnalytics.getInstance(activity).setCurrentScreen(activity, screenName, null);
    }

    /**
     * Send an event with eventName with all event types on bundle
     *
     * @param activity    the activity
     * @param eventNameId name that will appear on timeline
     * @param eventInfo   info about the event
     */
    public static void sendEvent(Activity activity, @StringRes int eventNameId, Map<String, String> eventInfo) {
        if (eventNameId == 0) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a eventName to send the Event");
            return;
        }
        sendEvent(activity, activity.getString(eventNameId), eventInfo);
    }

    /**
     * Send an event with eventName with all event types on bundle
     *
     * @param activity  the activity
     * @param eventName name that will appear on timeline
     * @param eventInfo info about the event
     */
    public static void sendEvent(Activity activity, String eventName, Map<String, String> eventInfo) {
        if (TextUtils.isEmpty(eventName)) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a eventName to send the Event");
            return;
        }
        if (eventInfo == null || eventInfo.isEmpty()) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a eventInfo to send the Event");
            return;
        }

        Bundle bundle = new Bundle();
        Set<Map.Entry<String, String>> setEntry = eventInfo.entrySet();
        for (Map.Entry<String, String> entry : setEntry) {
            bundle.putString(entry.getKey(), entry.getValue());
        }

        FirebaseAnalytics.getInstance(activity).logEvent(eventName, bundle);
    }

    /**
     * Send a user property (propertyId) with value
     *
     * @param activity   the activity
     * @param propertyId resource id to property string
     * @param valueId    resource id to value string
     */
    public static void sendUserProperty(Activity activity, @StringRes int propertyId, @StringRes int valueId) {
        if (propertyId == 0) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a propertyId to send the User Property");
            return;
        }
        if (valueId == 0) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a valueId to send the User Property");
            return;
        }
        sendUserProperty(activity, activity.getString(propertyId), activity.getString(valueId));
    }

    /**
     * Send a user property (propertyId) with value
     *
     * @param activity   the activity
     * @param propertyId resource id to property string
     * @param value      the value
     */
    public static void sendUserProperty(Activity activity, @StringRes int propertyId, String value) {
        if (propertyId == 0) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a propertyId to send the User Property");
            return;
        }
        sendUserProperty(activity, activity.getString(propertyId), value);
    }

    /**
     * Send a user property (property) with value
     *
     * @param activity the activity
     * @param property the property name
     * @param value    the value
     */
    public static void sendUserProperty(Activity activity, String property, String value) {
        if (TextUtils.isEmpty(property)) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a property to send the User Property");
            return;
        }
        if (TextUtils.isEmpty(value)) {
            Log.w(FirebaseAnalyticsManager.class.getCanonicalName(), "You need a value to send the User Property");
            return;
        }
        FirebaseAnalytics.getInstance(activity).setUserProperty(property, value);
    }
}
