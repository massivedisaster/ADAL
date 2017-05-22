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

package com.massivedisaster.adal.tests.unit.suite.accounts;

import android.accounts.Account;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.massivedisaster.adal.account.AccountHelper;
import com.massivedisaster.adal.tests.util.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AccountHelperTest {

    private Context mContext;

    @Before
    public void initialize() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        mContext = instrumentation.getTargetContext().getApplicationContext();
        AccountHelper.initialize(mContext);
    }

    @Test
    public void testAddAccount() {
        AccountHelper.addAccount(
                mContext,
                Constants.ACCOUNT_NAME,
                Constants.ACCOUNT_PASSWORD,
                Constants.ACCOUNT_TOKEN);

        assertTrue(AccountHelper.hasAccount(mContext));
    }

    @Test
    public void testClearAccounts() {
        testAddAccount();

        AccountHelper.clearAccounts(mContext, new AccountHelper.OnAccountListener() {
            @Override
            public void onFinished() {
                assertFalse(AccountHelper.hasAccount(mContext));
            }
        });
    }

    @Test
    public void testRetrieveCurrentAccount() {
        testAddAccount();

        Account account = AccountHelper.getCurrentAccount(mContext);

        String accountName = account.name;

        assertEquals(Constants.ACCOUNT_NAME, accountName);
    }

    @Test
    public void testRetrieveCurrentPassword() {
        testAddAccount();

        Account account = AccountHelper.getCurrentAccount(mContext);

        String accountPassword = AccountHelper.getAccountPassword(account);

        assertEquals(Constants.ACCOUNT_PASSWORD, accountPassword);
    }

    @Test
    public void testRetrieveCurrentToken() {
        testAddAccount();

        String accountToken = AccountHelper.getCurrentToken(mContext);

        assertEquals(Constants.ACCOUNT_TOKEN, accountToken);
    }

    @After
    public void dispose() {
        AccountHelper.clearAccounts(mContext, new AccountHelper.OnAccountListener() {
            @Override
            public void onFinished() {
                mContext = null;
            }
        });
    }

}
