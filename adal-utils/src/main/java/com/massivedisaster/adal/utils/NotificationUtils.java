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

package com.massivedisaster.adal.utils;

import android.app.NotificationManager;
import android.content.Context;

/**
 * Class that allow users to cancel notifications on application
 */
public final class NotificationUtils {

    /**
     * Private constructor to avoid user implement as a single instance instead of a Singleton
     */
    private NotificationUtils() {

    }

    /**
     * Cancel a specify notification based on notification id
     *
     * @param context        the application context
     * @param notificationId the notification id
     */
    public static void cancelNotification(Context context, int notificationId) {
        internalCancelNotification(context, null, notificationId);
    }

    /**
     * Cancel a specify notification based on notification id and tag
     *
     * @param context        the application context
     * @param tag            the notification tag
     * @param notificationId the notification id
     */
    public static void cancelNotification(Context context, String tag, int notificationId) {
        internalCancelNotification(context, tag, notificationId);
    }

    /**
     * Cancel a specify notification given a tag and an id
     *
     * @param context        the application context
     * @param tag            the notification tag
     * @param notificationId the notification id
     */
    private static void internalCancelNotification(Context context, String tag, int notificationId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
        nMgr.cancel(tag, notificationId);
    }
}
