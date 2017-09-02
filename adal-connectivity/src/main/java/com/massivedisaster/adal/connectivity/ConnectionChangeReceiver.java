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

package com.massivedisaster.adal.connectivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Abstract class meant to offer connectivity change detection capabilities,
 * using the Android system's connection change system filter.
 */
@SuppressWarnings("unused") public abstract class ConnectionChangeReceiver extends BroadcastReceiver {

    public static final String CONNECTIVITY_CHANGE_FILTER = "android.net.conn.CONNECTIVITY_CHANGE";

    private Context mContext;

    /**
     * Procedure meant to handle this {@link BroadcastReceiver}'s registration to any {@link Activity} instance.
     * @param context the application's current {@link Context}.
     */
    public void registerConnectionChangeReceiver(final Context context) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_CHANGE_FILTER);
        mContext = context;
        mContext.registerReceiver(this, filter);
        Log.d(NetworkConstants.LOG_TAG, NetworkConstants.CONNECTION_CHANGE_REGISTER_OK);
    }

    /** Procedure meant to handle this {@link BroadcastReceiver}'s unregistration from any {@link Activity} instance. */
    public void unregisterConnectionChangeReceiver() {
        if (mContext != null) {
            mContext.unregisterReceiver(this);
            Log.d(NetworkConstants.LOG_TAG, NetworkConstants.CONNECTION_CHANGE_UNREGISTER_OK);
        } else {
            Log.d(NetworkConstants.LOG_TAG, NetworkConstants.INVALID_CONTEXT_INSTANCE
                    + NetworkConstants.CONNECTION_CHANGE_UNREGISTER_NOK);
        }
    }

}

