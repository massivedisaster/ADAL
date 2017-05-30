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

package com.massivedisaster.adal.sample.feature.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
            put("Adapter", FragmentNetworkRequest.class);
            put("Analytics", FragmentAnalytics.class);
            put("Utils", FragmentUtils.class);
        }};
    }
}
