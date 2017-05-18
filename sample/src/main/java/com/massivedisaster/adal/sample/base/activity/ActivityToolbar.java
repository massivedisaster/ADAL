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

package com.massivedisaster.adal.sample.base.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.massivedisaster.activitymanager.AbstractFragmentActivity;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.utils.ToolbarUtils;

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
