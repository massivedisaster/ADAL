package com.massivedisaster.adal.fragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public abstract class AbstractRequestFragment extends AbstractBaseFragment {

    private List<Call<?>> mLstCallbacks = new ArrayList<>();

    public <U> Call<U> addRequest(Call<U> call) {
        mLstCallbacks.add(call);

        return call;
    }

    public void cancelAllRequests() {
        for (Call<?> c : mLstCallbacks) {
            c.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelAllRequests();
    }
}
