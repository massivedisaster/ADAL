package com.massivedisaster.adal.sample.feature.home;

import android.support.v4.app.Fragment;

import com.massivedisaster.adal.adapter.AbstractBaseAdapter;
import com.massivedisaster.adal.adapter.BaseViewHolder;
import com.massivedisaster.adal.sample.R;

import java.util.List;

/**
 * ADAL by Carbon by BOLD
 * Created in 4/27/17 by the following authors:
 * Jorge Costa - {jorgecosta@carbonbybold.com}
 */
public class ExampleAdapter extends AbstractBaseAdapter<Class<? extends Fragment>> {

    public ExampleAdapter(List<Class<? extends Fragment>> lstItems) {
        super(R.layout.adapter_example, lstItems);
    }

    @Override
    protected void bindItem(BaseViewHolder holder, Class<? extends Fragment> item) {
        holder.setText(R.id.txtName, item.getSimpleName());
    }

    @Override
    protected void bindError(BaseViewHolder holder, boolean loadingError) {

    }
}
