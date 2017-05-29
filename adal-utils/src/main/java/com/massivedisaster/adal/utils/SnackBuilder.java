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

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * Manages and generates snackbar messages on a given view
 */
public final class SnackBuilder {

    /**
     * Private constructor to avoid user implement as a single instance instead of a Singleton
     */
    private SnackBuilder() {

    }

    /**
     * Create a SnackBar and attach to the view
     *
     * @param view        the view to attach the snack bar
     * @param messageRes  the message id from resources
     * @param actionColor color to set to action bottom
     */
    public static void show(View view, int messageRes, int actionColor) {
        show(view, view.getContext().getString(messageRes), actionColor);
    }

    /**
     * Create a Snackbar and attach to the view
     *
     * @param view        the view to attach the snack bar
     * @param message     the message
     * @param actionColor color to set to action bottom
     */
    public static void show(View view, String message, int actionColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(view.getContext().getString(android.R.string.ok), new View.OnClickListener() {
            /**
             * On click method used to handle user's touch on snackbar
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                //Intended
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), actionColor));

        snackbar.show();
    }
}
