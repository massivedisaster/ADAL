/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2018 ADAL
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

package com.massivedisaster.adal.samplekotlin.feature.network

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.massivedisaster.adal.fragment.AbstractRequestFragment
import com.massivedisaster.adal.network.APIError
import com.massivedisaster.adal.network.APIRequestCallback
import com.massivedisaster.adal.samplekotlin.R
import com.massivedisaster.adal.samplekotlin.model.Photo
import com.massivedisaster.adal.samplekotlin.network.APIRequests
import com.massivedisaster.adal.samplekotlin.network.ResponseList

class FragmentNetworkRequest : AbstractRequestFragment() {

    private var mTxtMessage: TextView? = null
    private var mBtnTryAgain: Button? = null
    private var mRclItems: RecyclerView? = null

    private var mAdapterPhoto: AdapterPhoto? = null

    override fun getFromBundle(bundle: Bundle) {
        // Intended.
    }

    override fun layoutToInflate(): Int = R.layout.fragment_network_request

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        // Intended.
    }

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_network)

        mTxtMessage = findViewById(R.id.txtMessage)
        mBtnTryAgain = findViewById(R.id.btnTryAgain)
        mRclItems = findViewById(R.id.rclItems)

        mBtnTryAgain!!.setOnClickListener { onTryAgain() }

        mRclItems!!.layoutManager = LinearLayoutManager(context)

        mAdapterPhoto = AdapterPhoto()
        mAdapterPhoto!!.setOnLoadMoreListener({ request() })
        mAdapterPhoto!!.setOnErrorClickListener(View.OnClickListener { onTryAgain() })
        mRclItems!!.adapter = mAdapterPhoto

        request()
    }

    private fun onTryAgain() {
        mAdapterPhoto!!.setLoadingError(false)
        request()
    }

    private fun request() {

        // Show the general loading if the adapter is empty
        if (mAdapterPhoto!!.isEmpty) {
            showLoading()
        }

        addRequest(APIRequests.getPhotos(object : APIRequestCallback<ResponseList<Photo>>(context) {
            override fun onSuccess(photos: ResponseList<Photo>) {
                showContent()
                mAdapterPhoto!!.addAll(photos)
            }

            override fun onError(error: APIError, isServerError: Boolean) {
                if (!mAdapterPhoto!!.isEmpty) {
                    mAdapterPhoto!!.setLoadingError(true)
                } else {
                    showError(error.message)
                }
            }
        }))
    }

    private fun showLoading() {
        mRclItems!!.visibility = View.GONE
        mBtnTryAgain!!.visibility = View.GONE
        mTxtMessage!!.visibility = View.VISIBLE
        mTxtMessage!!.setText(R.string.loading)
    }

    private fun showError(error: String) {
        mRclItems!!.visibility = View.GONE
        mBtnTryAgain!!.visibility = View.VISIBLE
        mTxtMessage!!.visibility = View.VISIBLE
        mTxtMessage!!.text = error
    }

    private fun showContent() {
        mRclItems!!.visibility = View.VISIBLE
        mBtnTryAgain!!.visibility = View.GONE
        mTxtMessage!!.visibility = View.GONE

    }
}