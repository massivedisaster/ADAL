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

package com.massivedisaster.adal.samplekotlin.app

import android.app.Application
import android.widget.Toast
import com.massivedisaster.adal.applicationstate.ApplicationStateManager

class App : Application(), ApplicationStateManager.BackAndForegroundListener {

    var TAG: String = App::class.simpleName!!

    private var mApplicationStateManager: ApplicationStateManager? = null

    override fun onCreate() {
        super.onCreate()
        mApplicationStateManager = ApplicationStateManager(this)
        registerActivityLifecycleCallbacks(mApplicationStateManager)
    }

    override fun onBackground() {
        Toast.makeText(this, "onBackground called", Toast.LENGTH_SHORT).show()
    }

    override fun onForeground() {
        Toast.makeText(this, "onForeground called", Toast.LENGTH_SHORT).show()
    }

    fun isBackground(): Boolean = mApplicationStateManager!!.isBackground

    fun isForeground(): Boolean = mApplicationStateManager!!.isForeground
}