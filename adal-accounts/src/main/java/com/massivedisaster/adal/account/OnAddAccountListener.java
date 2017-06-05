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
