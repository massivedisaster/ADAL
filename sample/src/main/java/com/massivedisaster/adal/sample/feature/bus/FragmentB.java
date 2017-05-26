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

import android.view.View;
import android.widget.Button;

import com.massivedisaster.adal.bus.BangBus;
import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;

public class FragmentB extends AbstractBaseFragment {

    public static final String BANG_MESSAGE_WITH_ACTION = "received bang with action";
    public static final int BANG_NUMBER_WITHOUT_NUMBER = 666;

    private Button mBtnSendBangWithAction;
    private Button mBtnSendBangWithoutAction;

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_b;
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
