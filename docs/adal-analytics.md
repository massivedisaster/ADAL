# adal-analytics
Android library that simplifies using Google Analytics.

### Download

Gradle:

```gradle
dependencies {
  compile 'com.massivedisaster.adal:adal-analytics:0.1.12'
}
```
### Usage
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
### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)