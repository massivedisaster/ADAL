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

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * This class is an Base ViewHolder for recycler views.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    /**
     * Constructor of view holder.
     *
     * @param itemView The layout view for adapter element.
     */
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Set text in a TextView.
     *
     * @param view The TextView.
     * @param res  The string from resources.
     * @return The TextView with the new text.
     */
    public TextView setText(@IdRes int view, @StringRes int res) {
        TextView t = getView(view);
        if (t != null) {
            t.setText(res);
        }
        return t;
    }

    /**
     * Set text in a TextView.
     *
     * @param view The TextView.
     * @param text The text to add to TextView.
     * @return The TextView with the new text.
     */
    public TextView setText(@IdRes int view, String text) {
        TextView t = getView(view);
        if (t != null) {
            t.setText(text);
        }
        return t;
    }

    /**
     * Look for a child view with the given id.  If this view has the given
     * id, return this view.
     *
     * @param viewId The id to search for.
     * @param <T>    The type of view.
     * @return The view that has the given id in the hierarchy or null
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        return (T) itemView.findViewById(viewId);
    }
}
