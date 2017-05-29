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

package com.massivedisaster.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.massivedisaster.adal.permissions.PermissionsManager;

public class LocationManager implements LocationListener {

    private static final long sDefaultTimeoutLocation = 20000;

    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;

    private String mProvider;
    private boolean mLastKnowLocation;
    private long mTimeout;

    private android.location.LocationManager mLocationManager;
    private PermissionsManager mPermissionsManager;
    private Handler mHandler;
    private OnLocationManager mOnLocationManager;

    /**
     * Initialize LocationManager
     *
     * @param activity to watch onRequestPermissionsResult
     */
    public void onCreate(Activity activity) {
        mActivity = activity;
        mContext = activity;

        initialize();
    }

    /**
     * Initialize LocationManager
     *
     * @param fragment to watch onRequestPermissionsResult
     */
    public void onCreate(Fragment fragment) {
        mFragment = fragment;
        mContext = fragment.getContext();

        initialize();
    }

    /**
     * Stop and destroy location requests
     */
    public void onDestroy() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(this);
        }

        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Callback for the result from requesting permissions. This method is invoked for every call on requestPermissions(android.app.Activity, String[], int).
     *
     * @param requestCode  The request code passed in requestPermissions(android.app.Activity, String[], int)
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsManager.onPermissionResult(requestCode);
    }

    private void initialize() {
        mLocationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mHandler = new Handler();

        if (mActivity != null) {
            mPermissionsManager = new PermissionsManager(mActivity);
        } else {
            mPermissionsManager = new PermissionsManager(mFragment);
        }
    }

    /**
     * Retrieve the LocationManager object
     *
     * @return The LocationManager
     */
    public android.location.LocationManager getLocationManager() {
        return mLocationManager;
    }

    /**
     * Request a single user location
     *
     * @param provider          you want to request user location
     * @param lastKnowLocation  if you want to get last know location in case of timeout
     * @param onLocationManager callback
     */
    public void requestSingleLocation(final String provider, boolean lastKnowLocation, OnLocationManager onLocationManager) {
        requestSingleLocation(provider, lastKnowLocation, sDefaultTimeoutLocation, onLocationManager);
    }

    /**
     * Request a single user location with a custom timeout
     *
     * @param provider          you want to request user location
     * @param lastKnowLocation  if you want to get last know location in case of timeout
     * @param timeOut           to get the user location
     * @param onLocationManager callback
     */
    public void requestSingleLocation(final String provider, boolean lastKnowLocation, long timeOut, OnLocationManager onLocationManager) {
        mProvider = provider;
        mTimeout = timeOut;
        mLastKnowLocation = lastKnowLocation;
        mOnLocationManager = onLocationManager;

        mPermissionsManager.requestPermissions(
                new PermissionsManager.OnPermissionsListener() {
                    @Override
                    public void onGranted() {
                        if (LocationUtils.isLocationEnabled(mLocationManager)) {
                            startRequestLocation();
                        } else {
                            mOnLocationManager.onLocationError(LocationError.DISABLED);
                        }
                    }

                    @Override
                    public void onDenied(boolean showRationale) {
                        mOnLocationManager.onPermissionsDenied();
                    }
                },
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @SuppressWarnings("MissingPermission")
    private void startRequestLocation() {
        mLocationManager.requestSingleUpdate(mProvider, LocationManager.this, null);

        Runnable mRun = new Runnable() {
            public void run() {
                Location position = mLocationManager.getLastKnownLocation(mProvider);
                mLocationManager.removeUpdates(LocationManager.this);

                if (mLastKnowLocation && position != null) {
                    mOnLocationManager.onLocationFound(position, true);
                } else {
                    mOnLocationManager.onLocationError(LocationError.TIMEOUT);
                }
            }
        };

        mHandler.postDelayed(mRun, mTimeout);
    }

    @Override
    public void onLocationChanged(Location location) {
        mHandler.removeCallbacksAndMessages(null);

        mOnLocationManager.onLocationFound(location, false);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        mOnLocationManager.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
