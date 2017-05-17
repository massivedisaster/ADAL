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

public class ToolbarUtils {

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
