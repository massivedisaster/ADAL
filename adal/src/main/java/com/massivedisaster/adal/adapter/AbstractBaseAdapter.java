package com.massivedisaster.adal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class AbstractBaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

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


    private List<T> mLstItems;
    private int mResLayout, mResLoading;
    private boolean isLoading = false, isMoreDataAvailable = true, isLoadingError = false;

    public AbstractBaseAdapter(int resLayout, List<T> lstItems) {
        init(resLayout, -1, lstItems);
    }

    public AbstractBaseAdapter(int resLayout, int resLoading, List<T> lstItems) {
        init(resLayout, resLoading, lstItems);
    }

    private void init(int resLayout, int resLoading, List<T> lstItems) {
        mResLoading = resLoading;
        mResLayout = resLayout;
        mLstItems = lstItems;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == sViewTypeLoad && mResLoading != -1) {
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
                        mListener.onChildClick(v, mLstItems.get(position), position);
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
        } else if (getItemViewType(position) == sViewTypeLoad && mResLoading != -1) {
            bindError(holder, isLoadingError);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position > mLstItems.size() - 1 ? sViewTypeLoad : sViewTypeItem;
    }

    @Override
    public int getItemCount() {
        return (isLoading && mResLoading != -1) ? mLstItems.size() + 1 : mLstItems.size();
    }

    public T getItem(int position) {
        return mLstItems.get(position);
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

    public void addData(List<T> data) {

        if (data.isEmpty()) {
            isMoreDataAvailable = false;
        }

        mLstItems.addAll(data);
        notifyDataChanged();
    }

    public void clearData() {
        isMoreDataAvailable = true;
        isLoadingError = false;
        mLstItems.clear();
        notifyDataChanged();
    }

    private void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    public void setLoadingError(boolean isError) {
        isLoadingError = isError;
        notifyDataSetChanged();
    }
}