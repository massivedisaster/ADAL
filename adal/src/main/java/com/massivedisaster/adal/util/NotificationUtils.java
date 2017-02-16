package com.massivedisaster.adal.util;

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
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
        nMgr.cancel(notificationId);
    }

    /**
     * Cancel a specify notification based on notification id and tag
     *
     * @param context        the application context
     * @param tag            the notification tag
     * @param notificationId the notification id
     */
    public static void cancelNotification(Context context, String tag, int notificationId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
        nMgr.cancel(tag, notificationId);
    }
}
