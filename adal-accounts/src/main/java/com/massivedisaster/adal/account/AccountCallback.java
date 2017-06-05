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

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;

/**
 * Callback for accounts.
 * @param <T> The result
 */
public class AccountCallback<T> implements AccountManagerCallback<T> {

    private final int mDeletedAccounts;
    private final AccountHelper.OnAccountListener mOnAccountListener;
    private final int mAccountsLength;

    /**
     * Constructor of Callback.
     *
     * @param deletedAccounts number of deleted accounts.
     * @param onAccountListener The listener to call when finished.
     * @param accountsLength The number of accounts.
     */
    public AccountCallback(int deletedAccounts, AccountHelper.OnAccountListener onAccountListener, int accountsLength) {
        mDeletedAccounts = deletedAccounts;
        mOnAccountListener = onAccountListener;
        mAccountsLength = accountsLength;
    }

    @Override
    public void run(AccountManagerFuture<T> future) {
        if (mOnAccountListener != null && mDeletedAccounts == mAccountsLength) {
            mOnAccountListener.onFinished();
        }
    }
}
