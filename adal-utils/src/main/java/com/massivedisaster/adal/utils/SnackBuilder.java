package com.massivedisaster.adal.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class SnackBuilder {

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
            @Override
            public void onClick(View view) {

            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), actionColor));

        snackbar.show();
    }
}
