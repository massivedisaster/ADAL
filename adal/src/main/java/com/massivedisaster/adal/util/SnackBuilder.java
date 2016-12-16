package com.massivedisaster.adal.util;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.massivedisaster.adal.R;

public class SnackBuilder {

    public static void show(View v, int messageRes, int actionColor) {
        show(v, v.getContext().getString(messageRes), actionColor);
    }

    public static void show(View v, int messageRes) {
        show(v, v.getContext().getString(messageRes), R.color.colorAccent);
    }

    public static void show(View view, String message) {
        show(view, message, R.color.colorAccent);
    }

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
