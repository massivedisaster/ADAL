/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2018 ADAL
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

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.massivedisaster.adal.utils.KeyboardUtils;

/**
 * Base class for dialogs.
 */
public class BaseDialogFragment extends DialogFragment {

    private static final int INVALID_RESOURCE_ID = -1;

    private View mView;

    /**
     * <p>Used to get data from bundle</p>
     *
     * @param bundle Fragment Bundle
     */
    protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    /**
     * <p>Used to specify dialog fragment layout</p>
     *
     * @return Layout ID
     */
    @LayoutRes
    protected int layoutToInflate() {
        return INVALID_RESOURCE_ID;
    }

    /**
     * <p>Restores last instance state of the dialog fragment, this method is always empty
     * and going to restore data only when it's override</p>
     *
     * @param savedInstanceState Last instance state saved from this dialog fragment
     */
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Intended.
    }

    /**
     * <p>This method is called when the view is already created and is available to inflate
     * children views</p>
     */
    protected void doOnCreated() {
        // Intended.
    }

    /**
     * <p>Request a view by id in case is there any root view inflated
     * otherwise throws a null pointer exception</p>
     *
     * @param viewId Id of the requested view (child)
     * @param <T>    Type of the requested view
     * @return view requested if it exists else null caused by root not inflated.
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(@IdRes int viewId) {
        View view = mView == null ? getView() : mView;
        if (view == null) {
            return null;
        }
        return (T) view.findViewById(viewId);
    }

    /**
     * <p>Request a view by id on activity sight in case is there any activity
     * otherwise throws a null pointer exception</p>
     *
     * @param viewId Id of the requested view (child)
     * @param <T>    Type of the requested view
     * @return view requested if it exists else null if fragment is not attached to the activity yet.
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewByIdOnActivity(@IdRes int viewId) {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return (T) activity.findViewById(viewId);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;

        if (layoutToInflate() != INVALID_RESOURCE_ID) {
            view = inflater.inflate(layoutToInflate(), container, false);
            mView = view;
        }

        restoreInstanceState(savedInstanceState);

        if (getArguments() != null) {
            getFromBundle(getArguments());
        }

        doOnCreated();
        return view;
    }

    @Override
    public void onPause() {
        KeyboardUtils.hide(getActivity(), getView());
        super.onPause();
    }
}
