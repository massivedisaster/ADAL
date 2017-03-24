package com.massivedisaster.adal.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class AbstractBaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private View mEmptyView;

    private static final int sInvalidResourceId = -1;
    private static final int sViewTypeItem = 0;
    private static final int sViewTypeLoad = 1;

    private OnChildClickListener<T> mListener;
    private OnLoadMoreListener mOnLoadMoreListener;

    public interface OnChildClickListener<T> {
        void onChildClick(android.view.View view, T t, int position);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private List<T> mData;
    private int mResLayout, mResLoading;
    private boolean isLoading = false, isMoreDataAvailable = true, isLoadingError = false;

    RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }
    };

    public AbstractBaseAdapter(int resLayout, List<T> lstItems) {
        init(resLayout, sInvalidResourceId, lstItems);
    }

    public AbstractBaseAdapter(int resLayout, int resLoading, List<T> lstItems) {
        init(resLayout, resLoading, lstItems);
    }

    private void init(int resLayout, int resLoading, List<T> data) {
        mResLoading = resLoading;
        mResLayout = resLayout;
        mData = data;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == sViewTypeLoad && mResLoading != sInvalidResourceId) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mResLoading, parent, false);

            return new BaseViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mResLayout, parent, false);

            final BaseViewHolder baseViewHolder = new BaseViewHolder(v);

            if (mListener != null) {
                baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int position = baseViewHolder.getAdapterPosition();
                        mListener.onChildClick(v, mData.get(position), position);
                    }
                });
            }
            return baseViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && mOnLoadMoreListener != null) {
            isLoading = true;
            mOnLoadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == sViewTypeItem) {
            bindItem(holder, getItem(position));
        } else if (getItemViewType(position) == sViewTypeLoad && mResLoading != sInvalidResourceId) {
            bindError(holder, isLoadingError);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position > mData.size() - 1 ? sViewTypeLoad : sViewTypeItem;
    }

    @Override
    public int getItemCount() {
        return (isLoading && mResLoading != sInvalidResourceId) ? mData.size() + 1 : mData.size();
    }

    protected abstract void bindItem(BaseViewHolder holder, T item);

    protected abstract void bindError(BaseViewHolder holder, boolean loadingError);

    public OnLoadMoreListener getOnLoadMoreListener() {
        return mOnLoadMoreListener;
    }

    public void setOnChildClickListener(OnChildClickListener<T> listener) {
        this.mListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setIsMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
        isLoading = moreDataAvailable;
    }

    public void setLoadingError(boolean isError) {
        isLoadingError = isError;
        notifyDataSetChanged();
    }

    /**
     * <p>Adds an entire data set</p>
     *
     * @param data: Collection of data that will be added
     */
    public void addAll(Collection<T> data) {
        if (data.isEmpty()) {
            isMoreDataAvailable = false;
        }

        mData.addAll(data);
        notifyDataSetChanged();
        isLoading = false;
    }

    /**
     * <p>Adds an item on a specific position of the data set</p>
     *
     * @param position: specific position to add the item
     * @param item:     Item to be added
     */
    public void add(int position, T item) {
        validatePosition(position);
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * <p>Adds an item into data set</p>
     *
     * @param item: Item to be added
     */
    public void add(T item) {
        mData.add(item);
        notifyItemInserted(getItemPosition(item));
    }

    /**
     * <p>Returns the item on the given position</p>
     *
     * @param position: Position of the retrieved item
     * @return item: The item at the specific position
     */
    public T getItem(int position) {
        validatePosition(position);
        return mData.get(position);
    }

    /**
     * <p>Removes the item at the given position</p>
     *
     * @param position: Position of the item that will be removed
     */
    public void remove(int position) {
        validatePosition(position);
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * <p>Removes the given item</p>
     *
     * @param item: The item that will be removed
     */
    public void remove(T item) {
        mData.remove(item);
        notifyItemRemoved(getItemPosition(item));
    }

    /**
     * <p>Remove a collection of items</p>
     *
     * @param ts: Collection of items that will be removed
     */
    public void removeAll(Collection<T> ts) {
        mData.removeAll(ts);
        notifyDataSetChanged();
    }

    /**
     * <p>Returns the given item position</p>
     *
     * @param item: Item to retrieve the position
     * @return position: Position of the specified item
     */
    public int getItemPosition(T item) {
        return mData.indexOf(item);
    }

    /**
     * <p>Returns adapter data set (All items)</p>
     *
     * @return data: All items stored on this list
     */
    public List<T> getDataSet() {
        return mData;
    }

    /**
     * <p>Clears data set and reset loading variables</p>
     */
    public void clear() {
        isMoreDataAvailable = true;
        isLoadingError = false;
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * <p>Check if the adapter is empty</p>
     *
     * @return true if the data is empty, otherwise false
     */
    public boolean isEmpty() {
        return mData.isEmpty();
    }

    /**
     * <p>Validates an specific position according to the data set size</p>
     *
     * @param position: Specific position to be validated
     */
    private void validatePosition(int position) {
        if (mData.isEmpty()) {
            throw new IndexOutOfBoundsException("The adapter is empty!");
        }

        if (position < 0 || position >= mData.size()) {
            throw new IndexOutOfBoundsException("Please, specify a valid position that is equals or greater than 0 and less than " + mData.size());
        }
    }

    /**
     * Set a empty to br showed when the adapter is empty
     *
     * @param emptyView the view to be showed when the adapter is empty
     */
    public void setEmptyView(@Nullable View emptyView) {
        if (emptyView == null) {
            throw new NullPointerException("EmptyView cannot be null");
        }

        if (mEmptyView == null) {
            registerAdapterDataObserver(mDataObserver);
        }

        this.mEmptyView = emptyView;

        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(getItemCount() > 0 ? GONE : VISIBLE);
        }
    }
}