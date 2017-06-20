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

package com.massivedisaster.adal.account;

import android.content.Context;

/**
 * Listener to check when an a account is added.
 */
public class OnAddAccountListener implements AccountHelper.OnAccountListener {

    private final String mName;
    private final Context mContext;
    private final String mPassword;
    private final String mToken;

    /**
     * Add a new account to the account manager
     *
     * @param context  The application context.
     * @param name     The account name.
     * @param password The account password.
     * @param token    The account token.
     */
    public OnAddAccountListener(String name, Context context, String password, String token) {
        mName = name;
        mContext = context;
        mPassword = password;
        mToken = token;
    }

    @Override
    public void onFinished() {
        AccountHelper.onFinished(mName, mContext, mPassword, mToken);
    }
}
