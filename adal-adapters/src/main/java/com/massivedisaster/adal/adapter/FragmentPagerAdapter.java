/*
 * ADAL - A set of Android libraries to help speed up Android development.
 * Copyright (C) 2017 ADAL.
 *
 * ADAL is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or any later version.
 *
 * ADAL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with ADAL. If not, see <http://www.gnu.org/licenses/>.
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
