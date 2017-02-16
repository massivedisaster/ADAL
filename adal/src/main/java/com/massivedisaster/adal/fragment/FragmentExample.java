package com.massivedisaster.adal.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.massivedisaster.adal.manager.PermissionsManager;

/**
 * ADAL by Carbon by BOLD
 * Created in 2/16/17 by the following authors:
 * Jorge Costa - {jorgecosta@carbonbybold.com}
 */
public class FragmentExample extends Fragment {

    private PermissionsManager mPermissionsManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPermissionsManager = PermissionsManager.getInstance(this);

        mPermissionsManager.requestPermissions(new PermissionsManager.OnPermissionsListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(boolean showRationale) {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CALENDAR);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionsManager.onPermissionResult(requestCode);
    }
}
