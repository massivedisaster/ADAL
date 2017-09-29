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

import android.content.IntentSender;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.massivedisaster.location.listener.OnLocationManager;
import com.massivedisaster.location.utils.LocationError;

/**
 * LocationManager requests single or multiple device locations.
 */
public class LocationManager extends AbstractLocationManager {

    private static final long DEFAULT_TIMEOUT_LOCATION = 20000;
    private static final long REQUEST_LOCATION_INTERVAL = 1000;

    protected LocationRequest mLocationRequest;
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
            onLocationManager.onLocationError(LocationError.REQUEST_UPDATES_ENABLED);
            return;
        }

        setTimeout(timeOut);
        setLastKnowLocation(lastKnowLocation);
        mOnLocationManager = onLocationManager;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        buildRequest(mLocationRequest);
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
        locationRequest.setFastestInterval(interval);
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

        buildRequest(mLocationRequest);
    }

    /**
     * Start the request location updates
     */
    @Override
    protected void startRequestLocation() {
        if (mLocationSettingsRequest == null || mLocationRequest == null) {
            if (mOnLocationManager != null) {
                mOnLocationManager.onLocationError(LocationError.REQUEST_NEEDED);
            }
            return;
        }

        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        try {
                            if (mLocationRequest.getNumUpdates() != 1) {
                                mRequestingLocationUpdates = true;
                            }
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        } catch (SecurityException e) {
                            mRequestingLocationUpdates = false;
                            if (mOnLocationManager != null) {
                                mOnLocationManager.onLocationError(LocationError.DISABLED);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    if (getActivity() != null) {
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                    }
                                } catch (IntentSender.SendIntentException e1) {
                                    Log.e(getClass().getSimpleName(), e1.toString());
                                }
                                mRequestingLocationUpdates = false;
                                if (mOnLocationManager != null) {
                                    mOnLocationManager.onProviderDisabled();
                                }
                                break;
                            default:
                                mRequestingLocationUpdates = false;
                                if (mOnLocationManager != null) {
                                    mOnLocationManager.onLocationError(LocationError.DISABLED);
                                }
                                break;
                        }
                    }
                });

        showLastLocationAfterTimeout();
    }

    /**
     * Stop the updates from request location
     */
    @Override
    public void stopRequestLocation() {
        boolean isSuccessfull = mFusedLocationClient.removeLocationUpdates(mLocationCallback).isSuccessful();
        if (!isSuccessfull) {
            mRequestingLocationUpdates = false;
            if (mOnLocationManager != null) {
                mOnLocationManager.onStopRequestUpdate();
            }
        }
    }
}
