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

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.massivedisaster.adal.fragment.BaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.location.LocationManager;
import com.massivedisaster.location.listener.OnLocationManager;
import com.massivedisaster.location.utils.LocationError;

public class FragmentLocation extends BaseFragment {

    private LocationManager mLocationManager;

    private Button mBtnGetLocation, mBtnGetLocationUpdates;
    private TextView mTxtInfo, mTxtInfoUpdates;
    private String mLocationUpdates = "";

    @Override
    protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    @Override
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Intended.
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_location;
    }

    @Override
    protected void doOnCreated() {
        getActivity().setTitle(R.string.sample_location);

        mBtnGetLocation = findViewById(R.id.btnGetLocation);
        mTxtInfo = findViewById(R.id.txtInfo);

        mBtnGetLocationUpdates = findViewById(R.id.btnGetLocationUpdates);
        mTxtInfoUpdates = findViewById(R.id.txtInfoUpdates);

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
        mBtnGetLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    mBtnGetLocationUpdates.setText(R.string.start_location_updates);
                    mBtnGetLocationUpdates.setSelected(false);
                    mLocationManager.stopRequestLocation();
                    mLocationUpdates = "";
                } else {
                    mTxtInfoUpdates.setText("Getting location...");
                    v.setEnabled(false);
                    getLocationUpdates();
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLocationManager.onActivityResult(requestCode, resultCode);
    }

    private void getLocation() {
        mLocationManager.requestSingleLocation(false, new OnLocationManager() {

            @Override
            public void onLocationFound(Location location, boolean isLastKnowLocation) {
                mTxtInfo.setText(getLocation(location));
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
                    case REQUEST_UPDATES_ENABLED:
                        mTxtInfo.setText("Error: Request Updates are enabled");
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
            public void onProviderEnabled() {
                // Intended.
            }

            @Override
            public void onProviderDisabled() {
                // Intended.
            }
        });
    }

    private void getLocationUpdates() {
        mLocationManager.requestLocationUpdates(new OnLocationManager() {
            @Override
            public void onLocationFound(Location location, boolean isLastKnowLocation) {

                if (!mBtnGetLocationUpdates.isSelected()) {
                    mBtnGetLocationUpdates.setText(R.string.stop_location_updates);
                    mBtnGetLocationUpdates.setSelected(true);
                }

                mLocationUpdates += getLocation(location) + "\n";

                mTxtInfoUpdates.setText(mLocationUpdates);
                mBtnGetLocationUpdates.setEnabled(true);
            }

            @Override
            public void onLocationError(LocationError locationError) {
                switch (locationError) {
                    case DISABLED:
                        mTxtInfoUpdates.setText("Error: Location disabled");
                        break;
                    case TIMEOUT:
                        mTxtInfoUpdates.setText("Error: Timeout getting location");
                        break;
                    default:
                        break;
                }

                mBtnGetLocationUpdates.setEnabled(true);
            }

            @Override
            public void onPermissionsDenied() {
                mTxtInfoUpdates.setText("Permissions Denied");
                mBtnGetLocationUpdates.setEnabled(true);
            }

            @Override
            public void onProviderEnabled() {
                // Intended.
            }

            @Override
            public void onProviderDisabled() {
                // Intended.
            }
        });
    }

    private String getLocation(Location location) {
        return "Location found " + location.getLatitude() + ", " + location.getLongitude();
    }
}
