# adal-location
Android library that simplifies obtaining location.

<div align="center">
  <img src="art/adal-location.gif" />
</div>

### Download
Gradle:

```gradle
dependencies {
  implementation 'com.massivedisaster.adal:adal-location:0.1.20'
}
```

### Usage
#### Java
```java
public class FragmentLocation extends BaseFragment {

    private LocationManager mLocationManager;

    @Override
    protected void doOnCreated() {
        getLocation(); 
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
        mLocationManager.requestSingleLocation(true, new OnLocationManager() {
            @Override
            public void onLocationFound(Location location, boolean isLastKnowLocation) {
              // Use location here.
            }

            @Override
            public void onLocationError(LocationError locationError) {
                switch (locationError) {
                    case DISABLED:
                        // Location disabled.
                        break;
                    case TIMEOUT:
                        // Location timeout.
                        break;
                }
            }

            @Override
            public void onPermissionsDenied() {
                // Permissions denied.
            }

            @Override
            public void onProviderEnabled() {
                // Provider enabled.
            }

            @Override
            public void onProviderDisabled() {
                // Provider disabled.
            }
        });
    }

    private void getLocationUpdates() {
        mLocationManager.requestLocationUpdates(new OnLocationManager() {
            @Override
            public void onLocationFound(Location location, boolean isLastKnowLocation) {
                // Use location here.
            }

            @Override
            public void onLocationError(LocationError locationError) {
                switch (locationError) {
                    case DISABLED:
                        // Location disabled.
                        break;
                    case TIMEOUT:
                        // Location timeout.
                        break;
                }
            }

            @Override
            public void onPermissionsDenied() {
                // Permissions denied.
            }

            @Override
            public void onProviderEnabled() {
                // Provider enabled.
            }

            @Override
            public void onProviderDisabled() {
                // Provider disabled.
            }
        });
    }

    private void stopLocationUpdates() {
        mLocationManager.stopRequestLocation();
    }
}
```

#### Kotlin
```kotlin
class FragmentLocation : BaseFragment() {
    private var mLocationManager: LocationManager? = null
    
    override fun doOnCreated() {
        getLocation()
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocationManager = LocationManager()
        mLocationManager!!.onCreate(this)
    }
    
    override fun onDestroy() {
        mLocationManager!!.onDestroy()
        super.onDestroy()
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mLocationManager!!.onRequestPermissionsResult(requestCode, permissions, *grantResults)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mLocationManager!!.onActivityResult(requestCode, resultCode)
    }
    
    private fun getLocation() {
        mLocationManager!!.requestSingleLocation(true, object : OnLocationManager {
        
            override fun onLocationFound(location: Location, isLastKnowLocation: Boolean) {
                // Use location here.
            }
    
            override fun onLocationError(locationError: LocationError) {
                when (locationError) {
                    LocationError.DISABLED -> {}
                    LocationError.TIMEOUT -> {}
                    else -> {}
                }
            }
    
            override fun onPermissionsDenied() {
                // Permissions denied.
            }
    
            override fun onProviderEnabled() {
                // Provider enabled.
            }
    
            override fun onProviderDisabled() {
                // Provider disabled.
            }
            
            override fun onStopRequestUpdate() {
            
            }
        })
    }
    
    private fun getLocationUpdates() {
        mLocationManager!!.requestLocationUpdates(object : OnLocationManager {
        
            override fun onLocationFound(location: Location, isLastKnowLocation: Boolean) {
                // Use location here.
            }
    
            override fun onLocationError(locationError: LocationError) {
                when (locationError) {
                    LocationError.DISABLED -> {}
                    LocationError.TIMEOUT -> {}
                    else -> {}
                }
            }
    
            override fun onPermissionsDenied() {
                // Permissions denied.
            }
    
            override fun onProviderEnabled() {
                // Provider enabled.
            }
    
            override fun onProviderDisabled() {
                // Provider disabled.
            }
            
            override fun onStopRequestUpdate() {
            
            }
        })
    }
    
    private fun stopLocationUpdates() {
        mLocationManager!!.stopRequestLocation();
    }
}
```

### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)
