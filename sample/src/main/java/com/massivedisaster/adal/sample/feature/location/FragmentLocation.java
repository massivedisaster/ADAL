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

package com.massivedisaster.adal.sample.feature.location;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.location.LocationError;
import com.massivedisaster.location.LocationManager;
import com.massivedisaster.location.OnLocationManager;

public class FragmentLocation extends AbstractBaseFragment {

    private LocationManager mLocationManager;

    private Button mBtnGetLocation;
    private TextView mTxtInfo;

    @Override
    protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_location;
    }

    @Override
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Intended.
    }

    @Override
    protected void doOnCreated() {
        getActivity().setTitle(R.string.sample_location);

        mBtnGetLocation = findViewById(R.id.btnGetLocation);
        mTxtInfo = findViewById(R.id.txtInfo);

        initialize();
    }

    public void initialize() {
        mBtnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtInfo.setText("Getting location...");
                v.setEnabled(false);
                getLocation();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = new LocationManager();
        mLocationManager.onCreate(this);
    }

    @Override
    public void onDestroy() {
        mLocationManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getLocation() {
        mLocationManager.requestSingleLocation(android.location.LocationManager.NETWORK_PROVIDER, true, 10000, new OnLocationManager() {
            @Override
            public void onLocationFound(Location location, boolean isLastKnowLocation) {
                mTxtInfo.setText("Location found: " + location.toString());
                mBtnGetLocation.setEnabled(true);
            }

            @Override
            public void onLocationError(LocationError locationError) {
                switch (locationError) {
                    case DISABLED:
                        mTxtInfo.setText("Error: Location disabled");
                        break;
                    case TIMEOUT:
                        mTxtInfo.setText("Error: Timeout getting location");
                        break;
                    default:
                        break;
                }

                mBtnGetLocation.setEnabled(true);
            }

            @Override
            public void onPermissionsDenied() {
                mTxtInfo.setText("Permissions Denied");
                mBtnGetLocation.setEnabled(true);
            }

            @Override
            public void onProviderEnabled(String provider) {
                // Intended.
            }

            @Override
            public void onProviderDisabled(String provider) {
                // Intended.
            }
        });
    }
}
