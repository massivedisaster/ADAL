package com.massivedisaster.adal.sample.base.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.massivedisaster.activitymanager.AbstractFragmentActivity;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.utils.ToolbarUtils;

/**
 * ADAL by Carbon by BOLD
 * Created in 4/27/17 by the following authors:
 * Jorge Costa - {jorgecosta@carbonbybold.com}
 */
public class ActivityToolbar extends AbstractFragmentActivity {

    protected TextView toolBarTitle;
    protected Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setContentInsetsAbsolute(0, 0);

        ToolbarUtils.centerTitle(this, toolBarTitle);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_toolbar;
    }

    @Override
    protected int getContainerViewId() {
        return R.id.frmContainer;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
