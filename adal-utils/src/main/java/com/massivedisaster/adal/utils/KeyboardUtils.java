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

package com.massivedisaster.adal.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Manages keyboard action enabling users to force hide keyboard
 */
public final class KeyboardUtils {

    private static final int HIDE_SOFT_INPUT_FLAGS_NONE = 0;

    /**
     * Private constructor to avoid user implement as a single instance instead of a Singleton
     */
    private KeyboardUtils() {

    }

    /**
     * Hide keyboard
     *
     * @param activity the visible activity
     */
    public static void hide(Activity activity) {
        internalHide(activity, activity.getCurrentFocus());
    }

    /**
     * Hide keyboard
     *
     * @param activity the visible activity
     * @param view     view to get getWindowToken
     */
    public static void hide(Activity activity, View view) {
        internalHide(activity, view);
    }

    /**
     *
     *
     * @param activity  context to retrieve system services
     * @param view      view to get getWindowToken
     */
    private static void internalHide(Activity activity, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), HIDE_SOFT_INPUT_FLAGS_NONE);
        }
    }
}
