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

package com.massivedisaster.adal.tests.unit.suite.accounts;

import android.accounts.Account;

import com.massivedisaster.adal.account.AccountHelper;
import com.massivedisaster.adal.tests.unit.suite.base.AbstractBaseTestSuite;
import com.massivedisaster.adal.tests.utils.Constants;

import org.junit.Test;

import static android.os.SystemClock.sleep;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * <b>AccountHelperTests class</b>
 * <p>
 * <p>Test suite to evaluate AccountHelper class methods and behaviours</p>
 * <p>
 * <b>Implemented tests:</b>
 * <p>
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
    /*@Test
    public void testClearAccounts() {
        sleep(Constants.BASE_DELAY_SMALL);

        testAddAccount();

        final Context context = getContext();
        AccountHelper.clearAccounts(context, new AccountHelper.OnAccountListener() {
            @Override
            public void onFinished() {
                assertFalse(AccountHelper.hasAccount(context));
            }
        });
    }*/

    /**
     * <p>Adds an account, retrieves the account name stored and test if the inserted account and
     * requested match</p>
     */
    /*@Test
    public void testRetrieveCurrentAccountName() {
        sleep(Constants.BASE_DELAY_SMALL);

        testAddAccount();

        Account account = AccountHelper.getCurrentAccount(getContext());

        String accountName = account.name;

        assertEquals(sAccountName, accountName);
    }*/

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
