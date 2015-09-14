package org.fingerlinks.mobile.android.navigator.builder.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.fingerlinks.mobile.android.navigator.Navigator;
import org.fingerlinks.mobile.android.navigator.NavigatorBean;
import org.fingerlinks.mobile.android.navigator.NavigatorException;
import org.fingerlinks.mobile.android.navigator.NavigatorHelper;
import org.fingerlinks.mobile.android.navigator.builder.Builders;
import org.fingerlinks.mobile.android.navigator.utils.ContextReference;

/**
 * Created by fabio on 07/09/15.
 */
public class GoToBuilder extends BaseBuilder implements Builders.Any.G {

    public GoToBuilder(ContextReference context, NavigatorBean navigatorBean) throws NavigatorException {
        super(context, navigatorBean);
    }

    @Override
    public GoToBuilder animation() {
        if(mNavigatorBean == null) {
            throw new NavigatorException("NavBean not initialized");
        }
        mNavigatorBean.setAnimation(true);
        return this;
    }

    @Override
    public GoToBuilder animation(int enter, int exit) {
        int[] animations = new int[2];
        animations[0] = enter;
        animations[1] = exit;
        mNavigatorBean.setAnimations(animations);
        return this;
    }

    @Override
    public Builders.Any.A goTo(Class<?> activity) {
        goTo(activity, null);
        return new ActivityBuilder(mContextReference, mNavigatorBean);
    }

    @Override
    public Builders.Any.A goTo(Class<?> activity, Bundle bundle) {
        Intent intent = new Intent(mContextReference.getContext(), activity);
        if (bundle != null) {
            intent.putExtra(BUNDLE, bundle);
        }
        mNavigatorBean.setIntent(intent);
        //listStep.add(activity.getName());
    return new ActivityBuilder(mContextReference, mNavigatorBean);
    }

    @Override
    public Builders.Any.F goTo(Fragment fragment, int container) {
        goTo(fragment, null, container);
        return new FragmentBuilder(mContextReference, mNavigatorBean);
    }

    @Override
    public Builders.Any.F goTo(Fragment fragment, Bundle bundle, int container) {
        FragmentManager fragmentManager = ((FragmentActivity) mContextReference.getContext()).getSupportFragmentManager();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        mNavigatorBean.setFragment(fragment);
        mNavigatorBean.setFragmentManager(fragmentManager);
        mNavigatorBean.setContainer(container);
    return new FragmentBuilder(mContextReference, mNavigatorBean);
    }

    public final static String BUNDLE = Navigator.class.getName()+".bundle";
};