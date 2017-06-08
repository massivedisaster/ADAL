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

package com.massivedisaster.adal.sample.feature.bus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.massivedisaster.activitymanager.ActivityFragmentManager;
import com.massivedisaster.adal.bus.BangBus;
import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.base.activity.ActivityToolbar;

public class FragmentA extends AbstractBaseFragment {

    public static final String BANG_A = "BANG_A";

    private BangBus mBangBus;
    private Button mBtnSubscribeOpenB;
    private Button mUnsubscribeBtnOpenB;
    private TextView mTxtResult;

    @Override
    protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_a;
    }

    @Override
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Intended.
    }

    @Override
    protected void doOnCreated() {
        mBtnSubscribeOpenB = findViewById(R.id.btnSubscribeOpenB);
        mUnsubscribeBtnOpenB = findViewById(R.id.btnUnsubscribeOpenB);
        mTxtResult = findViewById(R.id.txtResult);

        initialize();
    }

    public void initialize() {
        mBangBus = new BangBus(getActivity());
        mBangBus.subscribe(this);

        mBtnSubscribeOpenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtResult.setText("");
                mBangBus.subscribe(FragmentA.this);
                ActivityFragmentManager.open(getActivity(), ActivityToolbar.class, FragmentB.class);
            }
        });

        mUnsubscribeBtnOpenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtResult.setText("");
                mBangBus.unsubscribe();
                ActivityFragmentManager.open(getActivity(), ActivityToolbar.class, FragmentB.class);
            }
        });
    }

    @BangBus.SubscribeBang(action = BANG_A)
    public void bangWithAction(String message) {
        mTxtResult.setText(message);
    }

    @BangBus.SubscribeBang
    public void bangWithoutAction(Integer number) {
        mTxtResult.setText(String.valueOf(number));
    }

    @Override
    public void onDestroy() {
        mBangBus.unsubscribe();
        super.onDestroy();
    }
}
