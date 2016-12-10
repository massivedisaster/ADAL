package com.massivedisaster.adal.util;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.massivedisaster.adal.R;

public class SnackBuilder {

    public static void show(View v, int messageRes) {
        show(v, v.getContext().getString(messageRes));
    }

    public static void show(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(view.getContext().getString(android.R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));

        snackbar.show();
    }
}
