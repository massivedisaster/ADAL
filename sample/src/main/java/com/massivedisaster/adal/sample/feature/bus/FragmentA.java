package com.massivedisaster.adal.sample.feature.bus;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.massivedisaster.activitymanager.ActivityFragmentManager;
import com.massivedisaster.adal.bus.BangBus;
import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.base.activity.ActivityToolbar;

public class FragmentA extends AbstractBaseFragment {

    public static final String BANG_A = "BANG_A";

    private BangBus mBangBus;
    private Button mBtnOpenB;
    private TextView mTxtResult;

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_a;
    }

    @Override
    protected void doOnCreated() {
        mBtnOpenB = findViewById(R.id.btnOpenB);
        mTxtResult = findViewById(R.id.txtResult);

        initialize();
    }

    public void initialize() {
        mBangBus = new BangBus(getActivity());
        mBangBus.subscribe(this);

        mBtnOpenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFragmentManager.open(getActivity(), ActivityToolbar.class, FragmentB.class);
            }
        });
    }

    @BangBus.SubscribeBang(name = BANG_A)
    public void showTripsSearch(String message) {
        mTxtResult.setText(message);
    }

    @Override
    public void onDestroy() {
        mBangBus.unsubscribe();
        super.onDestroy();
    }
}
