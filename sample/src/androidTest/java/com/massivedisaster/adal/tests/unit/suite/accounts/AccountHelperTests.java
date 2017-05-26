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

import com.massivedisaster.adal.account.AccountHelper;
import com.massivedisaster.adal.tests.unit.suite.base.AbstractBaseTestSuite;
import com.massivedisaster.adal.tests.utils.Constants;

import org.junit.Test;

import static android.os.SystemClock.sleep;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * <b>AccountHelperTests class</b>
 *
 * <p>Test suite to evaluate AccountHelper class methods and behaviours</p>
 *
 * <b>Implemented tests:</b>
 *
 * # <p>({@link #testAddAccount() testAddAccount} method)</p>
 * # <p>({@link #testClearAccounts() testClearAccounts} method)</p>
 * # <p>({@link #testRetrieveCurrentAccountName() testRetrieveCurrentAccountName} method)</p>
 * # <p>({@link #testRetrieveCurrentPassword() testRetrieveCurrentPassword} method)</p>
 * # <p>({@link #testRetrieveCurrentToken() testRetrieveCurrentToken} method)</p>
 */
public class AccountHelperTests extends AbstractBaseTestSuite {

    private static final String sAccountName = "Account Name";
    private static final String sAccountPassword = "Account Password";
    private static final String sAccountToken = "Account Token";

    @Override
    protected void setup() {
        AccountHelper.initialize(getContext());
    }

    @Override
    public void dispose() {
        clearAccounts();
    }

    /**
     * <p>Adds an account and test if there's an account added</p>
     */
    @Test
    public void testAddAccount() {
        clearAccounts();

        AccountHelper.addAccount(getContext(), sAccountName, sAccountPassword, sAccountToken);

        assertTrue(AccountHelper.hasAccount(getContext()));
    }

    /**
     * <p>Adds an account and clear all accounts afterwards and test if there's no account
     * associated</p>
     */
    @Test
    public void testClearAccounts() {
        sleep(Constants.BASE_DELAY_SMALL);

        testAddAccount();

        AccountHelper.clearAccounts(getContext(), new AccountHelper.OnAccountListener() {
            @Override
            public void onFinished() {
                assertFalse(AccountHelper.hasAccount(getContext()));
            }
        });
    }

    /**
     * <p>Adds an account, retrieves the account name stored and test if the inserted account and
     * requested match</p>
     */
    @Test
    public void testRetrieveCurrentAccountName() {
        sleep(Constants.BASE_DELAY_SMALL);

        testAddAccount();

        Account account = AccountHelper.getCurrentAccount(getContext());

        String accountName = account.name;

        assertEquals(sAccountName, accountName);
    }

    /**
     * <p>Adds an account, retrieves the account password stored and test if the inserted account
     * and requested match</p>
     */
    @Test
    public void testRetrieveCurrentPassword() {
        sleep(Constants.BASE_DELAY_SMALL);

        testAddAccount();

        Account account = AccountHelper.getCurrentAccount(getContext());

        String accountPassword = AccountHelper.getAccountPassword(account);

        assertEquals(sAccountPassword, accountPassword);
    }

    /**
     * <p>Adds an account, retrieves the account token stored and test if the inserted account and
     * requested match</p>
     */
    @Test
    public void testRetrieveCurrentToken() {
        sleep(Constants.BASE_DELAY_SMALL);

        testAddAccount();

        String accountToken = AccountHelper.getCurrentToken(getContext());

        assertEquals(sAccountToken, accountToken);
    }

    /**
     * <p>Clears all accounts stored</p>
     */
    private void clearAccounts() {
        AccountHelper.clearAccounts(getContext(), new AccountHelper.OnAccountListener() {
            @Override
            public void onFinished() {

            }
        });
    }

}
