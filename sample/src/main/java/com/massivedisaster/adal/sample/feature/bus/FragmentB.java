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

import com.massivedisaster.adal.bus.BangBus;
import com.massivedisaster.adal.fragment.BaseFragment;
import com.massivedisaster.adal.sample.R;

public class FragmentB extends BaseFragment {

    public static final String BANG_MESSAGE_WITH_ACTION = "received bang with action";
    public static final int BANG_NUMBER_WITHOUT_NUMBER = 666;

    private Button mBtnSendBangWithAction;
    private Button mBtnSendBangWithoutAction;

    @Override
    protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_b;
    }

    @Override
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Intended.
    }

    @Override
    protected void doOnCreated() {
        mBtnSendBangWithAction = findViewById(R.id.btnSendBangWithAction);
        mBtnSendBangWithoutAction = findViewById(R.id.btnSendBangWithoutAction);

        initialize();
    }

    public void initialize() {
        mBtnSendBangWithAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BangBus
                        .with(getContext())
                        .addAction(FragmentA.BANG_A)
                        .setParameter(BANG_MESSAGE_WITH_ACTION)
                        .bang();

                getActivity().finish();
            }
        });

        mBtnSendBangWithoutAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BangBus
                        .with(getContext())
                        .setParameter(BANG_NUMBER_WITHOUT_NUMBER)
                        .bang();

                getActivity().finish();
            }
        });
    }
}
