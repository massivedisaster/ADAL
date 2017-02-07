package com.massivedisaster.adal.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by Jorge Costa on 13/01/17.
 */

public class ToolbarUtils {

    public static void centerTitle(final Activity a, final View v) {
        v.post(new Runnable() {
            @Override
            public void run() {
                int marginRight = a.getWindow().getDecorView().getWidth() - v.getRight();

                if (marginRight > v.getLeft()) {
                    v.setPadding(marginRight - v.getLeft(), 0, 0, 0);
                } else {
                    v.setPadding(0, 0, v.getLeft() - marginRight, 0);
                }
            }
        });
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }
}
