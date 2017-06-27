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

package com.massivedisaster.adal.permissions;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Permissions manager.
 */
public final class PermissionsManager {

    private static final int REQUEST_CODE = 99;
    private final Activity mActivity;
    private Fragment mFragment;
    private String[] mPermissions;
    private int mRequestCode;
    private OnPermissionsListener mOnPermissionsListener;

    /**
     * Constructs permissions manager.
     *
     * @param activity the activity.
     */
    public PermissionsManager(Activity activity) {
        mActivity = activity;
    }

    /**
     * Constructs permissions manager.
     *
     * @param fragment the fragment.
     */
    public PermissionsManager(Fragment fragment) {
        mFragment = fragment;
        mActivity = fragment.getActivity();
    }

    /**
     * Request permissions.
     *
     * @param onPermissionsListener the code associated with requested permissions.
     * @param permissions           list of permissions to ask.
     */
    public void requestPermissions(@NonNull OnPermissionsListener onPermissionsListener,
                                   @NonNull String... permissions) {
        requestPermissions(REQUEST_CODE, onPermissionsListener, permissions);
    }

    /**
     * Request permissions.
     *
     * @param requestCode           the code associated with requested permissions.
     * @param onPermissionsListener listener for permissions.
     * @param permissions           list of permissions to ask.
     */
    public void requestPermissions(int requestCode,
                                   @NonNull OnPermissionsListener onPermissionsListener,
                                   @NonNull String... permissions) {
        mPermissions = permissions;
        mRequestCode = requestCode;
        mOnPermissionsListener = onPermissionsListener;

        //Verify if permissions are added in manifest
        for (String s : permissions) {
            if (!PermissionUtils.hasPermissionInManifest((mFragment != null ? mFragment.getContext() : mActivity), s)) {
                throw new IllegalStateException("Please add permission to manifest: " + s);
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || PermissionUtils.hasPermissions(mActivity, mPermissions)) {
            if (mOnPermissionsListener != null) {
                mOnPermissionsListener.onGranted();
            }
            return;
        }

        if (mFragment != null) {
            mFragment.requestPermissions(mPermissions, mRequestCode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(mActivity, mPermissions, mRequestCode);
        }
    }

    /**
     * To be called on onPermissionsResult.
     *
     * @param requestCode the request code.
     */
    public void onPermissionResult(int requestCode) {
        if (requestCode != mRequestCode) {
            logInfo("requestCode is different: " + requestCode + " != " + mRequestCode);
            return;
        }
        if (PermissionUtils.hasPermissions(mActivity, mPermissions) && mOnPermissionsListener != null) {
            mOnPermissionsListener.onGranted();
        } else if (mOnPermissionsListener != null) {

            for (String permission : mPermissions) {
                // When shouldShowRequestPermissionRationale returns false the user checked never ask me again.
                if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)
                        && !PermissionUtils.hasPermissions(mActivity, permission)) {
                    mOnPermissionsListener.onDenied(true);
                    return;
                }
            }

            mOnPermissionsListener.onDenied(false);
        }
    }

    /**
     * Log information.
     *
     * @param message the message.
     */
    private void logInfo(String message) {
        Log.i(PermissionsManager.class.getCanonicalName(), message);
    }

    /**
     * On permissions listener.
     */
    public interface OnPermissionsListener {
        /**
         * On granted permission.
         */
        void onGranted();

        /**
         * On denied permission.
         *
         * @param neverAskMeAgain tells if the user clicked never ask me again.
         */
        void onDenied(boolean neverAskMeAgain);
    }
}
