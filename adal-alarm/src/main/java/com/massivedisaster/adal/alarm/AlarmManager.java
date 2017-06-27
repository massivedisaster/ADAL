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

package com.massivedisaster.adal.alarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.massivedisaster.adal.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class for managing alarms.
 */
public final class AlarmManager {

    private static final String TAG_ALARMS = ":alarms";

    /**
     * Private constructor to avoid user implement as a single instance instead of a Singleton.
     */
    private AlarmManager() {
    }

    /**
     * Add alarm to the system
     *
     * @param context        The Context in which this PendingIntent should perform the broadcast.
     * @param intent         The Intent to be broadcast.
     * @param notificationId Unique id to identify the alarm
     * @param calendar       Time that the alarm should go off
     */
    public static void addAlarm(Context context, Intent intent, int notificationId, Calendar calendar) {

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        saveAlarmId(context, notificationId);
    }

    /**
     * Cancel alarm added before
     *
     * @param context        The Context in which this PendingIntent should perform the broadcast.
     * @param intent         The Intent to be cancelled.
     * @param notificationId Unique id to identify the alarm
     */
    public static void cancelAlarm(Context context, Intent intent, int notificationId) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        removeAlarmId(context, notificationId);
    }

    /**
     * Cancel all alarms added before for a specif intent
     *
     * @param context The Context in which this PendingIntent should perform the broadcast.
     * @param intent  The Intent to be cancelled.
     */
    public static void cancelAllAlarms(Context context, Intent intent) {
        for (int idAlarm : getAlarmIds(context)) {
            cancelAlarm(context, intent, idAlarm);
        }
    }

    /**
     * Verify if a specific alarm is added based on intent and notification id
     *
     * @param context        The Context in which this PendingIntent should perform the broadcast.
     * @param intent         The Intent to be verified.
     * @param notificationId The Notification Id to be verified.
     * @return True if the alarm exists.
     */
    public static boolean hasAlarm(Context context, Intent intent, int notificationId) {
        return PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    /**
     * Saves alarm by identifier.
     *
     * @param context the context.
     * @param id      the identifier.
     */
    private static void saveAlarmId(Context context, int id) {
        List<Integer> idsAlarms = getAlarmIds(context);

        if (idsAlarms.contains(id)) {
            return;
        }

        idsAlarms.add(id);

        saveIdsInPreferences(context, idsAlarms);
    }

    /**
     * Remove alarm by identifier.
     *
     * @param context the context.
     * @param id      the identifier.
     */
    private static void removeAlarmId(Context context, int id) {
        List<Integer> idsAlarms = getAlarmIds(context);

        for (int i = 0; i < idsAlarms.size(); i++) {
            if (idsAlarms.get(i) == id) {
                idsAlarms.remove(i);
            }
        }

        saveIdsInPreferences(context, idsAlarms);
    }

    /**
     * Get all alarms identifiers.
     *
     * @param context the context.
     * @return list of alarm identifiers.
     */
    private static List<Integer> getAlarmIds(Context context) {
        List<Integer> ids = new ArrayList<>();
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            JSONArray jsonArray2 = new JSONArray(prefs.getString(context.getPackageName() + TAG_ALARMS, "[]"));

            for (int i = 0; i < jsonArray2.length(); i++) {
                ids.add(jsonArray2.getInt(i));
            }

        } catch (JSONException e) {
            LogUtils.logErrorException(AlarmManager.class, e);
        }

        return ids;
    }

    /**
     * Saves all identifiers in preferences.
     *
     * @param context the context.
     * @param lstIds  list of alarm identifiers.
     */
    private static void saveIdsInPreferences(Context context, List<Integer> lstIds) {
        JSONArray jsonArray = new JSONArray();
        for (Integer idAlarm : lstIds) {
            jsonArray.put(idAlarm);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getPackageName() + TAG_ALARMS, jsonArray.toString());

        editor.apply();
    }
}
