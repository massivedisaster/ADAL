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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Utility class for accounts manager.
 */
public final class AccountHelper {

    private static final String MISSING_PERMISSION = "MissingPermission";
    private static AccountManager sManager;
    private static int sDeletedAccounts;

    /**
     * private constructor.
     */
    private AccountHelper() {
    }

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
    public static void onFinished(String name, Context context, String password, String token) {
        Account account = new Account(name, context.getPackageName());

        sManager.addAccountExplicitly(account, password, null);
        sManager.setAuthToken(account, context.getPackageName(), token);
    }

    /**
     * Remove accounts from manager.
     *
     * @param context           The application context.
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
     * @param accounts          The array of accounts to be removed.
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
     * Verify if the application have a account and retrieve the account.
     *
     * @param context The application context.
     * @return The current account or null if there is no account.
     */
    @Nullable
    public static Account getCurrentAccount(Context context) {
        validateAccountManager();

        final Account[] accounts = sManager.getAccountsByType(context.getPackageName());

        if (accounts.length > 0) {
            return accounts[0];
        }

        return null;
    }

    /**
     * Verify if the application have an account and retrieve the account password.
     *
     * @param account The account.
     * @return The user password.
     */
    public static String getAccountPassword(Account account) {
        validateAccountManager();
        return sManager.getPassword(account);
    }

    /**
     * Change the account password.
     *
     * @param account  The account.
     * @param password The new password.
     */
    public static void setAccountPassword(Account account, String password) {
        validateAccountManager();
        sManager.setPassword(account, password);
    }

    /**
     * Retrieve the account token.
     *
     * @param account The account account to take the token.
     * @param context The application context.
     * @return The account token.
     */
    @Nullable
    public static String getCurrentToken(@NonNull Account account, Context context) {
        validateAccountManager();
        return sManager.peekAuthToken(account, context.getPackageName());
    }

    /**
     * Retrieve the account token.
     *
     * @param account The account account to take the token.
     * @param context The application context.
     * @param token   The new token for account.
     */
    public static void setCurrentToken(@NonNull Account account, Context context, String token) {
        validateAccountManager();
        sManager.setAuthToken(account, context.getPackageName(), token);
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
     * Method to associate information to the user.
     *
     * @param account The account.
     * @param key     Key of the information.
     * @param value   The information to be associated.
     */
    public static void setUserData(@NonNull Account account, String key, String value) {
        validateAccountManager();
        sManager.setUserData(account, key, value);
    }

    /**
     * Method to get previous associated information to the user.
     *
     * @param account The account.
     * @param key     Key of the information.
     * @return The information associated.
     */
    @Nullable
    public static String getUserData(@NonNull Account account, String key) {
        validateAccountManager();
        return sManager.getUserData(account, key);
    }

    /**
     * Listener for modifications in manager.
     *
     * @see #clearAccounts(Context, OnAccountListener)
     */
    public interface OnAccountListener {

        /**
         * Called when finish modification to manager
         */
        void onFinished();
    }
}
