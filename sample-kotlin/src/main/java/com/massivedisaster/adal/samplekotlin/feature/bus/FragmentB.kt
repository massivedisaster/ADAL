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
import com.massivedisaster.adal.bus.BangBus
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.samplekotlin.R

class FragmentB : BaseFragment() {

    companion object {
        val BANG_MESSAGE_WITH_ACTION = "received bang with action"
        val BANG_NUMBER_WITHOUT_NUMBER = 666
    }

    private var mBtnSendBangWithAction: Button? = null
    private var mBtnSendBangWithoutAction: Button? = null

    override fun getFromBundle(bundle: Bundle) {
        // Intended.
    }

    override fun layoutToInflate(): Int = R.layout.fragment_b

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        // Intended.
    }

    override fun doOnCreated() {
        mBtnSendBangWithAction = findViewById(R.id.btnSendBangWithAction)
        mBtnSendBangWithoutAction = findViewById(R.id.btnSendBangWithoutAction)

        initialize()
    }

    private fun initialize() {
        mBtnSendBangWithAction!!.setOnClickListener {
            BangBus
                    .with(context)
                    .addAction(FragmentA.BANG_A)
                    .setParameter(BANG_MESSAGE_WITH_ACTION)
                    .bang()

            activity!!.finish()
        }

        mBtnSendBangWithoutAction!!.setOnClickListener {
            BangBus
                    .with(context)
                    .setParameter(BANG_NUMBER_WITHOUT_NUMBER)
                    .bang()

            activity!!.finish()
        }
    }
}