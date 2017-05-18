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

package com.massivedisaster.adal.utils;

import android.app.NotificationManager;
import android.content.Context;

public class NotificationUtils {

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

    private static void internalCancelNotification(Context context, String tag, int notificationId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
        nMgr.cancel(tag, notificationId);
    }
}
