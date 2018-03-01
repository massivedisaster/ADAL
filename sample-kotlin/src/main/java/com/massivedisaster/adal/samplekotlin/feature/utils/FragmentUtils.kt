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

package com.massivedisaster.adal.samplekotlin.feature.utils

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.samplekotlin.R
import com.massivedisaster.adal.utils.AppUtils
import com.massivedisaster.adal.utils.SnackBuilder

class FragmentUtils : BaseFragment() {

    private var mBtnCheckPlayServicesExists: Button? = null

    override fun getFromBundle(bundle: Bundle) {}

    override fun layoutToInflate(): Int {
        return R.layout.fragment_utils
    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {}

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_utils)

        mBtnCheckPlayServicesExists = findViewById(R.id.btnCheckPlayservicesExists)

        findViewById<View>(R.id.btnOpenSettings)!!.setOnClickListener { AppUtils.openAppSettings(activity) }

        findViewById<View>(R.id.btnCheckPlayservicesExists)!!.setOnClickListener { SnackBuilder.show(mBtnCheckPlayServicesExists, if (AppUtils.checkPlayServicesExists(activity)) R.string.playservices_exists else R.string.playservices_dont_exists, R.color.colorAccent) }

        findViewById<View>(R.id.btnOpenDial)!!.setOnClickListener { AppUtils.openDial(activity, "00351910000000") }

        findViewById<View>(R.id.btnOpenEmail)!!.setOnClickListener { AppUtils.openEmail(activity, getString(R.string.send_email), "teste@teste.com", "teste2@teste.com") }
    }

}