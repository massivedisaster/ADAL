/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2017 ADAL
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.massivedisaster.adal.sample.feature.permissions;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.permissions.PermissionsManager;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.utils.AppUtils;

public class FragmentPermissions extends AbstractBaseFragment {

    private PermissionsManager mPermissionsManager;

    private Button mBtnGetPermissions;
    private TextView mTxtInfo;

    @Override
    protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_permissions;
    }

    @Override
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void doOnCreated() {
        getActivity().setTitle(R.string.sample_permission);

        mBtnGetPermissions = findViewById(R.id.btnGetPermissions);
        mTxtInfo = findViewById(R.id.txtInfo);

        initialize();
    }

    public void initialize() {
        mPermissionsManager = new PermissionsManager(this);

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
                    AppUtils.openAppSettings(getActivity());
                }
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CALENDAR);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsManager.onPermissionResult(requestCode);
    }
}
