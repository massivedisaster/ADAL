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
        getActivity().setTitle(R.string.sample_bangbus);

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
