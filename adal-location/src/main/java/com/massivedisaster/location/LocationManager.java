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
import com.massivedisaster.location.listener.OnLocationManager;
import com.massivedisaster.location.utils.LocationError;

/**
 * LocationManager requests single or multiple device locations.
 */
public class LocationManager extends AbstractLocationManager {

    private static final long DEFAULT_TIMEOUT_LOCATION = 20000;
    private static final long REQUEST_LOCATION_INTERVAL = 1000;
    private static final long REQUEST_LOCATION_FASTEST_INTERVAL = 1000;

    private LocationRequest mLocationRequest;


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

        mTimeout = timeOut;
        mLastKnowLocation = lastKnowLocation;
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
        mTimeout = DEFAULT_TIMEOUT_LOCATION;
        mLastKnowLocation = false;
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
                            status.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
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
