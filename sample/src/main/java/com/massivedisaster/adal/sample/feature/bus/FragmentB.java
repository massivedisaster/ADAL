package com.massivedisaster.adal.sample.feature.bus;

import android.view.View;
import android.widget.Button;

import com.massivedisaster.adal.bus.BangBus;
import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;

public class FragmentB extends AbstractBaseFragment {

    private BangBus mBangBus;
    private Button mSendBang;

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_b;
    }

    @Override
    protected void doOnCreated() {
        mSendBang = findViewById(R.id.btnSendBang);

        initialize();
    }

    public void initialize() {
        mBangBus = new BangBus(getActivity());
        mBangBus.subscribe(this);

        mSendBang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBangBus.bang(getContext(), FragmentA.BANG_A, "received bang from " + FragmentB.class.getSimpleName());
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        mBangBus.unsubscribe();
        super.onDestroy();
    }
}
