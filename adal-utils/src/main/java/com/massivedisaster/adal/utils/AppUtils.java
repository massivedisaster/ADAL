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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class AppUtils {

    /**
     * Open application settings for a specify context
     * Call OnActivityResult when user back to the app
     *
     * @param activity    to get the result
     * @param requestCode to identify in onActivityResult
     */
    public static void openAppSettings(Activity activity, int requestCode) {
        activity.startActivityForResult(createAppSettingsIntent(activity), requestCode);
    }

    /**
     * Open App Setting for a specify context
     *
     * @param context the application context
     */
    public static void openAppSettings(Context context) {
        context.startActivity(createAppSettingsIntent(context));
    }

    private static Intent createAppSettingsIntent(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    /**
     * Verify if Google Play Services are installed
     *
     * @param context application context
     * @return true if google play services exists
     */
    public static boolean checkPlayServicesExists(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    /**
     * Open to make a call to specific phone number
     *
     * @param context     application context
     * @param phoneNumber the phone number
     */
    public static void openDial(Context context, String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(AppUtils.class.getCanonicalName(), e.toString());
        }
    }

    /**
     * Open a Intent Chooser to choose a app to send a email
     *
     * @param context     application context
     * @param intentTitle intent chooser title
     * @param email       list of emails
     */
    public static void openEmail(Context context, String intentTitle, String... email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, email);
        context.startActivity(Intent.createChooser(intent, intentTitle));
    }

}