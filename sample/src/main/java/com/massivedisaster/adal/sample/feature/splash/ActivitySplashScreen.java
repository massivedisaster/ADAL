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