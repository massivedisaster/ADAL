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
