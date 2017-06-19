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

package com.massivedisaster.adal.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.massivedisaster.adal.utils.KeyboardUtils;

/**
 * Base class for fragments.
 */
public abstract class AbstractBaseFragment extends Fragment {

    private static final int INVALID_RESOURCE_ID = -1;

    /**
     * <p>Used to get data from bundle</p>
     *
     * @param bundle Fragment Bundle
     */
    protected abstract void getFromBundle(Bundle bundle);

    /**
     * <p>Used to specify fragment layout</p>
     *
     * @return Layout ID
     */
    @LayoutRes
    protected int layoutToInflate() {
        return INVALID_RESOURCE_ID;
    }

    /**
     * <p>Restores last instance state of the fragment, this method is always empty
     * and going to restore data only when it's override</p>
     *
     * @param savedInstanceState Last instance state saved from this fragment
     */
    protected abstract void restoreInstanceState(@Nullable Bundle savedInstanceState);

    /**
     * <p>This method is called when the view is already created and is available to inflate
     * children views</p>
     */
    protected abstract void doOnCreated();

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
        View view = getView();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;

        if (layoutToInflate() != INVALID_RESOURCE_ID) {
            view = inflater.inflate(layoutToInflate(), container, false);
        }

        restoreInstanceState(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            getFromBundle(getArguments());
        }

        doOnCreated();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        KeyboardUtils.hide(getActivity(), getView());
        super.onPause();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //Check if the superclass already created the animation
        Animation anim = super.onCreateAnimation(transit, enter, nextAnim);

        if (anim == null && nextAnim != 0) {
            anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }

        if (anim != null) {
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // Intended.
                }
            });

        }

        return anim;
    }
}
