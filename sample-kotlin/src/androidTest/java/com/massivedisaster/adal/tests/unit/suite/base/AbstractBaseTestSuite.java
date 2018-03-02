/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2017 ADAL
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

package com.massivedisaster.adal.tests.unit.suite.base;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.view.WindowManager;

import org.junit.After;
import org.junit.Before;

/**
 * <b>AbstractBaseTestSuite class</b>
 * <p>
 * <p>Base class to execute instrumented or unit test suites that needs a context</p>
 */
public abstract class AbstractBaseTestSuite {

    private Context mContext;

    /**
     * <p>Set up all child instances and necessary prerequisites</p>
     */
    protected abstract void setup();

    /**
     * <p>Disposes all child instances declared during the test suite</p>
     */
    protected abstract void dispose();

    /**
     * <p>Initializes the context and all instances necessary to run the test suite</p>
     */
    @Before
    public void initialize() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        mContext = instrumentation.getTargetContext()
                .getApplicationContext();

        setup();
    }

    protected void unlock(final Activity activity) {
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }

    /**
     * <p>Disposes all instances declared during the test suite</p>
     */
    @After
    public void finish() {
        dispose();
    }

    /**
     * <p>Retrieves the test context</p>
     *
     * @return context
     */
    protected Context getContext() {
        return mContext;
    }
}
