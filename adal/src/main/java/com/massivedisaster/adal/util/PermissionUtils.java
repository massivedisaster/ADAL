package com.massivedisaster.adal.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermissionUtils {

    /**
     * Verify if all permissions given are granted
     *
     * @param context     the application context
     * @param permissions one or more permission to check
     * @return true if all permissions are granted
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }

        for (String permission : permissions) {
            if (!(ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verify if the permission given are added on manifest
     *
     * @param context    the application context
     * @param permission the permission to check
     * @return true if permission are added on manifest
     */
    public static boolean hasPermissionInManifest(Context context, String permission) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (p.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
