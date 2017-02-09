package com.massivedisaster.adal.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

public class PermissionsUtils {

    private static final int sRequestCode = 99;

    /**
     * Initialize PermissionsUtils inside a Activity
     *
     * @param activity
     * @return
     */
    public static PermissionsUtils getInstance(Activity activity) {
        return new PermissionsUtils(activity);
    }

    /**
     * Initialize PermissionsUtils inside a Fragment
     *
     * @param fragment
     * @return
     */
    public static PermissionsUtils getInstance(Fragment fragment) {
        return new PermissionsUtils(fragment);
    }

    private Activity mActivity;
    private Fragment mFragment;
    private String[] mPermissions;
    private int mRequestCode;
    private OnGrantedPermissions mOnGrantedPermissions;
    private OnDeniedPermissions mOnDeniedPermissions;

    private PermissionsUtils(Activity activity) {
        mActivity = activity;
    }

    private PermissionsUtils(Fragment fragment) {
        mFragment = fragment;
        mActivity = fragment.getActivity();
    }

    /**
     * @param onGrantedPermissions
     * @param onDeniedPermissions
     * @param permissions
     */
    public void requestPermissions(@NonNull OnGrantedPermissions onGrantedPermissions,
                                   @NonNull OnDeniedPermissions onDeniedPermissions,
                                   @NonNull String... permissions) {
        requestPermissions(sRequestCode, onGrantedPermissions, onDeniedPermissions, permissions);
    }

    /**
     * @param requestCode
     * @param onGrantedPermissions
     * @param onDeniedPermissions
     * @param permissions
     */
    public void requestPermissions(int requestCode,
                                   @NonNull OnGrantedPermissions onGrantedPermissions,
                                   @NonNull OnDeniedPermissions onDeniedPermissions,
                                   @NonNull String... permissions) {
        mPermissions = permissions;
        mRequestCode = requestCode;
        mOnGrantedPermissions = onGrantedPermissions;
        mOnDeniedPermissions = onDeniedPermissions;

        //Verify if permissions are added in manifest
        for (String s : permissions) {
            if (!hasPermissionInManifest((mFragment != null ? mFragment.getContext() : mActivity), s)) {
                Log.e(PermissionsUtils.class.getCanonicalName(), "Please add permission to manifest: " + s);
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || hasPermissions()) {
            if (mOnGrantedPermissions != null) {
                mOnGrantedPermissions.onGranted();
            }
            return;
        }

        if (mFragment != null) {
            logInfo("Fragment requestPermissions - " + mPermissions.toString());
            mFragment.requestPermissions(mPermissions, mRequestCode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            logInfo("Activity requestPermissions - " + mPermissions.toString());
            ActivityCompat.requestPermissions(mActivity, mPermissions, mRequestCode);
        }
    }

    /**
     * @param requestCode
     */
    public void onPermissionResult(int requestCode) {
        if (requestCode != mRequestCode) {
            logInfo("requestCode is different: " + requestCode + " != " + mRequestCode);
            return;
        }
        if (hasPermissions() && mOnGrantedPermissions != null) {
            logInfo("All permissions are Granted");
            mOnGrantedPermissions.onGranted();
        } else if (mOnDeniedPermissions != null) {
            logInfo("At least one permission was Denied");
            boolean showRationale = true;
            if (mFragment != null) {
                for (String permission : mPermissions) {
                    showRationale = showRationale && mFragment.shouldShowRequestPermissionRationale(permission);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (String permission : mPermissions) {
                    showRationale = showRationale && ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission);
                    logInfo("showRationale to " + permission + ": " + showRationale);
                }
            }
            mOnDeniedPermissions.onDenied(showRationale);
        }
    }

    /**
     * @param activity
     */
    public void openAppSettings(Activity activity) {
        openAppSettings(activity, mRequestCode);
    }

    /**
     * @param activity
     * @param requestCode
     */
    public void openAppSettings(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, requestCode);
    }

    private boolean hasPermissions() {
        if (mPermissions == null || mPermissions.length == 0) return false;
        boolean result = true;
        for (String permission : mPermissions) {
            result = result && ActivityCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
            logInfo("hasPermission to " + permission + ": " + result);
        }
        return result;
    }

    private boolean hasPermissionInManifest(Context context, String permission) {
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

    private void logInfo(String message) {
        Log.i(PermissionsUtils.class.getCanonicalName(), message);
    }

    public interface OnGrantedPermissions {
        void onGranted();
    }

    public interface OnDeniedPermissions {
        void onDenied(boolean showRationale);
    }
}