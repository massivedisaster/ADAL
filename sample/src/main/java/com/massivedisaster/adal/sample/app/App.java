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

package com.massivedisaster.adal.sample.app;

import android.app.Application;
import android.widget.Toast;

import com.massivedisaster.adal.applicationstate.ApplicationStateManager;

public class App extends Application implements ApplicationStateManager.BackAndForegroundListener {

    private static final String TAG = App.class.getSimpleName();
    private ApplicationStateManager applicationStateManager;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationStateManager = new ApplicationStateManager(this);
        registerActivityLifecycleCallbacks(applicationStateManager);
    }

    @Override
    public void onBackground() {
        Toast.makeText(this, "onBackground called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onForeground() {
        Toast.makeText(this, "onForeground called", Toast.LENGTH_SHORT).show();
    }

    public boolean isBackground() {
        return applicationStateManager.isBackground();
    }

    public boolean isForeground() {
        return applicationStateManager.isForeground();
    }
}
