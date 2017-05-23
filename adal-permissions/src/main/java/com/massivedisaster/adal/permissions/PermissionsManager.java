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

public class PermissionsManager {

    private static final int sRequestCode = 99;
    private Activity mActivity;
    private Fragment mFragment;
    private String[] mPermissions;
    private int mRequestCode;
    private OnPermissionsListener mOnPermissionsListener;

    private PermissionsManager(Activity activity) {
        mActivity = activity;
    }

    private PermissionsManager(Fragment fragment) {
        mFragment = fragment;
        mActivity = fragment.getActivity();
    }

    /**
     * Initialize PermissionsManager inside a Activity
     *
     * @param activity to manage the permissions
     * @return the PermissionsManager instance
     */
    public static PermissionsManager getInstance(Activity activity) {
        return new PermissionsManager(activity);
    }

    /**
     * Initialize PermissionsManager inside a Fragment
     *
     * @param fragment to manage the permissions
     * @return the PermissionsManager instance
     */
    public static PermissionsManager getInstance(Fragment fragment) {
        return new PermissionsManager(fragment);
    }

    public void requestPermissions(@NonNull OnPermissionsListener onPermissionsListener,
                                   @NonNull String... permissions) {
        requestPermissions(sRequestCode, onPermissionsListener, permissions);
    }

    public void requestPermissions(int requestCode,
                                   @NonNull OnPermissionsListener onPermissionsListener,
                                   @NonNull String... permissions) {
        mPermissions = permissions;
        mRequestCode = requestCode;
        mOnPermissionsListener = onPermissionsListener;

        //Verify if permissions are added in manifest
        for (String s : permissions) {
            if (!PermissionUtils.hasPermissionInManifest((mFragment != null ? mFragment.getContext() : mActivity), s)) {
                throw new RuntimeException("Please add permission to manifest: " + s);
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
                if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                    mOnPermissionsListener.onDenied(true);
                    return;
                }
            }

            mOnPermissionsListener.onDenied(false);
        }
    }

    private void logInfo(String message) {
        Log.i(PermissionsManager.class.getCanonicalName(), message);
    }

    public interface OnPermissionsListener {
        void onGranted();

        void onDenied(boolean neverAskMeAgain);
    }
}