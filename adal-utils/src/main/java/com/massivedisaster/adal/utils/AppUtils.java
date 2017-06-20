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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

/**
 * Manages general app behaviours and actions like open specific intents (dial, settings and email)
 * and google play services features
 */
public final class AppUtils {

    /**
     * Private constructor to avoid user implement as a single instance instead of a Singleton
     */
    private AppUtils() {

    }

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

    /**
     * Generates an intent to open app settings
     *
     * @param context the application context
     * @return settings intent
     */
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

            if (!canResolveIntent(context, intent)) {
                return;
            }

            context.startActivity(intent);
        } catch (IllegalStateException e) {
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
        intent.putExtra(Intent.EXTRA_EMAIL, email);

        if (!canResolveIntent(context, intent)) {
            return;
        }

        context.startActivity(Intent.createChooser(intent, intentTitle));
    }

    /**
     * Tell if an intent can be resolved.
     *
     * @param context the context
     * @param intent  the intent
     * @return returns true if intent can be resolved.
     */
    public static boolean canResolveIntent(Context context, Intent intent) {
        final PackageManager mgr = context.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

}
