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

package com.massivedisaster.adal.tests.instrumented.suite.adapters;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.massivedisaster.activitymanager.ActivityFragmentManager;
import com.massivedisaster.adal.sample.base.activity.ActivityToolbar;
import com.massivedisaster.adal.sample.feature.network.FragmentNetworkRequest;
import com.massivedisaster.adal.tests.unit.suite.base.AbstractBaseTestSuite;

import org.junit.Rule;

public class AbstractBaseAdapterTests extends AbstractBaseTestSuite {

    @Rule
    public ActivityTestRule<ActivityToolbar> activityTestRule = new ActivityTestRule<>(ActivityToolbar.class, true, false);

    @Override
    protected void setup() {
        Intent intent = new Intent();

        intent.putExtra(ActivityFragmentManager.ACTIVITY_MANAGER_FRAGMENT, FragmentNetworkRequest.class.getCanonicalName());
        intent.putExtra(ActivityFragmentManager.ACTIVITY_MANAGER_FRAGMENT_TAG, FragmentNetworkRequest.class.getCanonicalName());

        activityTestRule.launchActivity(intent);
    }

    @Override
    protected void dispose() {

    }
}
