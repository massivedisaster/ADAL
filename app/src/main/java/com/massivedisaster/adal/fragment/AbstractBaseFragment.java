package com.massivedisaster.adal.fragment;

import android.support.v4.app.Fragment;

import com.massivedisaster.adal.util.KeyboardUtils;

public abstract class AbstractBaseFragment extends Fragment {

    @Override
    public void onPause() {
        KeyboardUtils.hide(getActivity(), getView());

        super.onPause();
    }
}
