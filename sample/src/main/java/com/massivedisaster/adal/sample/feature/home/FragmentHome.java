package com.massivedisaster.adal.sample.feature.home;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.massivedisaster.activitymanager.ActivityFragmentManager;
import com.massivedisaster.adal.adapter.AbstractBaseAdapter;
import com.massivedisaster.adal.fragment.AbstractBaseFragment;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.base.activity.ActivityFullScreen;
import com.massivedisaster.adal.sample.base.activity.ActivityToolbar;
import com.massivedisaster.adal.sample.feature.accounts.FragmentAccounts;
import com.massivedisaster.adal.sample.feature.location.FragmentLocation;
import com.massivedisaster.adal.sample.feature.permissions.FragmentPermissions;

import java.util.ArrayList;
import java.util.List;

/**
 * ADAL by Carbon by BOLD
 * Created in 4/27/17 by the following authors:
 * Jorge Costa - {jorgecosta@carbonbybold.com}
 */
public class FragmentHome extends AbstractBaseFragment {

    private RecyclerView mRclItems;

    @Override
    protected int layoutToInflate() {
        return R.layout.fragment_home;
    }

    @Override
    protected void doOnCreated() {
        mRclItems = findViewById(R.id.rclItems);

        initialize();
    }

    private void initialize() {
        ExampleAdapter adapter = new ExampleAdapter(getExamples());
        adapter.setOnChildClickListener(new AbstractBaseAdapter.OnChildClickListener<Class<? extends Fragment>>() {
            @Override
            public void onChildClick(View view, Class<? extends Fragment> aClass, int position) {
                ActivityFragmentManager.open(getActivity(), ActivityToolbar.class, aClass);
            }
        });

        mRclItems.setLayoutManager(new LinearLayoutManager(getContext()));
        mRclItems.setAdapter(adapter);
    }

    public List<Class<? extends Fragment>> getExamples() {
        return new ArrayList<Class<? extends Fragment>>() {{
            add(FragmentLocation.class);
            add(FragmentPermissions.class);
            add(FragmentAccounts.class);
        }};
    }
}
