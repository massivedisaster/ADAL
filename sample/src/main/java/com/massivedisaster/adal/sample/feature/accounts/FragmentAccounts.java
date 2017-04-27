package com.massivedisaster.adal.sample.feature.accounts;

import android.accounts.Account;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.massivedisaster.adal.account.AccountHelper;
import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.utils.SnackBuilder;

public class FragmentAccounts extends AbstractBaseFragment {

    private Button mBtnGetAccount, mbtnAddHardCodedAccount, mbtnClearAccount;

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_accounts;
    }

    @Override
    protected void doOnCreated() {
        mBtnGetAccount = findViewById(R.id.btnGetAccount);
        mbtnAddHardCodedAccount = findViewById(R.id.btnAddHardCodedAccount);
        mbtnClearAccount = findViewById(R.id.btnClearAccount);

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

        mbtnAddHardCodedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });

        mbtnClearAccount.setOnClickListener(new View.OnClickListener() {
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
            SnackBuilder.show(mBtnGetAccount, "Name: " + account.name + " Password: " + AccountHelper.getAccountPassword(account) +" token: " + AccountHelper.getCurrentToken(getContext()), R.color.colorAccent);

        } else {
            SnackBuilder.show(mBtnGetAccount, "No accounts", R.color.colorAccent);
        }
    }
}
