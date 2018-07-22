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

import android.support.v7.widget.AppCompatEditText
import android.text.TextUtils
import android.widget.Button
import android.widget.RadioGroup
import com.massivedisaster.adal.analytics.FirebaseAnalyticsManager
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.samplekotlin.R
import com.massivedisaster.adal.samplekotlin.base.activity.ActivityToolbar
import com.massivedisaster.adal.utils.SnackBuilder
import com.massivedisaster.afm.ActivityCall
import java.util.*

class FragmentFirebaseAnalytics : BaseFragment() {

    private var mEdtAnalyticsEventLabel: AppCompatEditText? = null

    override fun layoutToInflate(): Int = R.layout.fragment_firebase_analytics

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_firebase_analytics)

        /*
         * Send a screen to FA
         */
        FirebaseAnalyticsManager.sendScreen(activity, R.string.analytics_screen_main)

        val button = findViewById<Button>(R.id.btnAnalyticsNewFragment)
        button!!.setOnClickListener {
            ActivityCall.init(activity!!, ActivityToolbar::class, FragmentFirebaseAnalyticsLabel::class!!)
                    .build()
        }

        val btnAnalyticsSendEvent = findViewById<Button>(R.id.btnAnalyticsSendEvent)
        btnAnalyticsSendEvent!!.setOnClickListener {
            /*
 * Send an event to FA
 */
            val hashMap = HashMap<String, String>()
            hashMap.put("event_category", getString(R.string.analytics_event_category))
            hashMap.put("event_action", getString(R.string.analytics_event_action))

            FirebaseAnalyticsManager.sendEvent(activity, R.string.analytics_event_name, hashMap)

            showEventMessage()
        }

        mEdtAnalyticsEventLabel = findViewById(R.id.edtAnalyticsEventLabel)

        val btnAnalyticsSendEventLabel = findViewById<Button>(R.id.btnAnalyticsSendEventLabel)
        btnAnalyticsSendEventLabel!!.setOnClickListener {
            var label = mEdtAnalyticsEventLabel!!.text.toString()

            if (TextUtils.isEmpty(label)) {
                label = "MY LABEL"
            }

            /*
 * Send an event with a label to FA
 */
            val hashMap = HashMap<String, String>()
            hashMap.put("event_category", getString(R.string.analytics_event_category))
            hashMap.put("event_action", getString(R.string.analytics_event_action))
            hashMap.put("event_label", label)

            FirebaseAnalyticsManager.sendEvent(activity, R.string.analytics_event_name, hashMap)

            showEventMessage()
        }

        val rdgAnalyticsGender = findViewById<RadioGroup>(R.id.rdgAnalyticsGender)
        rdgAnalyticsGender!!.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rdbAnalyticsGenderM -> {
                    FirebaseAnalyticsManager.sendUserProperty(
                            activity,
                            R.string.analytics_gender_title,
                            R.string.analytics_gender_male)
                    showUserPropertyMessage()
                }
                R.id.rdbAnalyticsGenderF -> {
                    FirebaseAnalyticsManager.sendUserProperty(
                            activity,
                            R.string.analytics_gender_title,
                            R.string.analytics_gender_female)
                    showUserPropertyMessage()
                }
            }
        }

    }

    private fun showEventMessage() {
        SnackBuilder.show(view, R.string.firebase_analytics_message_events, R.color.colorAccent)
    }

    private fun showUserPropertyMessage() {
        SnackBuilder.show(view, R.string.firebase_analytics_message_user_property, R.color.colorAccent)
    }
}