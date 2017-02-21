package com.massivedisaster.adal.sample.feature.splash;

import com.massivedisaster.adal.sample.base.activity.ActivityFullScreen;
import com.massivedisaster.activitymanager.ActivityFragmentManager;

public class ActivitySplashScreen extends ActivityFullScreen {

    @Override
    protected void onStart() {
        super.onStart();

        if (getSupportFragmentManager().getBackStackEntryCount() == 0 && !getIntent().hasExtra(ActivityFragmentManager.ACTIVITY_MANAGER_FRAGMENT)) {
            performTransaction(new FragmentSplashScreen(), FragmentSplashScreen.class.getCanonicalName());
        }
    }
}