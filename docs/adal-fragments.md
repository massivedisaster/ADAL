# adal-fragments
AbstractBaseFragment - To simplify an using of fragments.  
AbstractSplashFragment - An fragment with the splash screen logic implemented.

### Download

Gradle:

```gradle
dependencies {
  compile 'com.massivedisaster.adal:adal-fragments:0.1.12'
}
```
### Usage

AbstractBaseFragment:
```java
public class FragmentA extends AbstractBaseFragment {
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

### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)
