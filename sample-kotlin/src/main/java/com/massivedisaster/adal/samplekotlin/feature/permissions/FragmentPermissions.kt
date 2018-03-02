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

package com.massivedisaster.adal.samplekotlin.feature.permissions

import android.Manifest
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.permissions.PermissionsManager
import com.massivedisaster.adal.samplekotlin.R
import com.massivedisaster.adal.utils.AppUtils

class FragmentPermissions : BaseFragment() {

    private var mPermissionsManager: PermissionsManager? = null

    private var mBtnGetPermissions: Button? = null
    private var mTxtInfo: TextView? = null

    override fun getFromBundle(bundle: Bundle) {
        // Intended.
    }

    override fun layoutToInflate(): Int = R.layout.fragment_permissions

    override fun restoreInstanceState(savedInstanceState: Bundle?) {

    }

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_permission)

        mBtnGetPermissions = findViewById(R.id.btnGetPermissions)
        mTxtInfo = findViewById(R.id.txtInfo)

        initialize()
    }

    private fun initialize() {
        mPermissionsManager = PermissionsManager(this)

        mBtnGetPermissions!!.setOnClickListener {
            mTxtInfo!!.text = "Asking permissions"
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        mPermissionsManager!!.requestPermissions(object : PermissionsManager.OnPermissionsListener {
            override fun onGranted() {
                mTxtInfo!!.text = "onGranted"
            }

            override fun onDenied(neverAskMeAgain: Boolean) {
                mTxtInfo!!.text = "onDenied, neverAskMeAgain: " + neverAskMeAgain

                if (neverAskMeAgain) {
                    AppUtils.openAppSettings(activity)
                }
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CALENDAR)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mPermissionsManager!!.onPermissionResult(requestCode)
    }
}