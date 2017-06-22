# adal-permissions
Android library that simplifies the request of permissions at runtime.

<div align="center">
  <img src="art/adal-permissions.gif" />
</div>


### Download
Gradle:

```gradle
dependencies {
  compile 'com.massivedisaster.adal:adal-permissions:0.1.12'
}
```

### Usage
```java
public class FragmentPermissions extends BaseFragment {

    private PermissionsManager mPermissionsManager;

    @Override
    protected void doOnCreated() {
      
        mPermissionsManager = new PermissionsManager(this);

        requestPermissions();
    }

    private void requestPermissions() {
        mPermissionsManager.requestPermissions(new PermissionsManager.OnPermissionsListener() {
            @Override
            public void onGranted() {
                // Granted.
            }

            @Override
            public void onDenied(boolean neverAskMeAgain) {
                // Denied.

                if (neverAskMeAgain) {
                    // User clicked nerver ask me again.
                }
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CALENDAR);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsManager.onPermissionResult(requestCode);
    }
}

```
### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)