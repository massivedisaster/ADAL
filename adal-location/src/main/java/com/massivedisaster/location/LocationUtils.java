package com.massivedisaster.location;

import android.util.Log;

public class LocationUtils {

    /**
     * Verify if location are enable
     *
     * @param locationManager the location manager
     * @return True if location are enabled
     */
    public static boolean isLocationEnabled(android.location.LocationManager locationManager) {
        boolean gps_enabled;
        boolean network_enabled;

        try {
            gps_enabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);

            return !(!gps_enabled && !network_enabled);

        } catch (Exception ex) {
            Log.d(LocationUtils.class.getCanonicalName(), ex.toString());
        }

        return true;
    }

}
