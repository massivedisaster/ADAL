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

package com.massivedisaster.location.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.massivedisaster.location.LocationManager;
import com.massivedisaster.location.listener.OnLocationStatusProviderListener;
import com.massivedisaster.location.utils.LocationUtils;


/**
 * Broadcast Receiver to check GPS Status
 */
public class LocationStatusBroadcastReceiver extends BroadcastReceiver {

    private static final int PROVIDER_ENABLED = 1;
    private static final int PROVIDER_DISABLED = 0;
    private static final int PROVIDER_NONE = -1;

    protected int mStatus = PROVIDER_NONE;
    protected OnLocationStatusProviderListener mOnLocationStatusProviderListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            checkGPSStatus(context);
        }
    }

    /**
     * Listener to check provider status
     *
     * @param onLocationStatusProviderListener the listener
     */
    public void setOnLocationStatusProviderListener(OnLocationStatusProviderListener onLocationStatusProviderListener) {
        this.mOnLocationStatusProviderListener = onLocationStatusProviderListener;
    }

    /**
     * Check the GPS status
     *
     * @param context the context from BroadcasteReceiver
     */
    private void checkGPSStatus(Context context) {
        android.location.LocationManager locationManager =
                (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            boolean isLocationEnabled = LocationUtils.isLocationEnabled(locationManager);
            if (isLocationEnabled && mStatus != PROVIDER_ENABLED) {
                mStatus = PROVIDER_ENABLED;
                Log.w(LocationStatusBroadcastReceiver.class.getSimpleName(), "PROVIDER PROVIDER_ENABLED");
                if (mOnLocationStatusProviderListener != null) {
                    mOnLocationStatusProviderListener.onProviderEnabled();
                }
            } else if (!isLocationEnabled && mStatus != PROVIDER_DISABLED) {
                mStatus = PROVIDER_DISABLED;
                Log.w(LocationStatusBroadcastReceiver.class.getSimpleName(), "PROVIDER PROVIDER_DISABLED");
                if (mOnLocationStatusProviderListener != null) {
                    mOnLocationStatusProviderListener.onProviderDisabled();
                }
            }
        } else {
            Log.w(LocationManager.class.getSimpleName(), "PROVIDER ERROR");
        }
    }
}
