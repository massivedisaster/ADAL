package com.massivedisaster.adal.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.massivedisaster.adal.util.KeyboardUtils;

public abstract class AbstractBaseFragment extends Fragment {

    private static final int sInvalidResourceId = -1;

    /**
     * <p>Used to specify fragment layout</p>
     * @return Layout ID
     */
    protected @LayoutRes int layoutToInflate() {
        return sInvalidResourceId;
    }

    /**
     * <p>Restores last instance state of the fragment, this method is always empty
     * and going to restore data only when it's override</p>
     * @param savedInstanceState: Last instance state saved from this fragment
     */
    protected void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        //In case you're gonna use this method it could be override without calling super!
    }

    /**
     * <p>This method is called when the view is already created and is available to inflate
     * children views</p>
     */
    protected void doOnCreated() {
        //In case you're gonna use this method it could be override without calling super!
    }

    /**
     * <p>Request a view by id in case is there any root view inflated
     * otherwise throws a null pointer exception</p>
     * @param viewId: Id of the requested view (child)
     * @param <T>: Type of the requested view
     * @return view requested if it exists
     */
    protected <T extends View> T findViewById(@IdRes int viewId) {
        View view = getView();
        if(view == null) {
            throw new NullPointerException("You must inflate the root view before request their children!");
        }
        return (T)view.findViewById(viewId);
    }

    /**
     * <p>Request a view by id on activity sight in case is there any activity
     * otherwise throws a null pointer exception</p>
     * @param viewId: Id of the requested view (child)
     * @param <T>: Type of the requested view
     * @return view requested if it exists
     */
    protected <T extends View> T findViewByIdOnActivity(@IdRes int viewId) {
        Activity activity = getActivity();
        if(activity == null) {
            throw new NullPointerException("Fragment is not attached to the activity yet!");
        }
        return (T)activity.findViewById(viewId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;

        if(layoutToInflate() != sInvalidResourceId) {
            view = inflater.inflate(layoutToInflate(), container, false);
        }

        restoreInstanceState(savedInstanceState);
        doOnCreated();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        KeyboardUtils.hide(getActivity(), getView());
        super.onPause();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //Check if the superclass already created the animation
        Animation anim = super.onCreateAnimation(transit, enter, nextAnim);

        if (anim == null && nextAnim != 0) {
            anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }

        if (anim != null) {
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }

        return anim;
    }
}
