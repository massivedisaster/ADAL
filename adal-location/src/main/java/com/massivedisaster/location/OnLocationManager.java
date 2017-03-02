package com.massivedisaster.location;

import android.location.Location;

/**
 * ADAL by Carbon by BOLD
 * Created in 3/1/17 by the following authors:
 * Jorge Costa - {jorgecosta@carbonbybold.com}
 */
public interface OnLocationManager {

    /**
     * Called if location manager retrieve the user position
     *
     * @param location           The user location
     * @param isLastKnowLocation True if a location its given from the last know position
     */
    void onLocationFound(Location location, boolean isLastKnowLocation);

    /**
     * Called if the request gives an error
     *
     * @param locationError The location error
     */
    void onLocationError(LocationError locationError);

    /**
     * Called if user don't permissions to get location
     */
    void onPermissionsDenied();
}
