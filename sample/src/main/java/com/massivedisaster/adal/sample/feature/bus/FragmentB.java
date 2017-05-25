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

    private Button mSendBang;

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_b;
    }

    @Override
    protected void doOnCreated() {
        mSendBang = findViewById(R.id.btnSendBang);

        initialize();
    }

    public void initialize() {
        mSendBang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BangBus
                        .with(getContext())
                        .addAction(FragmentA.BANG_A)
                        .addParameter("received bang with action from " + FragmentB.class.getSimpleName())
                        .bang();

                getActivity().finish();
            }
        });
    }
}
