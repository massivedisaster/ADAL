package com.massivedisaster.adal.sample.feature.permissions;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.massivedisaster.adal.manager.PermissionsManager;
import com.massivedisaster.adal.sample.R;


public class FragmentPermissions extends Fragment {

    private PermissionsManager mPermissionsManager;

    private Button mBtnGetPermissions;
    private TextView mTxtInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permissions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mBtnGetPermissions = (Button) view.findViewById(R.id.btnGetPermissions);
        mTxtInfo = (TextView) view.findViewById(R.id.txtInfo);

        mPermissionsManager = PermissionsManager.getInstance(this);

        mBtnGetPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtInfo.setText("Asking permissions");
                requestPermissions();
            }
        });
    }

    private void requestPermissions() {
        mPermissionsManager.requestPermissions(new PermissionsManager.OnPermissionsListener() {
            @Override
            public void onGranted() {
                mTxtInfo.setText("onGranted");
            }

            @Override
            public void onDenied(boolean neverAskMeAgain) {
                mTxtInfo.setText("onDenied, neverAskMeAgain: " + neverAskMeAgain);

                if (neverAskMeAgain) {
                    mPermissionsManager.openAppSettings(getActivity());
                }
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CALENDAR);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsManager.onPermissionResult(requestCode);
    }
}
