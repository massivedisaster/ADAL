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

package com.massivedisaster.adal.sample.feature.accounts;

import android.accounts.Account;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.massivedisaster.adal.account.AccountHelper;
import com.massivedisaster.adal.fragment.BaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.utils.SnackBuilder;

public class FragmentAccounts extends BaseFragment {

    private Button mBtnGetAccount, mBtnAddHardCodedAccount, mBtnClearAccount;

    @Override
    protected void getFromBundle(Bundle bundle) {
        // Intended.
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_accounts;
    }

    @Override
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Intended.
    }

    @Override
    protected void doOnCreated() {
        getActivity().setTitle(R.string.sample_accounts);

        mBtnGetAccount = findViewById(R.id.btnGetAccount);
        mBtnAddHardCodedAccount = findViewById(R.id.btnAddHardCodedAccount);
        mBtnClearAccount = findViewById(R.id.btnClearAccount);

        initialize();
    }

    public void initialize() {

        AccountHelper.initialize(getActivity());

        mBtnGetAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccount();
            }
        });

        mBtnAddHardCodedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });

        mBtnClearAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAccount();
            }
        });
    }

    private void clearAccount() {
        AccountHelper.clearAccounts(getContext(), new AccountHelper.OnAccountListener() {
            @Override
            public void onFinished() {
                SnackBuilder.show(mBtnGetAccount, "Clear account finished.", R.color.colorAccent);
            }
        });
    }

    private void addAccount() {
        AccountHelper.addAccount(getContext(), "hardcoded_name", "hardcoded_password", "hardcoded_token");
        SnackBuilder.show(mBtnGetAccount, "Added account.", R.color.colorAccent);
    }

    private void getAccount() {
        Account account = AccountHelper.getCurrentAccount(getContext());
        if (account != null) {
            Toast.makeText(getContext(), "Name: " + account.name + " \nPassword: " + AccountHelper.getAccountPassword(account) + " \ntoken: " + AccountHelper.getCurrentToken(account, getContext()), Toast.LENGTH_LONG).show();
        } else {
            SnackBuilder.show(mBtnGetAccount, "No accounts", R.color.colorAccent);
        }
    }
}
