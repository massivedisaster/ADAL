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

package com.massivedisaster.adal.sample.feature.location;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.location.FusedLocationManager;
import com.massivedisaster.location.utils.LocationError;
import com.massivedisaster.location.listener.OnFusedLocationManager;

public class FragmentFusedLocation extends AbstractBaseFragment {

    private FusedLocationManager mFusedLocationManager;

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
        return R.layout.fragment_fused_location;
    }

    @Override
    protected void doOnCreated() {
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
                    mFusedLocationManager.stopLocationUpdates();
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
        mFusedLocationManager = new FusedLocationManager();
        mFusedLocationManager.onCreate(this);
    }

    @Override
    public void onDestroy() {
        mFusedLocationManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mFusedLocationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFusedLocationManager.onActivityResult(requestCode, resultCode);
    }

    private void getLocation() {
        mFusedLocationManager.requestSingleLocation(false, new OnFusedLocationManager() {

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
                    case UPDATES_ENABLED:
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
        mFusedLocationManager.requestLocationUpdates(new OnFusedLocationManager() {
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
