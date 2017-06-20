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
import com.massivedisaster.adal.sample.model.Photo;
import com.massivedisaster.adal.sample.network.APIRequests;
import com.massivedisaster.adal.sample.network.ResponseList;

public class FragmentNetworkRequest extends AbstractRequestFragment {

    private TextView mTxtMessage;
    private RecyclerView mRclItems;

    private AdapterPhoto mAdapterPhoto;

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
        getActivity().setTitle(R.string.sample_network);

        mTxtMessage = findViewById(R.id.txtMessage);
        mRclItems = findViewById(R.id.rclItems);

        mRclItems.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapterPhoto = new AdapterPhoto();
        mAdapterPhoto.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                request();
            }
        });
        mRclItems.setAdapter(mAdapterPhoto);

        request();
    }

    private void request() {

        // Show the general loading if the adapter is empty
        if (mAdapterPhoto.isEmpty()) {
            showLoading();
        }

        addRequest((APIRequests.getPhotos(new APIRequestCallback<ResponseList<Photo>>(getContext()) {
            @Override
            public void onSuccess(ResponseList<Photo> photos) {
                showContent();
                mAdapterPhoto.addAll(photos);
            }

            @Override
            public void onError(APIError error, boolean isServerError) {
                if (!mAdapterPhoto.isEmpty()) {
                    mAdapterPhoto.setLoadingError(true);
                }
                else {
                    showError(error.getMessage());
                }
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
