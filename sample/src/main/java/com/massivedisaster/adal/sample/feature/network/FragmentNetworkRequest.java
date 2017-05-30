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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.massivedisaster.adal.adapter.OnLoadMoreListener;
import com.massivedisaster.adal.fragment.AbstractRequestFragment;
import com.massivedisaster.adal.network.APIError;
import com.massivedisaster.adal.network.APIRequestCallback;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.model.Post;
import com.massivedisaster.adal.sample.network.APIRequests;
import com.massivedisaster.adal.sample.network.ResponseList;

public class FragmentNetworkRequest extends AbstractRequestFragment {

    private TextView mTxtMessage;
    private RecyclerView mRclItems;

    private AdapterPost mAdapterPost;

    @Override
    protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_network_request;
    }

    @Override
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Intended.
    }

    @Override
    protected void doOnCreated() {
        mTxtMessage = findViewById(R.id.txtMessage);
        mRclItems = findViewById(R.id.rclItems);

        mRclItems.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapterPost = new AdapterPost();
        mAdapterPost.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                request();
            }
        });
        mRclItems.setAdapter(mAdapterPost);

        request();
    }

    private void request() {

        // Show the general loading if the adapter is empty
        if (mAdapterPost.isEmpty()) {
            showLoading();
        }

        addRequest((APIRequests.getPosts(new APIRequestCallback<ResponseList<Post>>(getContext()) {
            @Override
            public void onSuccess(ResponseList<Post> posts) {
                showContent();
                mAdapterPost.addAll(posts);
            }

            @Override
            public void onError(APIError error, boolean isServerError) {
                showError(error.getMessage());
            }
        })));
    }

    private void showLoading() {
        mRclItems.setVisibility(View.GONE);
        mTxtMessage.setVisibility(View.VISIBLE);
        mTxtMessage.setText(R.string.loading);
    }

    private void showError(String error) {
        mRclItems.setVisibility(View.GONE);
        mTxtMessage.setVisibility(View.VISIBLE);
        mTxtMessage.setText(error);
    }

    private void showContent() {
        mTxtMessage.setVisibility(View.GONE);
        mRclItems.setVisibility(View.VISIBLE);
    }
}
