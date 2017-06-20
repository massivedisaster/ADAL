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

package com.massivedisaster.adal.sample.feature.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.massivedisaster.activitymanager.ActivityFragmentManager;
import com.massivedisaster.adal.adapter.OnChildClickListener;
import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.base.activity.ActivityToolbar;
import com.massivedisaster.adal.sample.feature.accounts.FragmentAccounts;
import com.massivedisaster.adal.sample.feature.analytics.FragmentAnalytics;
import com.massivedisaster.adal.sample.feature.bus.FragmentA;
import com.massivedisaster.adal.sample.feature.location.FragmentLocation;
import com.massivedisaster.adal.sample.feature.network.FragmentNetworkRequest;
import com.massivedisaster.adal.sample.feature.permissions.FragmentPermissions;
import com.massivedisaster.adal.sample.feature.utils.FragmentUtils;

import java.util.HashMap;

public class FragmentHome extends AbstractBaseFragment {

    private RecyclerView mRclItems;

    @Override
    protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_home;
    }

    @Override
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Intended.
    }

    @Override
    protected void doOnCreated() {
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        mRclItems = findViewById(R.id.rclItems);

        initialize();
    }

    private void initialize() {
        AdapterExample adapter = new AdapterExample(getExamples());
        adapter.setOnChildClickListener(new OnChildClickListener<String>() {
            @Override
            public void onChildClick(View view, String key, int position) {
                ActivityFragmentManager.open(getActivity(), ActivityToolbar.class, getExamples().get(key));
            }
        });

        mRclItems.setLayoutManager(new LinearLayoutManager(getContext()));
        mRclItems.setAdapter(adapter);
    }

    public HashMap<String, Class<? extends Fragment>> getExamples() {
        return new HashMap<String, Class<? extends Fragment>>() {{
            put("Location", FragmentLocation.class);
            put("Permission", FragmentPermissions.class);
            put("Accounts", FragmentAccounts.class);
            put("Bangbus", FragmentA.class);
            put("Network", FragmentNetworkRequest.class);
            put("Analytics", FragmentAnalytics.class);
            put("Utils", FragmentUtils.class);
        }};
    }
}
