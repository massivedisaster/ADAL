# adal-dialogs
BaseDialog - To simplify an using of fragments.
AbstractRequestDialog - An dialog prepared to cancel retrofit requests.
BaseDialogFragment - To simplify an using of DialogFragment.
AbstractRequestDialogFragment - An DialogFragment prepared to cancel retrofit requests.

### Download

Gradle:

```gradle
dependencies {
  implementation 'com.massivedisaster.adal:adal-dialogs:0.1.18'
}
```
### Usage
#### Java
BaseDialog:
```java
public class DialogTest extends BaseDialog {

    DialogTest(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.dialog_test;
    }

    @Override
    protected void doOnCreated() {
        findViewById(R.id.btnDismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
```

BaseDialogFragment:
```java
public class DialogFragmentTest extends BaseDialogFragment {

    @Override
    protected int layoutToInflate() {
        return R.layout.dialog_test;
    }

    @Override
    protected void doOnCreated() {
        findViewById(R.id.btnDismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
```

#### Kotlin
BaseDialog:
```kotlin
class DialogTest(context: Context) : BaseDialog(context) {

    override fun layoutToInflate(): Int = R.layout.dialog_test

    override fun doOnCreated() {
        findViewById<View>(R.id.btnDismiss)!!.setOnClickListener({ dismiss() })
    }

}
```

BaseDialogFragment:
```kotlin
class DialogFragmentTest : BaseDialogFragment() {

    override fun layoutToInflate(): Int = R.layout.dialog_test

    override fun doOnCreated() {
        findViewById<View>(R.id.btnDismiss)!!.setOnClickListener { dismiss() }
    }
}
```

### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)
