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

package com.massivedisaster.adal.sample.feature.connectivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.massivedisaster.adal.connectivity.ConnectionChangeReceiver;
import com.massivedisaster.adal.connectivity.NetworkUtils;
import com.massivedisaster.adal.fragment.BaseFragment;
import com.massivedisaster.adal.sample.R;

/**
 * Connectivity Change Fragment meant to test the {@link ConnectionChangeReceiver} for connectivity changes.
 */
public class FragmentConnectivityAware extends BaseFragment implements View.OnClickListener {

    private TextView mTxtMessage;
    private Button mCheckConnectivityButton;

    /**
     * Network verification.
     */
    private ConnectionChangeReceiver mConnectionReceiver = new ConnectionChangeReceiver() {
        @Override public void onReceive(@NonNull final Context context, @NonNull final Intent intent) {
            if (ConnectionChangeReceiver.CONNECTIVITY_CHANGE_FILTER.equals(intent.getAction())) {
                checkConnectivity();
            }
        }
    };

    @Override protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    @Override protected int layoutToInflate() {
        return R.layout.fragment_connectivity_aware;
    }

    @Override protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Intended.
    }

    @Override protected void doOnCreated() {
        getActivity().setTitle(R.string.sample_network);
        mTxtMessage = findViewById(R.id.txtMessage);
        mCheckConnectivityButton = findViewById(R.id.btnCheckConnectivity);
    }

    /**
     * Called when FragmentConnectivityAware is about to become visible.
     */
    @Override public void onStart() {
        super.onStart();
        mConnectionReceiver.registerConnectionChangeReceiver(getActivity());
        mCheckConnectivityButton.setOnClickListener(this);
    }

    /**
     * Called when the FragmentConnectivityAware has become visible.
     */
    @Override public void onResume() {
        super.onResume();
        checkConnectivity();
    }

    /**
     * Called just before the FragmentConnectivityAware is destroyed.
     */
    @Override public void onDestroy() {
        if (mConnectionReceiver != null) {
            mConnectionReceiver.unregisterConnectionChangeReceiver();
        }
        super.onDestroy();
    }

    /**
     * Called when a view has been clicked.
     * @param view the clicked {@link View}.
     */
    @Override public void onClick(View view) {
        if (R.id.btnCheckConnectivity == view.getId()) {
            checkConnectivity();
        }
    }

    /**
     * Check whether the device has Internet connectivity or not.
     */
    private void checkConnectivity() {
        if (isVisible()) {
            final boolean isOnline = NetworkUtils.isNetworkConnected(getActivity());
            handleConnectivityStatusChange(isOnline);
        }
    }

    /**
     * Log the device Internet connectivity status.
     *
     * @param isOnline boolean value indicating whether the device has a connection established or not.
     */
    private void handleConnectivityStatusChange(final boolean isOnline) {
        final String deviceConnectivity = isOnline ? getString(R.string.connectivity_device_online)
                : getString(R.string.connectivity_device_offline);
        mTxtMessage.setText(deviceConnectivity);
        Toast.makeText(getActivity(), deviceConnectivity, Toast.LENGTH_SHORT).show();
    }

}
