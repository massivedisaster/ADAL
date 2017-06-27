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
