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

package com.massivedisaster.adal.sample.feature.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.utils.AppUtils;
import com.massivedisaster.adal.utils.SnackBuilder;

public class FragmentUtils extends AbstractBaseFragment {

    private Button mBtnCheckPlayservicesExists;

    @Override
    protected void getFromBundle(Bundle bundle) {
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_utils;
    }

    @Override
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void doOnCreated() {
        getActivity().setTitle(R.string.sample_utils);

        mBtnCheckPlayservicesExists = findViewById(R.id.btnCheckPlayservicesExists);

        findViewById(R.id.btnOpenSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.openAppSettings(getActivity());
            }
        });

        findViewById(R.id.btnCheckPlayservicesExists).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackBuilder.show(mBtnCheckPlayservicesExists, AppUtils.checkPlayServicesExists(getActivity()) ? R.string.playservices_exists : R.string.playservices_dont_exists, R.color.colorAccent);
            }
        });

        findViewById(R.id.btnOpenDial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.openDial(getContext(), "00351910000000");
            }
        });

        findViewById(R.id.btnOpenEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.openEmail(getContext(), getString(R.string.send_email), "teste@teste.com", "teste2@teste.com");
            }
        });
    }
}
