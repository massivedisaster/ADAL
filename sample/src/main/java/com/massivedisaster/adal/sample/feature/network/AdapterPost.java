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

package com.massivedisaster.adal.sample.feature.network;

import com.massivedisaster.adal.adapter.LoadMoreBaseAdapter;
import com.massivedisaster.adal.adapter.BaseViewHolder;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.model.Post;

import java.util.ArrayList;

public class AdapterPost extends LoadMoreBaseAdapter<Post> {

    public AdapterPost() {
        super(R.layout.adapter_post, R.layout.adapter_loading, new ArrayList<Post>());
    }

    @Override
    protected void bindItem(BaseViewHolder holder, Post item) {
        holder.setText(R.id.txtTitle, item.getTitle());
        holder.setText(R.id.txtBody, item.getBody());
    }

    @Override
    protected void bindError(BaseViewHolder holder, boolean loadingError) {

    }
}
