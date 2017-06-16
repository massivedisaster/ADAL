# adal-accounts
Android library that helps store and retrieve an Account from Android [AccountManager](https://developer.android.com/reference/android/accounts/AccountManager.html).

### Download
Gradle:

```gradle
dependencies {
  compile 'com.massivedisaster.adal:adal-accounts:0.1.12'
}
```

### Usage
```java
public class FragmentAccounts extends AbstractBaseFragment {

    @Override
    protected void doOnCreated() {
        AccountHelper.initialize(getActivity());

        getAccount();
        addAccount();
        clearAccount();        
    }

    private void clearAccount() {
        AccountHelper.clearAccounts(getContext(), new AccountHelper.OnAccountListener() {
          @Override
          public void onFinished() {
          }
        });
    }

    private void addAccount() {
        AccountHelper.addAccount(getContext(), "hardcoded_name", "hardcoded_password", "hardcoded_token");
    }

    private void getAccount() {
        if (AccountHelper.hasAccount(getContext())) {
            Account account = AccountHelper.getCurrentAccount(getContext());
        }
    }
}
```
### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[GNU LESSER GENERAL PUBLIC LICENSE](../LICENSE.md)