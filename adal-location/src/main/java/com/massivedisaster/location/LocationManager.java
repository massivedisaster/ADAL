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
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.massivedisaster.adal.permissions.PermissionsManager;
import com.massivedisaster.location.listener.OnLocationManager;
import com.massivedisaster.location.utils.LocationError;
import com.massivedisaster.location.utils.LocationUtils;

/**
 * Location manager.
 */
public class LocationManager implements LocationListener {

    private static final long DEFAULT_TIMEOUT_LOCATION = 20000;
    protected android.location.LocationManager mLocationManager;
    protected String mProvider;
    protected boolean mLastKnowLocation;
    protected OnLocationManager mOnLocationManager;
    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;
    private long mTimeout;
    private PermissionsManager mPermissionsManager;
    private Handler mHandler;

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
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(this);
        }

        mHandler.removeCallbacksAndMessages(null);
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

    /**
     * Initialize location manager.
     */
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
        requestSingleLocation(provider, lastKnowLocation, DEFAULT_TIMEOUT_LOCATION, onLocationManager);
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

    /**
     * Stat the request location process.
     */
    @SuppressWarnings("MissingPermission")
    protected void startRequestLocation() {
        mLocationManager.requestSingleUpdate(mProvider, this, null);

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

    /**
     * Callend when location changes.
     *
     * @param location the location.
     */
    @Override
    public void onLocationChanged(Location location) {
        mHandler.removeCallbacksAndMessages(null);

        mOnLocationManager.onLocationFound(location, false);
    }

    /**
     * Called when status changes.
     *
     * @param provider the provider.
     * @param status   the status.
     * @param extras   the extras.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Intended.
    }

    /**
     * Called when provider is enabled.
     *
     * @param provider the provider.
     */
    @Override
    public void onProviderEnabled(String provider) {
        mOnLocationManager.onProviderEnabled(provider);
    }

    /**
     * Called when provider is disabled.
     *
     * @param provider the provider.
     */
    @Override
    public void onProviderDisabled(String provider) {
        mOnLocationManager.onProviderDisabled(provider);
    }
}
