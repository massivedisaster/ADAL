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

package com.massivedisaster.adal.sample.feature.network;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.massivedisaster.adal.adapter.AbstractLoadMoreBaseAdapter;
import com.massivedisaster.adal.adapter.BaseViewHolder;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.model.Photo;

import java.util.ArrayList;

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