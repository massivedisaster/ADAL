/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2018 ADAL
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

package com.massivedisaster.adal.tests.unit.suite.accounts

import com.massivedisaster.adal.account.AccountHelper
import com.massivedisaster.adal.tests.unit.suite.base.AbstractBaseTestSuite
import com.massivedisaster.adal.tests.utils.Constants
import org.junit.Assert.*
import org.junit.Test
import java.lang.Thread.sleep

class AccountHelperTests : AbstractBaseTestSuite() {

    private val sAccountName = "Account Name"
    private val sAccountPassword = "Account Password"
    private val sAccountToken = "Account Token"

    override fun setup() {
        AccountHelper.initialize(context)
    }

    public override fun dispose() {
        clearAccounts()
    }

    /**
     *
     * Adds an account and test if there's an account added
     */
    @Test
    fun testAddAccount() {
        clearAccounts()
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        AccountHelper.addAccount(context, sAccountName, sAccountPassword, sAccountToken)

        assertTrue(AccountHelper.getCurrentAccount(context) != null)
    }

    /**
     *
     * Adds an account and clear all accounts afterwards and test if there's no account
     * associated
     */
    @Test
    fun testClearAccounts() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        testAddAccount()

        val context = context
        AccountHelper.clearAccounts(context) { assertNull(AccountHelper.getCurrentAccount(context)) }

        sleep(Constants.BASE_DELAY_SMALL.toLong())
    }

    /**
     *
     * Adds an account, retrieves the account name stored and test if the inserted account and
     * requested match
     */
    @Test
    fun testRetrieveCurrentAccountName() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        testAddAccount()

        val account = AccountHelper.getCurrentAccount(context)

        assertNotNull(account)

        val accountName = account!!.name

        assertEquals(sAccountName, accountName)
    }

    /**
     *
     * Adds an account, retrieves the account password stored and test if the inserted account
     * and requested match
     */
    @Test
    fun testRetrieveCurrentPassword() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        testAddAccount()

        val account = AccountHelper.getCurrentAccount(context)

        val accountPassword = AccountHelper.getAccountPassword(account)

        assertEquals(sAccountPassword, accountPassword)
    }

    /**
     *
     * Adds an account, retrieves the account token stored and test if the inserted account and
     * requested match
     */
    @Test
    fun testRetrieveCurrentToken() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        testAddAccount()

        val account = AccountHelper.getCurrentAccount(context)

        val accountToken = AccountHelper.getCurrentToken(account!!, context)

        assertEquals(sAccountToken, accountToken)
    }

    /**
     *
     * Clears all accounts stored
     */
    private fun clearAccounts() {
        AccountHelper.clearAccounts(context) { }
    }
}