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

package com.massivedisaster.adal.tests.unit.suite.base

import android.app.Activity
import android.content.Context
import android.support.test.InstrumentationRegistry
import android.view.WindowManager
import org.junit.After
import org.junit.Before

abstract class AbstractBaseTestSuite {

    private var mContext: Context? = null

    /**
     *
     * Set up all child instances and necessary prerequisites
     */
    protected abstract fun setup()

    /**
     *
     * Disposes all child instances declared during the test suite
     */
    protected abstract fun dispose()

    /**
     *
     * Initializes the context and all instances necessary to run the test suite
     */
    @Before
    fun initialize() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        mContext = instrumentation.targetContext
                .applicationContext

        setup()
    }

    protected fun unlock(activity: Activity) {
        val wakeUpDevice = Runnable {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        activity.runOnUiThread(wakeUpDevice)
    }

    /**
     *
     * Disposes all instances declared during the test suite
     */
    @After
    fun finish() {
        dispose()
    }

    /**
     *
     * Retrieves the test context
     *
     * @return context
     */
    protected fun getContext(): Context? {
        return mContext
    }

}