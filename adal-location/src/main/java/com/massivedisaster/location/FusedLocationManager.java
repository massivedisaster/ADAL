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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.massivedisaster.adal.permissions.PermissionsManager;
import com.massivedisaster.location.listener.OnFusedLocationManager;
import com.massivedisaster.location.utils.LocationError;
import com.massivedisaster.location.utils.LocationUtils;

public class FusedLocationManager implements LocationListener {

    private static final long DEFAULT_TIMEOUT_LOCATION = 20000;
    private static final long REQUEST_LOCATION_INTERVAL = 1000;
    private static final long REQUEST_LOCATION_FASTEST_INTERVAL = 1000;
    private static final int REQUEST_CHECK_SETTINGS = 909;

    private static final int PROVIDER_ENABLED = 1;
    private static final int PROVIDER_DISABLED = 0;

    private boolean mLastKnowLocation;
    private OnFusedLocationManager mOnFusedLocationManager;

    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;
    private long mTimeout;
    private PermissionsManager mPermissionsManager;
    private Handler mHandler;

    private LocationManager mLocationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates;

    private BroadcastReceiver mGpsLocationReceiver;

    private int mStatus = -1;

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
     * Stop and destroy location requests
     */
    public void onDestroy() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            stopLocationUpdates();
        }

        mHandler.removeCallbacksAndMessages(null);

        mActivity.unregisterReceiver(mGpsLocationReceiver);
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
     * Callback for the result from startResolutionForResult from Status. This method is invoked for every call
     * on onActivityResult(int, int, Intent).
     *
     * @param requestCode
     * @param resultCode
     */
    public void onActivityResult(int requestCode, int resultCode) {
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) {
            startRequestLocation();
        }
    }

    /**
     * Initialize location manager.
     */
    private void initialize() {
        mLocationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mHandler = new Handler();

        mGpsLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager != null) {
                        boolean isLocationEnabled = LocationUtils.isLocationEnabled(locationManager);
                        if (isLocationEnabled && mStatus != PROVIDER_ENABLED) {
                            mStatus = PROVIDER_ENABLED;
                            Log.w(FusedLocationManager.class.getSimpleName(), "PROVIDER PROVIDER_ENABLED");
                            if (mOnFusedLocationManager != null) {
                                mOnFusedLocationManager.onProviderEnabled();
                            }
                        } else if (!isLocationEnabled && mStatus != PROVIDER_DISABLED) {
                            mStatus = PROVIDER_DISABLED;
                            Log.w(FusedLocationManager.class.getSimpleName(), "PROVIDER PROVIDER_DISABLED");
                            if (mOnFusedLocationManager != null) {
                                mOnFusedLocationManager.onProviderDisabled();
                            }
                        }
                    } else {
                        Log.w(FusedLocationManager.class.getSimpleName(), "PROVIDER ERROR");
                    }
                }
            }
        };

        mActivity.registerReceiver(mGpsLocationReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

        if (mFragment == null) {
            mPermissionsManager = new PermissionsManager(mActivity);
        } else {
            mPermissionsManager = new PermissionsManager(mFragment);
        }
    }

    /**
     * Request a single user location
     *
     * @param lastKnowLocation       if you want to get last know location in case of timeout
     * @param onFusedLocationManager callback
     */
    public void requestSingleLocation(boolean lastKnowLocation, OnFusedLocationManager onFusedLocationManager) {
        requestSingleLocation(lastKnowLocation, DEFAULT_TIMEOUT_LOCATION, onFusedLocationManager);
    }

    /**
     * Request a single user location with a custom timeout
     *
     * @param lastKnowLocation       if you want to get last know location in case of timeout
     * @param timeOut                to get the user location
     * @param onFusedLocationManager callback
     */
    public void requestSingleLocation(boolean lastKnowLocation, long timeOut, OnFusedLocationManager onFusedLocationManager) {
        if (mRequestingLocationUpdates && onFusedLocationManager != null) {
            onFusedLocationManager.onLocationError(LocationError.UPDATES_ENABLED);
            return;
        }

        mTimeout = timeOut;
        mLastKnowLocation = lastKnowLocation;
        mOnFusedLocationManager = onFusedLocationManager;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        connectGoogleAPI();
    }

    /**
     * Request updated locations
     *
     * @param onFusedLocationManager callback
     */
    public void requestLocationUpdates(OnFusedLocationManager onFusedLocationManager) {
        requestLocationUpdates(REQUEST_LOCATION_INTERVAL, onFusedLocationManager);
    }

    /**
     * Request updated locations
     *
     * @param interval               for active location updates in milliseconds
     * @param onFusedLocationManager callback
     */
    public void requestLocationUpdates(long interval, OnFusedLocationManager onFusedLocationManager) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(REQUEST_LOCATION_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        requestLocationUpdates(locationRequest, onFusedLocationManager);
    }

    /**
     * Request updated locations
     *
     * @param locationRequest        to put settings to request location updates
     * @param onFusedLocationManager callback
     */
    public void requestLocationUpdates(LocationRequest locationRequest, OnFusedLocationManager onFusedLocationManager) {
        mTimeout = DEFAULT_TIMEOUT_LOCATION;
        mLastKnowLocation = false;
        mOnFusedLocationManager = onFusedLocationManager;

        mLocationRequest = locationRequest;

        connectGoogleAPI();
    }

    /**
     * Callend when location changes.
     *
     * @param location the location.
     */
    @Override
    public void onLocationChanged(Location location) {
        mHandler.removeCallbacksAndMessages(null);

        mOnFusedLocationManager.onLocationFound(location, false);
    }

    /**
     * Stop the updates from request location
     */
    public void stopLocationUpdates() {
        mRequestingLocationUpdates = false;
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Connect Gooogle API Client
     */
    private void connectGoogleAPI() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        requestPermissions();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        mOnFusedLocationManager.onLocationError(LocationError.DISABLED);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        mOnFusedLocationManager.onLocationError(LocationError.DISABLED);
                    }
                })
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Request permissions to request locations
     */
    private void requestPermissions() {
        mPermissionsManager.requestPermissions(
                new PermissionsManager.OnPermissionsListener() {
                    @Override
                    public void onGranted() {
                        if (LocationUtils.isLocationEnabled(mLocationManager)) {
                            startRequestLocation();
                        } else {
                            mOnFusedLocationManager.onLocationError(LocationError.DISABLED);
                        }
                    }

                    @Override
                    public void onDenied(boolean showRationale) {
                        mOnFusedLocationManager.onPermissionsDenied();
                    }
                },
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * Check the providers
     */
    private void startRequestLocation() {
        if (mLocationRequest == null) {
            mOnFusedLocationManager.onLocationError(LocationError.REQUEST_NEEDED);
            return;
        }

        LocationSettingsRequest locationSettingsRequest =
                new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest).build();

        PendingResult<LocationSettingsResult> pendingResult =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequest);

        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                Log.w(getClass().getSimpleName(), "onRequestResult");
                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        mRequestingLocationUpdates = false;
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        mOnFusedLocationManager.onProviderDisabled();
                        break;
                    default:
                        mRequestingLocationUpdates = false;
                        mOnFusedLocationManager.onLocationError(LocationError.DISABLED);
                        break;
                }
            }
        });
    }

    /**
     * Start the request location updates
     */
    private void startLocationUpdates() {
        if (!mGoogleApiClient.isConnected() || !mRequestingLocationUpdates) {
            mOnFusedLocationManager.onLocationError(LocationError.DISABLED);
            return;
        }

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        Runnable mRun = new Runnable() {
            public void run() {
                try {
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    stopLocationUpdates();
                    if (mLastKnowLocation && lastLocation != null) {
                        mOnFusedLocationManager.onLocationFound(lastLocation, true);
                    } else {
                        mOnFusedLocationManager.onLocationError(LocationError.TIMEOUT);
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                    mOnFusedLocationManager.onLocationError(LocationError.TIMEOUT);
                }
            }
        };

        mHandler.postDelayed(mRun, mTimeout);
    }
}
