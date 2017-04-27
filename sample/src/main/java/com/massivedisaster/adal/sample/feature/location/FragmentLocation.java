package com.massivedisaster.adal.sample.feature.location;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.massivedisaster.adal.sample.R;
import com.massivedisaster.location.LocationError;
import com.massivedisaster.location.LocationManager;
import com.massivedisaster.location.OnLocationManager;


public class FragmentLocation extends Fragment {

    private LocationManager mLocationManager;

    private Button mBtnGetLocation;
    private TextView mTxtInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mBtnGetLocation = (Button) view.findViewById(R.id.btnGetLocation);
        mTxtInfo = (TextView) view.findViewById(R.id.txtInfo);

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
