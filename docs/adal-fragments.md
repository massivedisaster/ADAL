# adal-fragments
BaseFragment - To simplify an using of fragments.  
AbstractSplashFragment - An fragment with the splash screen logic implemented.  
AbstractRequestFragment - An fragment prepared to cancel retrofit requests.

### Download

Gradle:

```gradle
dependencies {
  implementation 'com.massivedisaster.adal:adal-fragments:0.1.18'
}
```
### Usage
#### Java
BaseFragment:
```java
public class FragmentA extends BaseFragment {
    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_a;
    }

    @Override
    protected void doOnCreated() {
        getActivity().setTitle(R.string.sample_bangbus);
    }
}
```

AbstractSplashFragment:
```java
public class FragmentSplash extends AbstractSplashFragment {

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_splash_screen;
    }

    @Override
    protected void onSplashStarted() {
        onSplashFinish(openHome());
    }

    private OnFinishSplashScreen openHome() {
        return new OnFinishSplashScreen() {
            @Override
            public void onFinish() {
                ActivityFragmentManager.open(getActivity(), ActivityToolbar.class, FragmentHome.class);
                getActivity().finish();
            }
        };
    }
}
```

AbstractRequestFragment:
```java
public class FragmentRequest extends AbstractRequestFragment {

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_request;
    }

    @Override
    protected void doOnCreated() {
        addRequest(...);
    }
}
```

#### Kotlin
BaseFragment:
```kotlin
class FragmentA : BaseFragment() {
    override fun layoutToInflate(): Int = R.layout.fragment_a

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_bangbus)
    }
}
```

AbstractSplashFragment:
```kotlin
class FragmentSplash : AbstractSplashFragment() {

    override fun layoutToInflate(): Int = R.layout.fragment_splash_screen

    override fun onSplashStarted() {
        onSplashFinish(openHome())
    }

    private fun openHome(): AbstractSplashFragment.OnFinishSplashScreen = OnFinishSplashScreen {
                ActivityCall.init(activity!!, ActivityToolbar::class, FragmentHome::class)
                        .build()
                activity!!.finish()
            }
}
```

AbstractRequestFragment:
```kotlin
class FragmentRequest : AbstractRequestFragment() {

    override fun layoutToInflate(): Int = R.layout.fragment_network_request

    override fun doOnCreated() {
        addRequest(...)
    }
}
```

### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)
