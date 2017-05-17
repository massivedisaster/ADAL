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

package com.massivedisaster.adal.sample.feature.splash;

import com.massivedisaster.activitymanager.ActivityFragmentManager;
import com.massivedisaster.adal.sample.base.activity.ActivityFullScreen;
import com.massivedisaster.adal.sample.feature.home.FragmentHome;
import com.massivedisaster.adal.sample.feature.location.FragmentLocation;

public class ActivitySplashScreen extends ActivityFullScreen {

    @Override
    protected void onStart() {
        super.onStart();

        if (getSupportFragmentManager().getBackStackEntryCount() == 0 && !getIntent().hasExtra(ActivityFragmentManager.ACTIVITY_MANAGER_FRAGMENT)) {
            performTransaction(new FragmentHome(), FragmentHome.class.getCanonicalName());
        }
    }
}