# adal-location
Android library that simplifies obtaining location.

<div align="center">
  <img src="art/adal-location.gif" />
</div>

### Download
Gradle:

```gradle
dependencies {
  compile 'com.massivedisaster.adal:adal-location:0.1.12'
}
```

### Usage
```java
public class FragmentLocation extends AbstractBaseFragment {

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

    private void getLocation() {
        mLocationManager.requestSingleLocation(android.location.LocationManager.NETWORK_PROVIDER, true, 10000, new OnLocationManager() {
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
        });
    }
}
```

### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[GNU LESSER GENERAL PUBLIC LICENSE](../LICENSE.md)