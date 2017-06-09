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
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

/**
 * Utility class for accounts manager.
 */
public final class AccountHelper {

    private static AccountManager sManager;
    private static int sDeletedAccounts;
    private static final String MISSING_PERMISSION = "MissingPermission";

    /**
     * private constructor.
     */
    private AccountHelper() { }

    /**
     * Initialize the AccountHelper
     *
     * @param context The application context
     */
    public static void initialize(Context context) {
        sManager = AccountManager.get(context);
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
        clearAccounts(context, new OnAddAccountListener(name, context, password, token));
    }

    /**
     * Add a new account to the account manager
     *
     * @param context  The application context.
     * @param name     The account name.
     * @param password The account password.
     * @param token    The account token.
     */
    @SuppressWarnings(MISSING_PERMISSION)
    public static void onFinished(String name, Context context, String password, String token) {
        Account account = new Account(name, context.getPackageName());

        sManager.addAccountExplicitly(account, password, null);
        sManager.setAuthToken(account, context.getPackageName(), token);
    }

    /**
     * Remove accounts from manager.
     *
     * @param context The application context.
     * @param onAccountListener The listener to be called when process finished.
     */
    public static void clearAccounts(Context context, final OnAccountListener onAccountListener) {
        synchronized (AccountHelper.class) {
            validateAccountManager();
            sDeletedAccounts = 0;

            final Account[] accounts = sManager.getAccountsByType(context.getPackageName());

            if (accounts.length == 0) {
                if (onAccountListener != null) {
                    onAccountListener.onFinished();
                }
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                for (Account acc : accounts) {
                    sManager.removeAccountExplicitly(acc);
                }

                if (onAccountListener != null) {
                    onAccountListener.onFinished();
                }
            } else {
                removeAccounts(onAccountListener, accounts);
            }
        }
    }

    /**
     * Remove accounts from manager.
     *
     * @param onAccountListener The listener to be called when process finished.
     * @param accounts The array of accounts to be removed.
     */
    @SuppressWarnings("deprecation")
    private static void removeAccounts(final OnAccountListener onAccountListener, final Account... accounts) {
        for (Account account : accounts) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                AccountManagerCallback<Bundle> callback = new AccountCallback<>(++sDeletedAccounts, onAccountListener, accounts.length);
                sManager.removeAccount(account, null, callback, null);
            } else {
                AccountManagerCallback<Boolean> callback = new AccountCallback<>(++sDeletedAccounts, onAccountListener, accounts.length);
                sManager.removeAccount(account, callback, null);
            }
        }
    }

    /**
     * Verify if the application has account added.
     *
     * @param context The application context.
     * @return True if the application has a account added.
     */
    @SuppressWarnings(MISSING_PERMISSION)
    public static boolean hasAccount(Context context) {
        validateAccountManager();
        return sManager.getAccountsByType(context.getPackageName()).length > 0;
    }

    /**
     * Verify if the application  have a account and retrieve the account
     *
     * @param context The application context.
     * @return The current account.
     */
    @SuppressWarnings(MISSING_PERMISSION)
    public static Account getCurrentAccount(Context context) {
        validateAccountManager();
        return sManager.getAccountsByType(context.getPackageName())[0];
    }

    /**
     * Verify if the application have an account and retrieve the account password
     *
     * @param account The account.
     * @return The user password.
     */
    @SuppressWarnings(MISSING_PERMISSION)
    public static String getAccountPassword(Account account) {
        validateAccountManager();
        return sManager.getPassword(account);
    }

    /**
     * Verify if the application have an account and retrieve the account token
     *
     * @param context The application context.
     * @return The account token.
     */
    @SuppressWarnings(MISSING_PERMISSION)
    public static String getCurrentToken(Context context) {
        validateAccountManager();
        Account account = sManager.getAccountsByType(context.getPackageName())[0];
        return sManager.peekAuthToken(account, context.getPackageName());
    }

    /**
     * Verify if manager was initialized.
     *
     * @throws ExceptionInInitializerError if <var>sManager</var> is null
     */
    private static void validateAccountManager() {
        if (sManager == null) {
            throw new ExceptionInInitializerError("It's necessary to call AccountHelper.initialize first");
        }
    }

    /**
     * Listener for modifications in manager.
     * @see #clearAccounts(Context, OnAccountListener)
     */
    public interface OnAccountListener {

        /**
         * Called when finish modification to manager
         */
        void onFinished();

    }
}
