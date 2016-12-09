package com.massivedisaster.adal.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AccountHelper {

    private static AccountManager mManager;
    private static int deletedAccounts;

    public static void initialize(Context context) {
        mManager = AccountManager.get(context);
    }

    public static void addAccount(final Context context, final String name, final String password, final String token) {
        validateAccountManager();
        clearAccounts(context, new OnAccountListener() {
            @SuppressWarnings("MissingPermission")
            @Override
            public void onFinished() {
                Account account = new Account(name, context.getPackageName());
                Intent intent = new Intent();

                mManager.addAccountExplicitly(account, password, null);
                mManager.setAuthToken(account, context.getPackageName(), token);

                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, name);
                intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, context.getPackageName());
                intent.putExtra(AccountManager.KEY_AUTHTOKEN, token);
            }
        });
    }

    @SuppressWarnings("MissingPermission")
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

    @SuppressWarnings("MissingPermission")
    public static boolean hasAccount(Context context) {
        validateAccountManager();
        return mManager.getAccountsByType(context.getPackageName()).length > 0;
    }

    @SuppressWarnings("MissingPermission")
    public static Account getCurrentAccount(Context context) {
        validateAccountManager();
        return mManager.getAccountsByType(context.getPackageName())[0];
    }

    @SuppressWarnings("MissingPermission")
    public static String getAccountPassword(Account account) {
        validateAccountManager();
        return mManager.getPassword(account);
    }

    @SuppressWarnings("MissingPermission")
    public static String getCurrentToken(Context context) {
        validateAccountManager();
        Account account = mManager.getAccountsByType(context.getPackageName())[0];
        return mManager.peekAuthToken(account, context.getPackageName().trim());
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
