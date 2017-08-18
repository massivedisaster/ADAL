/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2017 ADAL
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.massivedisaster.adal.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Base class for an Adapter
 * <p>
 * <p>Adapters provide a binding from an app-specific data set to views that are displayed
 * within a {@link RecyclerView}.</p>
 *
 * @param <T> The type of the elements from the adapter.
 */
public abstract class AbstractLoadMoreBaseAdapter<T> extends AbstractBaseAdapter<T> {

    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_LOAD = 1;

    private static final int INVALID_RESOURCE_ID = -1;

    protected OnChildClickListener<T> mOnChildCLickListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int mResLayout, mResLoading;
    private View mEmptyView;
    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            setEmptyViewVisibility();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            setEmptyViewVisibility();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            setEmptyViewVisibility();
        }
    };
    private boolean mIsLoading, mIsMoreDataAvailable = true, mIsLoadingError;

    /**
     * The constructor of the adapter.
     *
     * @param resLayout The layout.
     * @param lstItems  The list of items.
     */
    public AbstractLoadMoreBaseAdapter(int resLayout, List<T> lstItems) {
        super();
        init(resLayout, INVALID_RESOURCE_ID, lstItems);
    }

    /**
     * The constructor of the adapter.
     *
     * @param resLayout  The layout resource id.
     * @param resLoading The loading resource id.
     * @param lstItems   The list of items.
     */
    public AbstractLoadMoreBaseAdapter(int resLayout, int resLoading, List<T> lstItems) {
        super();
        init(resLayout, resLoading, lstItems);
    }

    /**
     * Initialization fo the adapter.
     *
     * @param resLayout  The layout resource id.
     * @param resLoading The loading resource id.
     * @param data       The list of items.
     */
    private void init(int resLayout, int resLoading, List<T> data) {
        mResLoading = resLoading;
        mResLayout = resLayout;
        mData = data;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_LOAD && hasLoadingLayout()) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mResLoading, parent, false);

            return new BaseViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mResLayout, parent, false);

            final BaseViewHolder baseViewHolder = new BaseViewHolder(v);

            if (mOnChildCLickListener != null) {
                baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int position = baseViewHolder.getAdapterPosition();
                        mOnChildCLickListener.onChildClick(v, mData.get(position), position);
                    }
                });
            }
            return baseViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (position >= getItemCount() - 1 && mIsMoreDataAvailable && !mIsLoading && mOnLoadMoreListener != null) {
            mIsLoading = true;
            mOnLoadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            bindItem(holder, getItem(position));
        } else if (getItemViewType(position) == VIEW_TYPE_LOAD && hasLoadingLayout()) {
            bindError(holder, mIsLoadingError);
        }
    }

    @Override
    @ViewType
    public int getItemViewType(int position) {
        return position > mData.size() - 1 ? VIEW_TYPE_LOAD : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return (mIsMoreDataAvailable && hasLoadingLayout()) ? mData.size() + 1 : mData.size();
    }

    /**
     * Get the on load more listener.
     *
     * @return The on load more listener defined.
     */
    public OnLoadMoreListener getOnLoadMoreListener() {
        return mOnLoadMoreListener;
    }

    /**
     * Set on load more listener.
     *
     * @param onLoadMoreListener The listener called when there is more data to load.
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    /**
     * Set the on click listener on list elements.
     *
     * @param listener the listener called when an element is clicked.
     */
    public void setOnChildClickListener(OnChildClickListener<T> listener) {
        this.mOnChildCLickListener = listener;
    }

    /**
     * Set if is more data available.
     *
     * @param moreDataAvailable true if is more available, false otherwise
     */
    public void setIsMoreDataAvailable(boolean moreDataAvailable) {
        mIsMoreDataAvailable = moreDataAvailable;
    }

    /**
     * Set if appended an error during loading.
     *
     * @param isError true if there was an error, false otherwise.
     */
    public void setLoadingError(boolean isError) {
        mIsLoadingError = isError;
        notifyDataSetChanged();
    }

    /**
     * <p>Adds an entire data set</p>
     *
     * @param data Collection of data that will be added
     */
    public void addAll(Collection<T> data) {
        if (data.isEmpty()) {
            mIsMoreDataAvailable = false;
        }

        mData.addAll(data);
        notifyDataSetChanged();
        mIsLoading = false;
    }

    /**
     * <p>Clears data set and reset loading variables</p>
     */
    public void clear() {
        super.clear();
        mIsMoreDataAvailable = true;
        mIsLoadingError = false;
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
     * Set a empty to br showed when the adapter is empty
     *
     * @param emptyView the view to be showed when the adapter is empty
     */
    public void setEmptyView(@NonNull View emptyView) {
        if (mEmptyView == null) {
            registerAdapterDataObserver(mDataObserver);
        }

        mEmptyView = emptyView;

        setEmptyViewVisibility();
    }

    /**
     * Hide view if there is no items to show.
     */
    protected void setEmptyViewVisibility() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(getItemCount() > 0 ? GONE : VISIBLE);
        }
    }

    /**
     * Validate if loading layout is present.
     *
     * @return true if is present, false otherwise.
     */
    private boolean hasLoadingLayout() {
        return mResLoading != INVALID_RESOURCE_ID;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VIEW_TYPE_ITEM, VIEW_TYPE_LOAD})
    @interface ViewType {
    }
}
