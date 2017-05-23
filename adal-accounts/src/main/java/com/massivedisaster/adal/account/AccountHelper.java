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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

public class AccountHelper {

    private static AccountManager mManager;
    private static int deletedAccounts;

    /**
     * Initialize the AccountHelper
     *
     * @param context The application context
     */
    public static void initialize(Context context) {
        mManager = AccountManager.get(context);
    }

    /**
     * Add a new account to the account manager
     *
     * @param context  The application context.
     * @param name     The account name.
     * @param password The account password.
     * @param token    The account token.
     */
    public static void addAccount(final Context context, final String name, final String password, final String token) {
        validateAccountManager();
        clearAccounts(context, new OnAccountListener() {
            @SuppressWarnings("MissingPermission")
            @Override
            public void onFinished() {
                Account account = new Account(name, context.getPackageName());

                mManager.addAccountExplicitly(account, password, null);
                mManager.setAuthToken(account, context.getPackageName(), token);
            }
        });
    }

    @SuppressWarnings("deprecation")
    public static synchronized void clearAccounts(Context context, final OnAccountListener onAccountListener) {
        validateAccountManager();
        deletedAccounts = 0;

        final Account[] accounts = mManager.getAccountsByType(context.getPackageName());

        if (accounts.length == 0) {
            if (onAccountListener != null) {
                onAccountListener.onFinished();
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            for (Account acc : accounts) {
                mManager.removeAccountExplicitly(acc);
            }

            if (onAccountListener != null) {
                onAccountListener.onFinished();
            }
        } else {
            for (Account account : accounts) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    AccountManagerCallback<Bundle> callback = new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            if (onAccountListener != null && ++deletedAccounts == accounts.length) {
                                onAccountListener.onFinished();
                            }
                        }
                    };
                    mManager.removeAccount(account, null, callback, null);
                } else {
                    AccountManagerCallback<Boolean> callback = new AccountManagerCallback<Boolean>() {
                        @Override
                        public void run(AccountManagerFuture<Boolean> future) {
                            if (onAccountListener != null && ++deletedAccounts == accounts.length) {
                                onAccountListener.onFinished();
                            }
                        }
                    };
                    mManager.removeAccount(account, callback, null);
                }
            }
        }
    }

    /**
     * Verify if the application has account added.
     *
     * @param context The application context.
     * @return True if the application has a account added.
     */
    @SuppressWarnings("MissingPermission")
    public static boolean hasAccount(Context context) {
        validateAccountManager();
        return mManager.getAccountsByType(context.getPackageName()).length > 0;
    }

    /**
     * Verify if the application  have a account and retrieve the account
     *
     * @param context The application context.
     * @return The current account.
     */
    @SuppressWarnings("MissingPermission")
    public static Account getCurrentAccount(Context context) {
        validateAccountManager();
        return mManager.getAccountsByType(context.getPackageName())[0];
    }

    /**
     * Verify if the application have an account and retrieve the account password
     *
     * @param account The account.
     * @return The user password.
     */
    @SuppressWarnings("MissingPermission")
    public static String getAccountPassword(Account account) {
        validateAccountManager();
        return mManager.getPassword(account);
    }

    /**
     * Verify if the application have an account and retrieve the account token
     *
     * @param context The application context.
     * @return The account token.
     */
    @SuppressWarnings("MissingPermission")
    public static String getCurrentToken(Context context) {
        validateAccountManager();
        Account account = mManager.getAccountsByType(context.getPackageName())[0];
        return mManager.peekAuthToken(account, context.getPackageName());
    }

    private static void validateAccountManager() {
        if (mManager == null) {
            throw new NullPointerException("It's necessary to call AccountHelper.initialize first");
        }
    }

    public interface OnAccountListener {

        void onFinished();

    }
}
