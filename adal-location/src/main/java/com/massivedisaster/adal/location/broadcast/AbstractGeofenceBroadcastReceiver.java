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

package com.massivedisaster.adal.location.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class AbstractGeofenceBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_RECEIVE_GEOFENCE = "com.massivedisaster.adal.location.ACTION_RECEIVE_GEOFENCE";

    private static final String sNullActionException = "You must define an action on your pending intent";
    private static final String sBootCompleteMessage = "::: ::: ::: BOOT - ON RECEIVE";
    private static final String sGeofenceReceivedMessage = "::: ::: ::: GEOFENCE - ON RECEIVE";

    /**
     * Handles broadcast message when action
     * equals to {@link android.content.Intent#ACTION_BOOT_COMPLETED ACTION_BOOT_COMPLETED}
     *
     * @param context used by the application
     * @param intent declared as a Pending Intent by the system or a user's request
     */
    protected abstract void onBootCompleted(Context context, Intent intent);

    /**
     * Handles broadcast message when action
     * equals to {@link #ACTION_RECEIVE_GEOFENCE ACTION_RECEIVE_GEOFENCE}
     *
     * @param context used by the application
     * @param intent declared as a Pending Intent by the system or a user's request
     */
    protected abstract void onReceiveGeofence(Context context, Intent intent);

    /**
     * @inheritdoc
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == null) {
            throw new NullPointerException(sNullActionException);
        }

        if(action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(AbstractGeofenceBroadcastReceiver.class.getSimpleName(), sBootCompleteMessage );
            onBootCompleted(context, intent);
        } else if(action.equals(ACTION_RECEIVE_GEOFENCE)) {
            Log.d(AbstractGeofenceBroadcastReceiver.class.getSimpleName(), sGeofenceReceivedMessage);
            onReceiveGeofence(context, intent);
        }
    }
}
