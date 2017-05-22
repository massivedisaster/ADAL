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

package com.massivedisaster.adal.sample.feature.analytics;

import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.massivedisaster.activitymanager.ActivityFragmentManager;
import com.massivedisaster.adal.analytics.AnalyticsManager;
import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.base.activity.ActivityToolbar;

public class FragmentAnalytics extends AbstractBaseFragment {

    private AppCompatEditText mEdtAnalyticsEventLabel;

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_analytics;
    }

    @Override
    protected void doOnCreated() {
        super.doOnCreated();

        /*
         * You need to generate a json file
         *
         * https://developers.google.com/mobile/add?platform=android&cntapi=signin&cnturl=https:%2F%2Fdevelopers.google.com%2Fidentity%2Fsign-in%2Fandroid%2Fsign-in%3Fconfigured%3Dtrue&cntlbl=Continue%20Adding%20Sign-In
         */

        /*
         * Send a screen to GA
         */
        AnalyticsManager.with(getContext()).sendScreen(R.string.analytics_screen_main);

        Button button = findViewById(R.id.btnAnalyticsNewFragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFragmentManager.open(getActivity(), ActivityToolbar.class, FragmentAnalyticsLabel.class);
            }
        });

        Button btnAnalyticsSendEvent = findViewById(R.id.btnAnalyticsSendEvent);
        btnAnalyticsSendEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Send an event to GA
                 */
                AnalyticsManager.with(getContext()).sendEvent(
                        R.string.analytics_event_category,
                        R.string.analytics_event_action);
                showEventMessage();
            }
        });

        mEdtAnalyticsEventLabel = findViewById(R.id.edtAnalyticsEventLabel);

        Button btnAnalyticsSendEventLabel = findViewById(R.id.btnAnalyticsSendEventLabel);
        btnAnalyticsSendEventLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String label = mEdtAnalyticsEventLabel.getText().toString();

                if (TextUtils.isEmpty(label)) {
                    label = "MY LABEL";
                }

                /*
                 * Send an event with a label to GA
                 */
                AnalyticsManager.with(getContext()).sendEvent(
                        R.string.analytics_event_category,
                        R.string.analytics_event_action,
                        label);
                showEventMessage();
            }
        });
    }

    private void showEventMessage() {
        findViewById(R.id.txtAnalyticsSendEventMessage).setVisibility(View.VISIBLE);
    }
}
