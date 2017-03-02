package com.massivedisaster.adal.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import java.util.List;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Class<? extends Fragment>> mLstFragments;

    private SparseArray<Fragment> fragmentSparseArray;

    public FragmentPagerAdapter(FragmentManager fm, List<Class<? extends Fragment>> lstFragment) {
        super(fm);
        mLstFragments = lstFragment;
        fragmentSparseArray = new SparseArray<>(lstFragment.size());
    }

    @Override
    public Fragment getItem(int position) {
        try {

            Fragment fragment;

            if ((fragment = fragmentSparseArray.get(position)) == null) {

                fragmentSparseArray.put(position, fragment = mLstFragments.get(position).newInstance());
                fragment.setRetainInstance(true);

            }

            return fragment;

        } catch (InstantiationException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new Fragment();
    }

    @Override
    public int getCount() {
        return mLstFragments.size();
    }

    public SparseArray<Fragment> getFragments() {
        return fragmentSparseArray;
    }
}