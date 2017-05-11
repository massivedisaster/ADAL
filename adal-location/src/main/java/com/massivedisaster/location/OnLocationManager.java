package com.massivedisaster.location;

import android.location.Location;

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
    public void onProviderEnabled(String provider) {
    }
}
