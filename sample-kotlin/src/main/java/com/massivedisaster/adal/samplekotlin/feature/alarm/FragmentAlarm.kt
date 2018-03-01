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

package com.massivedisaster.adal.samplekotlin.feature.alarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.massivedisaster.adal.alarm.AlarmManager
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.samplekotlin.R
import com.massivedisaster.adal.utils.SnackBuilder
import java.util.*

class FragmentAlarm : BaseFragment(), View.OnClickListener {

    val PARAM_EVENT_NAME: String = "PARAM_EVENT_NAME"

    private var mBtnAddAlarm: Button? = null
    private var mBtnRemoveAlarm: Button? = null
    private var mIntentAlarm: Intent? = null

    override fun layoutToInflate(): Int {
        return R.layout.fragment_alarm
    }

    override fun getFromBundle(bundle: Bundle) {

    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {

    }

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_alarm)

        mBtnAddAlarm = findViewById(R.id.btnAddAlarm)
        mBtnRemoveAlarm = findViewById(R.id.btnRemoveAlarm)

        mBtnAddAlarm!!.setOnClickListener(this)
        mBtnRemoveAlarm!!.setOnClickListener(this)

        mIntentAlarm = Intent(context, AlarmReceiver::class.java)
        mIntentAlarm!!.putExtra(PARAM_EVENT_NAME, "Alarm message!")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnAddAlarm -> addAlarm()
            R.id.btnRemoveAlarm -> removeAlarm()
        }
    }

    /**
     * Add a new alarm to the system.
     */
    private fun addAlarm() {
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        if (!AlarmManager.hasAlarm(context, mIntentAlarm, 1001)) {
            AlarmManager.addAlarm(context, mIntentAlarm, 1001, calendar)
            SnackBuilder.show(mBtnRemoveAlarm, "Alarm added!", R.color.colorAccent)
        } else {
            SnackBuilder.show(mBtnAddAlarm, "Alarm already added.", R.color.colorAccent)
        }
    }

    /**
     * Remove an alarm.
     */
    private fun removeAlarm() {
        if (AlarmManager.hasAlarm(context, mIntentAlarm, 1001)) {
            AlarmManager.cancelAlarm(context, mIntentAlarm, 1001)
            SnackBuilder.show(mBtnRemoveAlarm, "Alarm removed!", R.color.colorAccent)
        } else {
            SnackBuilder.show(mBtnRemoveAlarm, "Please, add an alarm first!", R.color.colorAccent)
        }
    }

}