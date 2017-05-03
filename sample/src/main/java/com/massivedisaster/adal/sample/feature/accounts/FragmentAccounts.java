package com.massivedisaster.adal.sample.feature.accounts;

import android.accounts.Account;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.massivedisaster.adal.account.AccountHelper;
import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.utils.SnackBuilder;

public class FragmentAccounts extends AbstractBaseFragment {

    private Button mBtnGetAccount, mBtnAddHardCodedAccount, mBtnClearAccount;

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_accounts;
    }

    @Override
    protected void doOnCreated() {
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
        if (AccountHelper.hasAccount(getContext())) {
            Account account = AccountHelper.getCurrentAccount(getContext());
            Toast.makeText(getContext(), "Name: " + account.name + " \nPassword: " + AccountHelper.getAccountPassword(account) + " \ntoken: " + AccountHelper.getCurrentToken(getContext()), Toast.LENGTH_LONG).show();
        } else {
            SnackBuilder.show(mBtnGetAccount, "No accounts", R.color.colorAccent);
        }
    }
}
