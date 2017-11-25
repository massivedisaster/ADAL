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

import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.massivedisaster.adal.location.listener.OnLocationManager;
import com.massivedisaster.adal.location.utils.LocationError;

/**
 * LocationManager requests single or multiple device locations.
 */
public class LocationManager extends AbstractLocationManager {

    private static final long DEFAULT_TIMEOUT_LOCATION = 20000;
    private static final long REQUEST_LOCATION_INTERVAL = 1000;
    private static final long REQUEST_LOCATION_FASTEST_INTERVAL = 1000;

    private LocationRequest mLocationRequest;
    protected boolean mRequestingLocationUpdates;


    /**
     * Request a single user location
     *
     * @param lastKnowLocation  if you want to get last know location in case of timeout
     * @param onLocationManager callback
     */
    public void requestSingleLocation(boolean lastKnowLocation, OnLocationManager onLocationManager) {
        requestSingleLocation(lastKnowLocation, DEFAULT_TIMEOUT_LOCATION, onLocationManager);
    }

    /**
     * Request a single user location with a custom timeout
     *
     * @param lastKnowLocation  if you want to get last know location in case of timeout
     * @param timeOut           to get the user location
     * @param onLocationManager callback
     */
    public void requestSingleLocation(boolean lastKnowLocation, long timeOut, OnLocationManager onLocationManager) {
        if (mRequestingLocationUpdates && onLocationManager != null) {
            onLocationManager.onLocationError(LocationError.UPDATES_ENABLED);
            return;
        }

        setTimeout(timeOut);
        setLastKnowLocation(lastKnowLocation);
        mOnLocationManager = onLocationManager;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        connectGoogleAPI();
    }

    /**
     * Request updated locations
     *
     * @param onLocationManager callback
     */
    public void requestLocationUpdates(OnLocationManager onLocationManager) {
        requestLocationUpdates(REQUEST_LOCATION_INTERVAL, onLocationManager);
    }

    /**
     * Request updated locations
     *
     * @param interval          for active location updates in milliseconds
     * @param onLocationManager callback
     */
    public void requestLocationUpdates(long interval, OnLocationManager onLocationManager) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(REQUEST_LOCATION_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        requestLocationUpdates(locationRequest, onLocationManager);
    }

    /**
     * Request updated locations
     *
     * @param locationRequest   to put settings to request location updates
     * @param onLocationManager callback
     */
    public void requestLocationUpdates(LocationRequest locationRequest, OnLocationManager onLocationManager) {
        setTimeout(DEFAULT_TIMEOUT_LOCATION);
        setLastKnowLocation(false);
        mOnLocationManager = onLocationManager;

        mLocationRequest = locationRequest;

        connectGoogleAPI();
    }

    /**
     * Check the providers
     */
    @Override
    protected void startRequestLocation() {
        if (mLocationRequest == null) {
            mOnLocationManager.onLocationError(LocationError.REQUEST_NEEDED);
            return;
        }

        LocationSettingsRequest locationSettingsRequest =
                new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest).build();

        PendingResult<LocationSettingsResult> pendingResult =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequest);

        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
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
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(getClass().getSimpleName(), e.toString());
                        }
                        mOnLocationManager.onProviderDisabled();
                        break;
                    default:
                        mRequestingLocationUpdates = false;
                        mOnLocationManager.onLocationError(LocationError.DISABLED);
                        break;
                }
            }
        });
    }

    /**
     * Start the request location updates
     */
    protected void startLocationUpdates() {
        if (!mGoogleApiClient.isConnected() || !mRequestingLocationUpdates) {
            mOnLocationManager.onLocationError(LocationError.DISABLED);
            return;
        }

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }

        showLastLocationAfterTimeout();
    }

    /**
     * Stop the updates from request location
     */
    public void stopRequestLocation() {
        mRequestingLocationUpdates = false;

        if (mGoogleApiClient == null) {
            return;
        }

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    mGoogleApiClient.disconnect();
                }
            });
        }
    }
}
