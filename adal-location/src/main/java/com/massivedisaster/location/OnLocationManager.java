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
