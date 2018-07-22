# adal-bus
Android library that uses `LocalBroadcastManager` to simplify communication between `Activities`, `Fragments`, `Threads`, `Services`, etc.

<div align="center">
  <img src="art/adal-bus.gif" />
</div>

### Download
Gradle:

```gradle
dependencies {
  implementation 'com.massivedisaster.adal:adal-bus:0.1.20'
}
```

### Usage
#### Java
* Subscriber
```java
public static final String BANG_A = "BANG_A";
private BangBus mBangBus;

// Initialize
mBangBus = new BangBus(getActivity());

// Subscribe with Action
@BangBus.SubscribeBang(action = BANG_A)
public void bangWithAction(String message) {
   mTxtResult.setText(message);
}

// Subscribe Without Action
@BangBus.SubscribeBang
public void bangWithoutAction(Integer number) {
    mTxtResult.setText(String.valueOf(number));
}

// Unsubscribe
@Override
public void onDestroy() {
    mBangBus.unsubscribe();
    super.onDestroy();
}
```

* Sender
```java
public static final String BANG_MESSAGE_WITH_ACTION = "received bang with action";
public static final int BANG_NUMBER_WITHOUT_NUMBER = 666;

// Send with Action
BangBus.with(getContext())
       .addAction(FragmentA.BANG_A)
       .setParameter(BANG_MESSAGE_WITH_ACTION)
       .bang();

// Send Bang Without Action
BangBus.with(getContext())
       .setParameter(BANG_NUMBER_WITHOUT_NUMBER)
       .bang();
```

#### Kotlin
* Subscriber
```kotlin
companion object {
    val BANG_A: String = "BANG_A"
}
private var mBangBus: BangBus? = null

// Initialize
mBangBus = BangBus(activity)

// Subscribe with Action
@BangBus.SubscribeBang(action = "BANG_A")
fun bangWithAction(message: String) {
    mTxtResult!!.text = message
}

// Subscribe Without Action
@BangBus.SubscribeBang
fun bangWithoutAction(number: Int?) {
    mTxtResult!!.text = number.toString()
}

// Unsubscribe
@Override
override fun onDestroy() {
    mBangBus!!.unsubscribe()
    super.onDestroy()
}
```

* Sender
```kotlin
val BANG_MESSAGE_WITH_ACTION = "received bang with action"
val BANG_NUMBER_WITHOUT_NUMBER = 666

// Send with Action
BangBus.with(context)
       .addAction(FragmentA.BANG_A)
       .setParameter(BANG_MESSAGE_WITH_ACTION)
       .bang()

// Send Bang Without Action
BangBus.with(context)
       .setParameter(BANG_NUMBER_WITHOUT_NUMBER)
       .bang()
```

### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)