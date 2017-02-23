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
