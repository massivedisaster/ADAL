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

import android.util.Log;

/**
 * Location utilities.
 */
public final class LocationUtils {

    /**
     * Private constructor to avoid user implement as a single instance instead of a Singleton.
     */
    private LocationUtils() {
    }

    /**
     * Verify if location are enable
     *
     * @param locationManager the location manager
     * @return True if location are enabled
     */
    public static boolean isLocationEnabled(android.location.LocationManager locationManager) {
        boolean gpsEnabled;
        boolean networkEnabled;

        try {
            gpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
            networkEnabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);

            return !(!gpsEnabled && !networkEnabled);

        } catch (IllegalArgumentException ex) {
            Log.d(LocationUtils.class.getCanonicalName(), ex.toString());
        }

        return true;
    }

}
