/*
 * ADAL - A set of Android libraries to help speed up Android development.
 * Copyright (C) 2017 ADAL.
 *
 * ADAL is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or any later version.
 *
 * ADAL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with ADAL. If not, see <http://www.gnu.org/licenses/>.
 */

package com.massivedisaster.adal.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public abstract class LoadMoreBaseAdapter<T> extends BaseAdapter<T> {

    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_LOAD = 1;
    private static final int INVALID_RESOURCE_ID = -1;
    protected OnChildClickListener<T> mListener;
    private View mEmptyView;
    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };
    private OnLoadMoreListener mOnLoadMoreListener;
    private int mResLayout, mResLoading;
    private boolean mIsLoading, mIsMoreDataAvailable = true, mIsLoadingError;

    /**
     * The constructor of the adapter.
     *
     * @param resLayout The layout.
     * @param lstItems  The list of items.
     */
    public LoadMoreBaseAdapter(int resLayout, List<T> lstItems) {
        super();
        init(resLayout, INVALID_RESOURCE_ID, lstItems);
    }

    /**
     * The constructor of the adapter.
     *
     * @param resLayout  The layout.
     * @param resLoading The loading.
     * @param lstItems   The list of items.
     */
    public LoadMoreBaseAdapter(int resLayout, int resLoading, List<T> lstItems) {
        super();
        init(resLayout, resLoading, lstItems);
    }

    /**
     * Initialization fo the adapter.
     *
     * @param resLayout  The layout.
     * @param resLoading The loading.
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
     * Set the on click listener on elements.
     *
     * @param listener the listener called when an element is clicked.
     */
    public void setOnChildClickListener(OnChildClickListener<T> listener) {
        this.mListener = listener;
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

        checkIfEmpty();
    }

    /**
     * Hide view if there is no items to show.
     */
    protected void checkIfEmpty() {
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
}
