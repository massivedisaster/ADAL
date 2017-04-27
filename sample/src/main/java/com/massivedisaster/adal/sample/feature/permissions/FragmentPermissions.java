package com.massivedisaster.adal.sample.feature.permissions;

import android.Manifest;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.manager.PermissionsManager;
import com.massivedisaster.adal.sample.R;


public class FragmentPermissions extends AbstractBaseFragment {

    private PermissionsManager mPermissionsManager;

    private Button mBtnGetPermissions;
    private TextView mTxtInfo;

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_permissions;
    }

    @Override
    protected void doOnCreated() {
        mBtnGetPermissions = findViewById(R.id.btnGetPermissions);
        mTxtInfo = findViewById(R.id.txtInfo);

        initialize();
    }


    public void initialize() {
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
