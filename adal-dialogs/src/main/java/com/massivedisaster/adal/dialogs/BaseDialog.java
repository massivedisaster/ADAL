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
package com.massivedisaster.adal.dialogs;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Window;

/**
 * Base class for dialogs.
 */
public class BaseDialog extends AlertDialog {

    private static final int INVALID_RESOURCE_ID = -1;

    /**
     * BaseDialog Constructor.
     *
     * @param context The application context.
     */
    protected BaseDialog(@NonNull Context context) {
        super(context);

        init();
    }

    /**
     * BaseDialog Constructor.
     *
     * @param context    The application context.
     * @param themeResId The theme.
     */
    protected BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        init();
    }

    /**
     * BaseDialog Constructor.
     *
     * @param context        The application context.
     * @param cancelable     The flag to indicate if teh dialogs is cancelable.
     * @param cancelListener The cancel listener.
     */
    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        init();
    }

    /**
     * Init function to apply in all constructors.
     */
    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * <p>Used to specify dialog layout</p>
     *
     * @return Layout ID
     */
    @LayoutRes
    protected int layoutToInflate() {
        return INVALID_RESOURCE_ID;
    }

    /**
     * <p>This method is called when the view is already created and is available to inflate
     * children views</p>
     */
    protected void doOnCreated() {
        // Intended.
    }

    @Override
    public void show() {
        super.show();
        setContentView(layoutToInflate());

        doOnCreated();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
