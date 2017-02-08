package com.massivedisaster.adal.util;

import android.app.NotificationManager;
import android.content.Context;

public class NotificationUtils {

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    public static void cancelNotification(Context ctx, String tag, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(tag, notifyId);
    }
}
