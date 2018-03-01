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

package com.massivedisaster.adal.samplekotlin.feature.connectivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.massivedisaster.adal.connectivity.ConnectionChangeReceiver
import com.massivedisaster.adal.connectivity.NetworkUtils
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.samplekotlin.R

/**
 * Connectivity Change Fragment meant to test the {@link ConnectionChangeReceiver} for connectivity changes.
 */
class FragmentConnectivityAware : BaseFragment(), View.OnClickListener {

    private var mTxtMessage: TextView? = null
    private var mCheckConnectivityButton: Button? = null

    /**
     * Network verification.
     */
    private val mConnectionReceiver = object : ConnectionChangeReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ConnectionChangeReceiver.CONNECTIVITY_CHANGE_FILTER == intent.action) {
                checkConnectivity()
            }
        }
    }

    override fun getFromBundle(bundle: Bundle) {
        // Intended.
    }

    override fun layoutToInflate(): Int {
        return R.layout.fragment_connectivity_aware
    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        // Intended.
    }

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_network)
        mTxtMessage = findViewById(R.id.txtMessage)
        mCheckConnectivityButton = findViewById(R.id.btnCheckConnectivity)
    }

    /**
     * Called when FragmentConnectivityAware is about to become visible.
     */
    override fun onStart() {
        super.onStart()
        mConnectionReceiver.registerConnectionChangeReceiver(activity)
        mCheckConnectivityButton!!.setOnClickListener(this)
    }

    /**
     * Called when the FragmentConnectivityAware has become visible.
     */
    override fun onResume() {
        super.onResume()
        checkConnectivity()
    }

    /**
     * Called just before the FragmentConnectivityAware is destroyed.
     */
    override fun onDestroy() {
        mConnectionReceiver?.unregisterConnectionChangeReceiver()
        super.onDestroy()
    }

    /**
     * Called when a view has been clicked.
     * @param view the clicked [View].
     */
    override fun onClick(view: View) {
        if (R.id.btnCheckConnectivity == view.id) {
            checkConnectivity()
        }
    }

    /**
     * Check whether the device has Internet connectivity or not.
     */
    private fun checkConnectivity() {
        if (isVisible) {
            val isOnline = NetworkUtils.isNetworkConnected(activity)
            handleConnectivityStatusChange(isOnline)
        }
    }

    /**
     * Log the device Internet connectivity status.
     *
     * @param isOnline boolean value indicating whether the device has a connection established or not.
     */
    private fun handleConnectivityStatusChange(isOnline: Boolean) {
        val deviceConnectivity = if (isOnline)
            getString(R.string.connectivity_device_online)
        else
            getString(R.string.connectivity_device_offline)
        mTxtMessage!!.text = deviceConnectivity
        Toast.makeText(activity, deviceConnectivity, Toast.LENGTH_SHORT).show()
    }

}