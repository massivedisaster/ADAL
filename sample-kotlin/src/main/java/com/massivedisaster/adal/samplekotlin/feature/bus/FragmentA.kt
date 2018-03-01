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

package com.massivedisaster.adal.samplekotlin.feature.bus

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.massivedisaster.adal.bus.BangBus
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.samplekotlin.R
import com.massivedisaster.adal.samplekotlin.base.activity.ActivityToolbar
import com.massivedisaster.afm.ActivityCall

class FragmentA : BaseFragment() {

    val BANG_A: String = "BANG_A"

    private var mBangBus: BangBus? = null
    private var mBtnSubscribeOpenB: Button? = null
    private var mUnsubscribeBtnOpenB: Button? = null
    private var mTxtResult: TextView? = null

    override fun getFromBundle(bundle: Bundle) {
        // Intended.
    }

    override fun layoutToInflate(): Int {
        return R.layout.fragment_a
    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        // Intended.
    }

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_bangbus)

        mBtnSubscribeOpenB = findViewById(R.id.btnSubscribeOpenB)
        mUnsubscribeBtnOpenB = findViewById(R.id.btnUnsubscribeOpenB)
        mTxtResult = findViewById(R.id.txtResult)

        initialize()
    }

    fun initialize() {
        mBangBus = BangBus(activity)
        mBangBus!!.subscribe(this)

        mBtnSubscribeOpenB!!.setOnClickListener {
            mTxtResult!!.text = ""
            mBangBus!!.subscribe(this@FragmentA)
            ActivityCall.init(activity!!, ActivityToolbar::class, FragmentB::class!!)
                    .build()
        }

        mUnsubscribeBtnOpenB!!.setOnClickListener {
            mTxtResult!!.text = ""
            mBangBus!!.unsubscribe()
            ActivityCall.init(activity!!, ActivityToolbar::class, FragmentB::class!!)
                    .build()
        }
    }

    @BangBus.SubscribeBang(action = "BANG_A")
    fun bangWithAction(message: String) {
        mTxtResult!!.text = message
    }

    @BangBus.SubscribeBang
    fun bangWithoutAction(number: Int?) {
        mTxtResult!!.text = number.toString()
    }

    override fun onDestroy() {
        mBangBus!!.unsubscribe()
        super.onDestroy()
    }
}