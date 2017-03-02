package com.massivedisaster.adal.sample.base.activity;

import com.massivedisaster.activitymanager.AbstractFragmentActivity;
import com.massivedisaster.adal.sample.R;

public class ActivityFullScreen extends AbstractFragmentActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fullscreen;
    }

    @Override
    protected int getContainerViewId() {
        return R.id.frmContainer;
    }
}
