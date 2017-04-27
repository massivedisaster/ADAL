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
    protected int layoutToInflate() {
        return R.layout.fragment_location;
    }

    @Override
    protected void doOnCreated() {
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
        });
    }
}
