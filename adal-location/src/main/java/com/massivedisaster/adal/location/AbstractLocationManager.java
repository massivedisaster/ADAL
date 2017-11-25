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

package com.massivedisaster.adal.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.massivedisaster.adal.location.broadcast.LocationStatusBroadcastReceiver;
import com.massivedisaster.adal.location.listener.OnLocationManager;
import com.massivedisaster.adal.location.listener.OnLocationStatusProviderListener;
import com.massivedisaster.adal.location.utils.LocationError;
import com.massivedisaster.adal.location.utils.LocationUtils;
import com.massivedisaster.adal.permissions.PermissionsManager;

/**
 * AbstractLocationManager contains the base methods to request device location
 * <p>
 * PermissionsManager implemented in all requests.
 */
abstract class AbstractLocationManager implements LocationListener {

    protected static final int REQUEST_CHECK_SETTINGS = 909;

    private PermissionsManager mPermissionsManager;

    private Fragment mFragment;
    private Context mContext;
    private Handler mHandler;

    protected OnLocationManager mOnLocationManager;
    protected GoogleApiClient mGoogleApiClient;
    protected android.location.LocationManager mLocationManager;
    protected boolean mLastKnowLocation;

    private Activity mActivity;
    private long mTimeout;

    private final LocationStatusBroadcastReceiver mLocationStatusBroadcastReceiver = new LocationStatusBroadcastReceiver();

    /**
     * Start the request location updates
     */
    protected abstract void startRequestLocation();

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
     * Stop and destroy location requests
     */
    public void onDestroy() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            stopRequestLocation();
        }

        mHandler.removeCallbacksAndMessages(null);

        mActivity.unregisterReceiver(mLocationStatusBroadcastReceiver);
    }

    /**
     * Initialize location manager.
     */
    private void initialize() {
        mLocationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mHandler = new Handler();

        mLocationStatusBroadcastReceiver.setOnLocationStatusProviderListener(new OnLocationStatusProviderListener() {
            @Override
            public void onProviderEnabled() {
                Log.i(getClass().getSimpleName(), "PROVIDER PROVIDER_ENABLED");
                mOnLocationManager.onProviderEnabled();
            }

            @Override
            public void onProviderDisabled() {
                Log.i(getClass().getSimpleName(), "PROVIDER PROVIDER_DISABLED");
                mOnLocationManager.onProviderDisabled();
            }
        });

        mActivity.registerReceiver(mLocationStatusBroadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

        if (mFragment == null) {
            mPermissionsManager = new PermissionsManager(mActivity);
        } else {
            mPermissionsManager = new PermissionsManager(mFragment);
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
     * Connect Gooogle API Client
     */
    protected void connectGoogleAPI() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        requestPermissions();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        mOnLocationManager.onLocationError(LocationError.DISABLED);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        mOnLocationManager.onLocationError(LocationError.DISABLED);
                    }
                })
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Request permissions to request locations
     */
    protected void requestPermissions() {
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
     * Show the last location after timeout
     */
    protected void showLastLocationAfterTimeout() {
        Runnable mRun = new Runnable() {
            public void run() {
                try {
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    stopRequestLocation();
                    if (mLastKnowLocation && lastLocation != null) {
                        mOnLocationManager.onLocationFound(lastLocation, true);
                    } else {
                        mOnLocationManager.onLocationError(LocationError.TIMEOUT);
                    }
                } catch (SecurityException e) {
                    Log.e(getClass().getSimpleName(), e.toString());
                    mOnLocationManager.onLocationError(LocationError.TIMEOUT);
                }
            }
        };

        mHandler.postDelayed(mRun, mTimeout);
    }
}
