/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2017 ADAL
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.massivedisaster.adal.sample.feature.analytics;

import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.massivedisaster.activitymanager.ActivityFragmentManager;
import com.massivedisaster.adal.analytics.FirebaseAnalyticsManager;
import com.massivedisaster.adal.fragment.BaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.base.activity.ActivityToolbar;
import com.massivedisaster.adal.utils.SnackBuilder;

import java.util.HashMap;

public class FragmentFirebaseAnalytics extends BaseFragment {

    private AppCompatEditText mEdtAnalyticsEventLabel;

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_firebase_analytics;
    }

    @Override
    protected void doOnCreated() {
        getActivity().setTitle(R.string.sample_firebase_analytics);

        /*
         * Send a screen to FA
         */
        FirebaseAnalyticsManager.sendScreen(getActivity(), R.string.analytics_screen_main);

        Button button = findViewById(R.id.btnAnalyticsNewFragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFragmentManager.open(getActivity(), ActivityToolbar.class, FragmentFirebaseAnalyticsLabel.class);
            }
        });

        Button btnAnalyticsSendEvent = findViewById(R.id.btnAnalyticsSendEvent);
        btnAnalyticsSendEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Send an event to FA
                 */
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("event_category", getString(R.string.analytics_event_category));
                hashMap.put("event_action", getString(R.string.analytics_event_action));

                FirebaseAnalyticsManager.sendEvent(getActivity(), R.string.analytics_event_name, hashMap);

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
                 * Send an event with a label to FA
                 */
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("event_category", getString(R.string.analytics_event_category));
                hashMap.put("event_action", getString(R.string.analytics_event_action));
                hashMap.put("event_label", label);

                FirebaseAnalyticsManager.sendEvent(getActivity(), R.string.analytics_event_name, hashMap);

                showEventMessage();
            }
        });

        RadioGroup rdgAnalyticsGender = findViewById(R.id.rdgAnalyticsGender);
        rdgAnalyticsGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rdbAnalyticsGenderM:
                        FirebaseAnalyticsManager.sendUserProperty(
                                getActivity(),
                                R.string.analytics_gender_title,
                                R.string.analytics_gender_male);
                        showUserPropertyMessage();
                        break;
                    case R.id.rdbAnalyticsGenderF:
                        FirebaseAnalyticsManager.sendUserProperty(
                                getActivity(),
                                R.string.analytics_gender_title,
                                R.string.analytics_gender_female);
                        showUserPropertyMessage();
                        break;
                }
            }
        });

    }

    private void showEventMessage() {
        SnackBuilder.show(getView(), R.string.firebase_analytics_message_events, R.color.colorAccent);
    }

    private void showUserPropertyMessage() {
        SnackBuilder.show(getView(), R.string.firebase_analytics_message_user_property, R.color.colorAccent);
    }
}
