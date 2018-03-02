# adal-connectivity
Android library to verify if the device has an Internet connection and what type it is!

### Download

Gradle:

```gradle
dependencies {
  implementation 'com.massivedisaster.adal:adal-connectivity:0.1.18'
}
```
### Usage
#### Java
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

#### Kotlin
```kotlin
/**
 * Connectivity Change Fragment meant to test the {@link ConnectionChangeReceiver} for connectivity changes.
 */
class FragmentConnectivityAware : BaseFragment(), View.OnClickListener {

    private var mTxtMessage: TextView? = null
    private var mCheckConnectivityButton: Button? = null

    /**
     * Network verification.
     */
    private val mConnectionReceiver = object : ConnectionChangeReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ConnectionChangeReceiver.CONNECTIVITY_CHANGE_FILTER == intent.action) {
                checkConnectivity()
            }
        }
    }

    override fun getFromBundle(bundle: Bundle) {
        // Intended.
    }

    override fun layoutToInflate(): Int = R.layout.fragment_connectivity_aware

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        // Intended.
    }

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_network)
        mTxtMessage = findViewById(R.id.txtMessage)
        mCheckConnectivityButton = findViewById(R.id.btnCheckConnectivity)
    }

    /**
     * Called when FragmentConnectivityAware is about to become visible.
     */
    override fun onStart() {
        super.onStart()
        mConnectionReceiver.registerConnectionChangeReceiver(activity)
        mCheckConnectivityButton!!.setOnClickListener(this)
    }

    /**
     * Called when the FragmentConnectivityAware has become visible.
     */
    override fun onResume() {
        super.onResume()
        checkConnectivity()
    }

    /**
     * Called just before the FragmentConnectivityAware is destroyed.
     */
    override fun onDestroy() {
        mConnectionReceiver?.unregisterConnectionChangeReceiver()
        super.onDestroy()
    }

    /**
     * Called when a view has been clicked.
     * @param view the clicked [View].
     */
    override fun onClick(view: View) {
        if (R.id.btnCheckConnectivity == view.id) {
            checkConnectivity()
        }
    }

    /**
     * Check whether the device has Internet connectivity or not.
     */
    private fun checkConnectivity() {
        if (isVisible) {
            val isOnline = NetworkUtils.isNetworkConnected(activity)
            handleConnectivityStatusChange(isOnline)
        }
    }

    /**
     * Log the device Internet connectivity status.
     *
     * @param isOnline boolean value indicating whether the device has a connection established or not.
     */
    private fun handleConnectivityStatusChange(isOnline: Boolean) {
        val deviceConnectivity = if (isOnline)
            getString(R.string.connectivity_device_online)
        else
            getString(R.string.connectivity_device_offline)
        mTxtMessage!!.text = deviceConnectivity
        Toast.makeText(activity, deviceConnectivity, Toast.LENGTH_SHORT).show()
    }

}
```

### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)
