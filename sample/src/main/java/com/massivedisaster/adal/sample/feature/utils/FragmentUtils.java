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

package com.massivedisaster.adal.sample.feature.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.massivedisaster.adal.fragment.BaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.utils.AppUtils;
import com.massivedisaster.adal.utils.SnackBuilder;

public class FragmentUtils extends BaseFragment {

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
                AppUtils.openDial(getActivity(), "00351910000000");
            }
        });

        findViewById(R.id.btnOpenEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.openEmail(getActivity(), getString(R.string.send_email), "teste@teste.com", "teste2@teste.com");
            }
        });
    }
}
