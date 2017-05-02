package com.massivedisaster.adal.sample.feature.home;

import android.support.v4.app.Fragment;

import com.massivedisaster.adal.adapter.AbstractBaseAdapter;
import com.massivedisaster.adal.adapter.BaseViewHolder;
import com.massivedisaster.adal.sample.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ADAL by Carbon by BOLD
 * Created in 4/27/17 by the following authors:
 * Jorge Costa - {jorgecosta@carbonbybold.com}
 */
public class ExampleAdapter extends AbstractBaseAdapter<Class<? extends Fragment>> {

    private HashMap<Class<? extends Fragment>, String> mLstItems;

    public ExampleAdapter(HashMap<Class<? extends Fragment>, String> lstItems) {
        super(R.layout.adapter_example, new ArrayList<>(lstItems.keySet()));
        mLstItems = lstItems;
    }

    @Override
    protected void bindItem(BaseViewHolder holder, Class<? extends Fragment> item) {
        holder.setText(R.id.txtName, mLstItems.get(item));
    }

    @Override
    protected void bindError(BaseViewHolder holder, boolean loadingError) {

    }
}
