package com.massivedisaster.adal.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermissionUtils {

    public static boolean hasPermissions(Context context, String... lstPermissions) {
        if (lstPermissions == null || lstPermissions.length == 0) {
            return false;
        }

        boolean result = true;
        for (String permission : lstPermissions) {
            result = result && ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return result;
    }

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
