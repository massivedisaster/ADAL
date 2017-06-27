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
