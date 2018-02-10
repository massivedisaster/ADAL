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

package com.massivedisaster.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.massivedisaster.adal.permissions.PermissionsManager;
import com.massivedisaster.location.listener.OnLocationManager;
import com.massivedisaster.location.utils.LocationError;
import com.massivedisaster.location.utils.LocationUtils;

/**
 * ADAL by Carbon by BOLD
 * Created in 29/09/17 by the following authors:
 * <p>
 * Nuno Silva
 */
public abstract class AbstractPermissionsLocationManager {

    protected Activity mActivity;
    protected Fragment mFragment;
    protected Context mContext;

    private PermissionsManager mPermissionsManager;

    protected OnLocationManager mOnLocationManager;
    private android.location.LocationManager mLocationManager;

    /**
     * Start the request location updates
     */
    protected abstract void startRequestLocation();

    /**
     * Initialize Permissions Manager
     */
    protected void initPermissionsManager() {
        mLocationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (mFragment != null) {
            mPermissionsManager = new PermissionsManager(mFragment);
        } else if (mActivity != null) {
            mPermissionsManager = new PermissionsManager(mActivity);
        } else {
            onGrantedPermission();
        }
    }

    /**
     * Request permissions to request locations
     */
    protected void requestPermissions() {
        if (mPermissionsManager == null) {
            onGrantedPermission();
            return;
        }
        mPermissionsManager.requestPermissions(
                new PermissionsManager.OnPermissionsListener() {
                    @Override
                    public void onGranted() {
                        onGrantedPermission();
                    }

                    @Override
                    public void onDenied(boolean showRationale) {
                        if (mOnLocationManager != null) {
                            mOnLocationManager.onPermissionsDenied();
                        }
                    }
                },
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * When permissions were granted
     */
    protected void onGrantedPermission() {
        try {
            if (LocationUtils.isLocationEnabled(mLocationManager)) {
                startRequestLocation();
            } else if (mOnLocationManager != null) {
                mOnLocationManager.onLocationError(LocationError.DISABLED);
            }
        } catch (SecurityException e) {
            Log.e(getClass().getSimpleName(), e.toString());
            if (mOnLocationManager != null) {
                mOnLocationManager.onPermissionsDenied();
            }
        }
    }

    /**
     * Callback for the result from requesting permissions. This method is invoked for every call
     * on requestPermissions(android.app.Activity, String[], int).
     *
     * @param requestCode  The request code passed in requestPermissions(android.app.Activity, String[], int)
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED.
     *                     Never null.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int... grantResults) {
        mPermissionsManager.onPermissionResult(requestCode);
    }
}
