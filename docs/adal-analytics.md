# adal-analytics
Android library that simplifies using Google Analytics or Firebase Analytics.

### Download

Gradle:

```gradle
dependencies {
  compile 'com.massivedisaster.adal:adal-analytics:0.1.12'
}

// ADD THIS AT THE BOTTOM
apply plugin: 'com.google.gms.google-services'
```
### Usage with Google Analytics
```java
public class FragmentAnalytics extends BaseFragment {
    @Override
    protected void doOnCreated() {
      // Send a screen to GA
      AnalyticsManager.with(getContext()).sendScreen("FragmentAnalytics");

      // Send an event to GA
      AnalyticsManager.with(getContext()).sendEvent(
              "Category",
              "Action");
}
```
### Usage with Firebase Analytics
```java
public class FragmentFirebaseAnalytics extends BaseFragment {
    @Override
    protected void doOnCreated() {
      // Send a screen to FA
      FirebaseAnalyticsManager.sendScreen(getActivity(), "FragmentFirebaseAnalytics");

      // Send an event to FA
      Map<String, String> hashMap = new HashMap<>();
      hashMap.put("event_category", "Category");
      hashMap.put("event_action", "Action");
      hashMap.put("event_label", "Label");
      FirebaseAnalyticsManager.sendEvent(getActivity(), "EventName", hashMap);
      
      // Send a user property to FA
      FirebaseAnalyticsManager.sendUserProperty(getActivity(), "Property", "Value");
}
```
### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)
