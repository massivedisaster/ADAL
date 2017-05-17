/*
 * ADAL - A set of Android libraries to help speed up Android development.
 * Copyright (C) 2017 ADAL.
 *
 * ADAL is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or any later version.
 *
 * ADAL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with ADAL. If not, see <http://www.gnu.org/licenses/>.
 */

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
