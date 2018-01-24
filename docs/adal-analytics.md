# adal-analytics
Android library that simplifies using Google Analytics or Firebase Analytics.

<div align="center">
  <img src="art/adal-google-analytics.gif" />
  <img src="art/adal-firebase-analytics.gif" />
</div>

### Download

Gradle:

```gradle
dependencies {
  compile 'com.massivedisaster.adal:adal-analytics:0.1.15'
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
      // Send Enhanced Ecommerce actions to GA
      // Send impression
      Product product = new Product()
          .setId("P12345")
          .setName("Android Warhol T-Shirt")
          .setCategory("Apparel/T-Shirts")
          .setBrand("Google")
          .setVariant("Black")
          .setPosition(1)
          .setCustomDimension(1, "Member");
      AnalyticsManager.with(getContext()).sendProduct(product, "Search Results", "searchResults");
      
      // Send product action (product can be null in this case)
      ProductAction productAction = new ProductAction(ProductAction.ACTION_CLICK)
          .setProductActionList("Search Results");
      AnalyticsManager.with(getContext()).sendProduct(productAction, product, null, "searchResults");
      
      // Send promotion
      Promotion promotion = new Promotion()
          .setId("PROMO_1234")
          .setName("Summer Sale")
          .setCreative("summer_banner2")
          .setPosition("banner_slot1");
      AnalyticsManager.with(getContext()).sendPromotion(promotion, "promotions");
      
      // Send promotion action
      AnalyticsManager.with(getContext()).sendPromotionAction(promotion, Promotion.ACTION_CLICK, "Internal Promotions", "click", "Summer Sale", "promotions");
   }
   ...
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
    ...
}
```
### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)
