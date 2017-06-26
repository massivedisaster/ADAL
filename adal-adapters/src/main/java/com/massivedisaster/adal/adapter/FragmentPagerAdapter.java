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

package com.massivedisaster.adal.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.massivedisaster.adal.utils.LogUtils;

import java.util.List;

/**
 * The adapter of fragments.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Class<? extends Fragment>> mLstFragments;

    private final SparseArray<Fragment> mFragmentSparseArray;

    /**
     * Constructor of the adapter.
     *
     * @param fm          The fragment manager.
     * @param lstFragment The list of fragments to be add to the adapter.
     */
    public FragmentPagerAdapter(FragmentManager fm, List<Class<? extends Fragment>> lstFragment) {
        super(fm);
        mLstFragments = lstFragment;
        mFragmentSparseArray = new SparseArray<>(lstFragment.size());
    }

    @Override
    public Fragment getItem(int position) {
        try {

            Fragment fragment = mFragmentSparseArray.get(position);

            if (fragment == null) {
                fragment = mLstFragments.get(position).newInstance();
                mFragmentSparseArray.put(position, fragment);
                fragment.setRetainInstance(true);
            }

            return fragment;

        } catch (InstantiationException e) {
            LogUtils.logErrorException(FragmentPagerAdapter.class, e);
        } catch (IllegalAccessException e) {
            LogUtils.logErrorException(FragmentPagerAdapter.class, e);
        }

        return new Fragment();
    }

    @Override
    public int getCount() {
        return mLstFragments.size();
    }

    /**
     * Get all the fragments in the adapter.
     *
     * @return The {@link SparseArray} with all the fragments.
     */
    public SparseArray<Fragment> getFragments() {
        return mFragmentSparseArray;
    }
}
