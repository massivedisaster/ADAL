package com.massivedisaster.adal.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    private static final int sHideSoftInputFlagsNone = 0;

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

    private static void internalHide(Activity activity, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), sHideSoftInputFlagsNone);
        }
    }
}
