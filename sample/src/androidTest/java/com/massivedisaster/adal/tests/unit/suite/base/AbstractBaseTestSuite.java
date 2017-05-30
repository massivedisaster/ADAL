/*
 * ADAL - A set of Android libraries to help speed up Android development.
 * Copyright (C) 2017 ADAL.
 *
 * ADAL is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or any later version.
 *
 * ADAL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with ADAL. If not, see <http://www.gnu.org/licenses/>.
 */

package com.massivedisaster.adal.tests.unit.suite.base;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;

/**
 * <b>AbstractBaseTestSuite class</b>
 *
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
