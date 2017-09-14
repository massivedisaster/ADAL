# adal-connectivity
Android library to verify if the device has an Internet connection and what type it is!

### Download

Gradle:

```gradle
dependencies {
  compile 'com.massivedisaster.adal:adal-connectivity:0.1.15'
}
```
### Usage

```java
/**
 * Connectivity Change Fragment meant to test the {@link ConnectionChangeReceiver} for connectivity changes.
 */
public class FragmentConnectivityAware extends BaseFragment implements View.OnClickListener {

    private TextView mTxtMessage;
    private Button mCheckConnectivityButton;

    /**
     * Network verification.
     */
    private ConnectionChangeReceiver mConnectionReceiver = new ConnectionChangeReceiver() {
        @Override public void onReceive(@NonNull final Context context, @NonNull final Intent intent) {
            if (ConnectionChangeReceiver.CONNECTIVITY_CHANGE_FILTER.equals(intent.getAction())) {
                checkConnectivity();
            }
        }
    };

    @Override protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    @Override protected int layoutToInflate() {
        return R.layout.fragment_connectivity_aware;
    }

    @Override protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Intended.
    }

    @Override protected void doOnCreated() {
        getActivity().setTitle(R.string.sample_network);
        mTxtMessage = findViewById(R.id.txtMessage);
        mCheckConnectivityButton = findViewById(R.id.btnCheckConnectivity);
    }

    /**
     * Called when FragmentConnectivityAware is about to become visible.
     */
    @Override public void onStart() {
        super.onStart();
        mConnectionReceiver.registerConnectionChangeReceiver(getActivity());
        mCheckConnectivityButton.setOnClickListener(this);
    }

    /**
     * Called when the FragmentConnectivityAware has become visible.
     */
    @Override public void onResume() {
        super.onResume();
        checkConnectivity();
    }

    /**
     * Called just before the FragmentConnectivityAware is destroyed.
     */
    @Override public void onDestroy() {
        if (mConnectionReceiver != null) {
            mConnectionReceiver.unregisterConnectionChangeReceiver();
        }
        super.onDestroy();
    }

    /**
     * Called when a view has been clicked.
     * @param view the clicked {@link View}.
     */
    @Override public void onClick(View view) {
        if (R.id.btnCheckConnectivity == view.getId()) {
            checkConnectivity();
        }
    }

    /**
     * Check whether the device has Internet connectivity or not.
     */
    private void checkConnectivity() {
        if (isVisible()) {
            final boolean isOnline = NetworkUtils.isNetworkConnected(getActivity());
            handleConnectivityStatusChange(isOnline);
        }
    }

    /**
     * Log the device Internet connectivity status.
     *
     * @param isOnline boolean value indicating whether the device has a connection established or not.
     */
    private void handleConnectivityStatusChange(final boolean isOnline) {
        final String deviceConnectivity = isOnline ? getString(R.string.connectivity_device_online)
                : getString(R.string.connectivity_device_offline);
        mTxtMessage.setText(deviceConnectivity);
        Toast.makeText(getActivity(), deviceConnectivity, Toast.LENGTH_SHORT).show();
    }

}

```

### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)
