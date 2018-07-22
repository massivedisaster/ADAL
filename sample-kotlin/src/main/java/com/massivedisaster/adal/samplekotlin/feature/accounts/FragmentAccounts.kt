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

package com.massivedisaster.adal.samplekotlin.feature.accounts

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.massivedisaster.adal.account.AccountHelper
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.samplekotlin.R
import com.massivedisaster.adal.utils.SnackBuilder

class FragmentAccounts : BaseFragment() {

    private var mBtnGetAccount: Button? = null
    private var mBtnAddHardCodedAccount: Button? = null
    private var mBtnClearAccount: Button? = null

    override fun getFromBundle(bundle: Bundle) {
        // Intended.
    }

    override fun layoutToInflate(): Int = R.layout.fragment_accounts

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        // Intended.
    }

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_accounts)

        mBtnGetAccount = findViewById(R.id.btnGetAccount)
        mBtnAddHardCodedAccount = findViewById(R.id.btnAddHardCodedAccount)
        mBtnClearAccount = findViewById(R.id.btnClearAccount)

        initialize()
    }

    private fun initialize() {
        AccountHelper.initialize(activity)

        mBtnGetAccount!!.setOnClickListener { getAccount() }

        mBtnAddHardCodedAccount!!.setOnClickListener({ addAccount() })

        mBtnClearAccount!!.setOnClickListener({ clearAccount() })
    }

    private fun clearAccount() {
        AccountHelper.clearAccounts(context) { SnackBuilder.show(mBtnGetAccount, "Clear account finished.", R.color.colorAccent) }
    }

    private fun addAccount() {
        AccountHelper.addAccount(context, "hardcoded_name", "hardcoded_password", "hardcoded_token")
        SnackBuilder.show(mBtnGetAccount, "Added account.", R.color.colorAccent)
    }

    private fun getAccount() {
        val account = AccountHelper.getCurrentAccount(context)
        if (account != null) {
            Toast.makeText(context, "Name: " + account.name + " \nPassword: " + AccountHelper.getAccountPassword(account) + " \ntoken: " + AccountHelper.getCurrentToken(account, context), Toast.LENGTH_LONG).show()
        } else {
            SnackBuilder.show(mBtnGetAccount, "No accounts", R.color.colorAccent)
        }
    }
}