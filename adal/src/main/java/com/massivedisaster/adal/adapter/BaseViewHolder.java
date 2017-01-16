package com.massivedisaster.adal.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public TextView setText(@IdRes int view, @StringRes int res) {
        TextView t = getView(view);
        if (t != null) {
            t.setText(res);
        }
        return t;
    }

    public TextView setText(@IdRes int view, String res) {
        TextView t = getView(view);
        if (t != null) {
            t.setText(res);
        }
        return t;
    }

    public <T extends View> T getView(@IdRes int viewId) {
        return (T) itemView.findViewById(viewId);
    }
}