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

package com.massivedisaster.adal.samplekotlin.feature.analytics

import android.os.Bundle
import android.widget.TextView
import com.massivedisaster.adal.analytics.FirebaseAnalyticsManager
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.samplekotlin.R

class FragmentFirebaseAnalyticsLabel : BaseFragment() {

    override fun getFromBundle(bundle: Bundle) {
        // Intended.
    }

    override fun layoutToInflate(): Int = R.layout.fragment_analytics_label

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        // Intended.
    }

    override fun doOnCreated() {

        val txtAnalyticsLabel = findViewById<TextView>(R.id.txtAnalyticsLabel)
        txtAnalyticsLabel!!.setText(R.string.firebase_analytics_message_screen_label)

        /*
         * Send a screen with a label to FA
         *
         * The screen must have a formatted string to put your label (see R.string.analytics_screen_label)
         */
        FirebaseAnalyticsManager.sendScreen(activity, R.string.analytics_screen_label, "My Label")
    }
}