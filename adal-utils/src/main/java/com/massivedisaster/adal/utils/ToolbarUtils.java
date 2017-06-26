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

/**
 * Helper class to retrieve toolbar information and define behaviours like center title
 */
public final class ToolbarUtils {

    /**
     * Private constructor to avoid user implement as a single instance instead of a Singleton
     */
    private ToolbarUtils() {

    }

    /**
     * Center the toolbar view
     *
     * @param activity the activity that contains toolbar
     * @param view     toolbar view
     */
    public static void centerTitle(final Activity activity, final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                int marginRight = activity.getWindow().getDecorView().getWidth() - view.getRight();

                if (marginRight > view.getLeft()) {
                    view.setPadding(marginRight - view.getLeft(), 0, 0, 0);
                } else {
                    view.setPadding(0, 0, view.getLeft() - marginRight, 0);
                }
            }
        });
    }

    /**
     * Get the size of status bar in pixels
     *
     * @param context the application context
     * @return size of status bar in pixels
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }
}
