# adal-network
Android library that offers an retrofit callback with centralized error handling

<div align="center">
  <img src="art/adal-network.gif" />
</div>

### Download

Gradle:

```gradle
dependencies {
  implementation 'com.massivedisaster.adal:adal-network:0.1.20'
}
```
### Usage
#### Java
```java
public class FragmentNetworkRequest extends AbstractRequestFragment {

    private RecyclerView mRclItems;
    private AdapterPost mAdapterPost;   

    @Override
    protected void doOnCreated() {
        mRclItems.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapterPost = new AdapterPost();
        mAdapterPost.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                request();
            }
        });
        mRclItems.setAdapter(mAdapterPost);

        request();
    }

    private void request() {

        // Show the general loading if the adapter is empty
        if (mAdapterPost.isEmpty()) {
            showLoading();
        }

        addRequest((APIRequests.getPosts(new APIRequestCallback<ResponseList<Post>>(getContext()) {
            @Override
            public void onSuccess(ResponseList<Post> posts) {
                mAdapterPost.addAll(posts);
            }

            @Override
            public void onError(APIError error, boolean isServerError) {
                showError(error.getMessage());
            }
        })));
    }
}
```

#### Kotlin
```kotlin
class FragmentNetworkRequest : AbstractRequestFragment() {

    private var mRclItems: RecyclerView? = null
    private var mAdapterPost: AdapterPost? = null

    override fun doOnCreated() {
        mRclItems!!.layoutManager = LinearLayoutManager(context)

        mAdapterPost = AdapterPost()
        mAdapterPost!!.setOnLoadMoreListener({ request() })
        mRclItems!!.adapter = mAdapterPost

        request()
    }

    private fun request() {

        // Show the general loading if the adapter is empty
        if (mAdapterPost!!.isEmpty) {
            showLoading()
        }

        addRequest(APIRequests.getPosts(object : APIRequestCallback<ResponseList<Post>>(context) {
            override fun onSuccess(posts: ResponseList<Post>) {
                mAdapterPost!!.addAll(posts)
            }

            override fun onError(error: APIError, isServerError: Boolean) {
                showError(error.message)
            }
        }))
    }
}
```

### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)