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

package com.massivedisaster.adal.samplekotlin.feature.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.samplekotlin.R
import com.massivedisaster.adal.samplekotlin.base.activity.ActivityToolbar
import com.massivedisaster.adal.samplekotlin.feature.accounts.FragmentAccounts
import com.massivedisaster.adal.samplekotlin.feature.alarm.FragmentAlarm
import com.massivedisaster.adal.samplekotlin.feature.analytics.FragmentAnalytics
import com.massivedisaster.adal.samplekotlin.feature.analytics.FragmentFirebaseAnalytics
import com.massivedisaster.adal.samplekotlin.feature.bus.FragmentA
import com.massivedisaster.adal.samplekotlin.feature.connectivity.FragmentConnectivityAware
import com.massivedisaster.adal.samplekotlin.feature.dialogs.FragmentDialogs
import com.massivedisaster.adal.samplekotlin.feature.location.FragmentLocation
import com.massivedisaster.adal.samplekotlin.feature.network.FragmentNetworkRequest
import com.massivedisaster.adal.samplekotlin.feature.permissions.FragmentPermissions
import com.massivedisaster.adal.samplekotlin.feature.utils.FragmentUtils
import com.massivedisaster.afm.ActivityCall
import java.util.*
import kotlin.reflect.KClass

class FragmentHome : BaseFragment() {

    private var mRclItems: RecyclerView? = null

    override fun getFromBundle(bundle: Bundle) {
        // Intended.
    }

    override fun layoutToInflate(): Int {
        return R.layout.fragment_home
    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        // Intended.
    }

    override fun doOnCreated() {
        if ((activity as AppCompatActivity).supportActionBar != null) {
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }

        mRclItems = findViewById(R.id.rclItems)

        initialize()
    }

    private fun initialize() {
        val adapter = AdapterExample(getExamples())
        adapter.setOnChildClickListener({ _, key, _ ->
            ActivityCall.init(activity!!, ActivityToolbar::class, getExamples()[key]!!)
                    .build()
        })

        mRclItems!!.layoutManager = LinearLayoutManager(context)
        mRclItems!!.adapter = adapter
    }

    private fun getExamples(): TreeMap<String, KClass<out Fragment>> {
        return object : TreeMap<String, KClass<out Fragment>>() {
            init {
                put("Alarm", FragmentAlarm::class)
                put("Location", FragmentLocation::class)
                put("Permission", FragmentPermissions::class)
                put("Accounts", FragmentAccounts::class)
                put("Bangbus", FragmentA::class)
                put("Network", FragmentNetworkRequest::class)
                put("Analytics", FragmentAnalytics::class)
                put("Firebase Analytics", FragmentFirebaseAnalytics::class)
                put("Utils", FragmentUtils::class)
                put("Connectivity", FragmentConnectivityAware::class)
                put("Dialogs", FragmentDialogs::class)
            }
        }
    }

}