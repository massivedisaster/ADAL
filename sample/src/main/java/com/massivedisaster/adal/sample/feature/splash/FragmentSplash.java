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
import com.massivedisaster.adal.fragment.AbstractSplashFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.feature.home.FragmentHome;

public class FragmentSplash extends AbstractSplashFragment {

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_splash_screen;
    }

    @Override
    protected void onSplashStarted() {
        onSplashFinish(openHome());
    }

    private OnFinishSplashScreen openHome() {
        return new OnFinishSplashScreen() {
            @Override
            public void onFinish() {
                ActivityFragmentManager.open(getActivity(), ActivitySplashScreen.class, FragmentHome.class);
                getActivity().finish();
            }
        };
    }
}
