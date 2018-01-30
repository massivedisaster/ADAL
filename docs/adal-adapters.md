# adal-adapters
Android library that helps and abstract the use of `RecyclerView.Adapter`.

<div align="center">
  <img src="art/adal-adapters.gif" />
</div>


### Download

Gradle:

```gradle
dependencies {
  implementation 'com.massivedisaster.adal:adal-adapters:0.1.17'
}
```
### Usage

Create a new adapter:
```java
public class AdapterPhoto extends AbstractLoadMoreBaseAdapter<Photo> {

    public AdapterPhoto() {
        super(R.layout.adapter_photo, R.layout.adapter_loading, new ArrayList<Photo>());
    }

    @Override
    protected void bindItem(BaseViewHolder holder, Photo item) {
        ImageView imgThumbnail = holder.getView(R.id.imgThumbnail);

        Glide.with(imgThumbnail.getContext()).load(item.getThumbnailUrl()).into(imgThumbnail);
        holder.setText(R.id.txtTitle, item.getTitle());
        holder.setText(R.id.txtDescription, item.getTitle());
    }

    @Override
    protected void bindError(BaseViewHolder holder, boolean loadingError) {
        LinearLayout lnlLoading = holder.getView(R.id.lnrLoading);
        LinearLayout lnlError = holder.getView(R.id.lnrError);

        if (loadingError) {
            lnlLoading.setVisibility(View.GONE);
            lnlError.setVisibility(View.VISIBLE);
        }
        else {
            lnlLoading.setVisibility(View.VISIBLE);
            lnlError.setVisibility(View.GONE);
        }
    }
}
```

Set the adapter:
```java
mAdapterPost = new AdapterPost();
mAdapterPost.setOnLoadMoreListener(new OnLoadMoreListener() {
    @Override
    public void onLoadMore() {
        request();
    }
});
```

### Contributing
[CONTRIBUTING](../CONTRIBUTING.md)

### License
[MIT LICENSE](../LICENSE.md)
