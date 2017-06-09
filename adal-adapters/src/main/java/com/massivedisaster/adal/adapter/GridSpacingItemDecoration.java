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

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * An ItemDecoration allows the application to add a special drawing and layout offset
 * to specific item views from the adapter's data set. This can be useful for drawing dividers
 * between items, highlights, visual grouping boundaries and more.
 * <p>
 * In this case the GridSpacingItemDecoration calculates the space between elements in a grid list.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpanCount;
    private final int mSpacing;
    private final boolean mIncludeEdge;
    private final int mHeaderNum;

    /**
     * Constructor of item decoration.
     *
     * @param spanCount   The span count.
     * @param spacing     The size of the space.
     * @param includeEdge true if is to include edge, false otherwise.
     * @param headerNum   The number of headers
     */
    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge, int headerNum) {
        super();
        this.mSpanCount = spanCount;
        this.mSpacing = spacing;
        this.mIncludeEdge = includeEdge;
        this.mHeaderNum = headerNum;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view) - mHeaderNum; // item position

        if (position >= 0) {
            int column = position % mSpanCount; // item column

            if (mIncludeEdge) {
                outRect.left = mSpacing - column * mSpacing / mSpanCount; // mSpacing - column * ((1f / mSpanCount) * mSpacing)
                outRect.right = (column + 1) * mSpacing / mSpanCount; // (column + 1) * ((1f / mSpanCount) * mSpacing)

                if (position < mSpanCount) { // top edge
                    outRect.top = mSpacing;
                }
                outRect.bottom = mSpacing; // item bottom
            } else {
                outRect.left = column * mSpacing / mSpanCount; // column * ((1f / mSpanCount) * mSpacing)
                outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount; // mSpacing - (column + 1) * ((1f /    mSpanCount) * mSpacing)
                if (position >= mSpanCount) {
                    outRect.top = mSpacing; // item top
                }
            }
        } else {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }
}
