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
