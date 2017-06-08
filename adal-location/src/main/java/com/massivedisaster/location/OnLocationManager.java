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

import android.location.Location;

/**
 * Manages location.
 */
public abstract class OnLocationManager {

    /**
     * Called if location manager retrieve the user position
     *
     * @param location           The user location
     * @param isLastKnowLocation True if a location its given from the last know position
     */
    public abstract void onLocationFound(Location location, boolean isLastKnowLocation);

    /**
     * Called if the request gives an error
     *
     * @param locationError The location error
     */
    public abstract void onLocationError(LocationError locationError);

    /**
     * Called if user don't permissions to get location
     */
    public abstract void onPermissionsDenied();

    /**
     * Called if provider change status
     *
     * @param provider the provider
     */
    public abstract void onProviderEnabled(String provider);

    /**
     * Called if provider change status
     *
     * @param provider the provider
     */
    public abstract void onProviderDisabled(String provider);
}
