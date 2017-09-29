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
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.massivedisaster.location.broadcast.LocationStatusBroadcastReceiver;
import com.massivedisaster.location.listener.OnLocationStatusProviderListener;
import com.massivedisaster.location.utils.LocationError;

/**
 * AbstractLocationManager contains the base methods to request device location
 * <p>
 * PermissionsManager implemented in all requests.
 */
abstract class AbstractLocationManager extends AbstractPermissionsLocationManager {

    protected static final int REQUEST_CHECK_SETTINGS = 909;

    protected FusedLocationProviderClient mFusedLocationClient;
    protected SettingsClient mSettingsClient;

    protected LocationSettingsRequest mLocationSettingsRequest;
    protected LocationCallback mLocationCallback;

    protected boolean mLastKnowLocation;

    protected Handler mHandler;
    private long mTimeout;

    private final LocationStatusBroadcastReceiver mLocationStatusBroadcastReceiver = new LocationStatusBroadcastReceiver();

    /**
     * Start the request location updates
     */
    protected abstract void stopRequestLocation();

    /**
     * Initialize LocationManager
     *
     * @param activity to watch onRequestPermissionsResult and onActivityResult
     */
    public void onCreate(Activity activity) {
        mActivity = activity;
        mContext = activity;

        initialize();
    }

    /**
     * Initialize LocationManager
     *
     * @param fragment to watch onRequestPermissionsResult and onActivityResult
     */
    public void onCreate(Fragment fragment) {
        mFragment = fragment;
        mActivity = fragment.getActivity();
        mContext = fragment.getContext();

        initialize();
    }

    /**
     * Initialize LocationManager
     * <p>
     * Without fragment or activity the activityResult is losted
     *
     * @param context to request location
     */
    public void onCreate(Context context) {
        mContext = context;

        initialize();
    }

    /**
     * Stop and destroy location requests
     */
    public void onDestroy() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            stopRequestLocation();
        }

        mHandler.removeCallbacksAndMessages(null);

        if (mActivity != null) {
            mActivity.unregisterReceiver(mLocationStatusBroadcastReceiver);
        }
    }

    /**
     * Initialize location manager.
     */
    private void initialize() {
        mHandler = new Handler();

        if (mActivity != null) {
            mLocationStatusBroadcastReceiver.setOnLocationStatusProviderListener(new OnLocationStatusProviderListener() {
                @Override
                public void onProviderEnabled() {
                    Log.i(getClass().getSimpleName(), "PROVIDER PROVIDER_ENABLED");
                    if (mOnLocationManager != null) {
                        mOnLocationManager.onProviderEnabled();
                    }
                }

                @Override
                public void onProviderDisabled() {
                    Log.i(getClass().getSimpleName(), "PROVIDER PROVIDER_DISABLED");
                    if (mOnLocationManager != null) {
                        mOnLocationManager.onProviderDisabled();
                    }
                }
            });
            mActivity.registerReceiver(mLocationStatusBroadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
            mSettingsClient = LocationServices.getSettingsClient(mActivity);
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
            mSettingsClient = LocationServices.getSettingsClient(mContext);
        }

        initLocationCallback();

        initPermissionsManager();
    }

    /**
     * Initialize Location Callback
     */
    protected void initLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                mHandler.removeCallbacksAndMessages(null);

                if (mOnLocationManager != null) {
                    mOnLocationManager.onLocationFound(locationResult.getLastLocation(), false);
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                if (!locationAvailability.isLocationAvailable() && mOnLocationManager != null) {
                    mOnLocationManager.onLocationError(LocationError.DISABLED);
                }
            }
        };
    }

    /**
     * Callback for the result from startResolutionForResult from Status. This method is invoked for every call
     * on onActivityResult(int, int, Intent).
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     */
    public void onActivityResult(int requestCode, int resultCode) {
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) {
            startRequestLocation();
        }
    }

    /**
     * Set Timeout
     *
     * @param timeout in milliseconds
     */
    public void setTimeout(long timeout) {
        this.mTimeout = timeout;
    }

    /**
     * Set true if user wants receive the last location
     *
     * @param lastKnowLocation the value
     */
    public void setLastKnowLocation(boolean lastKnowLocation) {
        this.mLastKnowLocation = lastKnowLocation;
    }

    /**
     * Get the actual activity
     *
     * @return the actual activity
     */
    public Activity getActivity() {
        return mActivity;
    }

    /**
     * Create builder to request
     *
     * @param locationRequest the request
     */
    protected void buildRequest(LocationRequest locationRequest) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();

        requestPermissions();
    }

    /**
     * Show the last location after timeout
     */
    protected void showLastLocationAfterTimeout() {
        Runnable mRun = new Runnable() {
            public void run() {
                try {
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location lastLocation) {
                                    stopRequestLocation();
                                    if (mOnLocationManager != null) {
                                        if (mLastKnowLocation && lastLocation != null) {
                                            mOnLocationManager.onLocationFound(lastLocation, true);
                                        } else {
                                            mOnLocationManager.onLocationError(LocationError.TIMEOUT);
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (mOnLocationManager != null) {
                                        mOnLocationManager.onLocationError(LocationError.TIMEOUT);
                                    }
                                }
                            });
                } catch (SecurityException e) {
                    Log.e(getClass().getSimpleName(), e.toString());
                    if (mOnLocationManager != null) {
                        mOnLocationManager.onLocationError(LocationError.TIMEOUT);
                    }
                }
            }
        };

        mHandler.postDelayed(mRun, mTimeout);
    }
}
