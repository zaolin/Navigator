package org.fingerlinks.mobile.android.navigator.builder.impl;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.fingerlinks.mobile.android.navigationhelper.BuildConfig;
import org.fingerlinks.mobile.android.navigator.NavigatorException;
import org.fingerlinks.mobile.android.navigator.builder.Builders;
import org.fingerlinks.mobile.android.navigator.utils.ContextReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fabio on 08/09/15.
 */
public class NavigatorUtils extends BaseBuilder implements Builders.Any.U {

    public NavigatorUtils(ContextReference context) throws NavigatorException {
        super(context, null);
    }

    /**
     *
     * @param message
     */
    public void confirmExitWithMessage(int message) {
        confirmExitWithMessage(
                mContextReference.getContext().getResources().getString(message), -1);
    }

    /**
     *
     * @param message
     */
    public void confirmExitWithMessage(String message) {
        confirmExitWithMessage(
                message, -1);
    }

    /**
     *
     * @param message
     * @param doublePressInterval
     */
    public void confirmExitWithMessage(int message, long doublePressInterval) {
        confirmExitWithMessage(
                mContextReference.getContext().getResources().getString(message),
                doublePressInterval);
    }

    /**
     *
     * @param message
     * @param doublePressInterval
     */
    public void confirmExitWithMessage(String message, long doublePressInterval) {

        if(doublePressInterval == -1) {
            doublePressInterval = DOUBLE_PRESS_INTERVAL;
        }
        Toast.makeText(mContextReference.getContext(), message, Toast.LENGTH_SHORT).show();
        long pressTime = System.currentTimeMillis();
        if ((pressTime - lastPressTime) <= doublePressInterval) {
            lastPressTime = 0;
            ((Activity)mContextReference.getContext()).finish();
        }
        lastPressTime = pressTime;
    }

    public void goToPreviousBackStack() throws NavigatorException {
        FragmentManager fragmentManager = ((FragmentActivity)mContextReference.getContext())
                .getSupportFragmentManager();
        if (canGoBack(fragmentManager)) {
            fragmentManager.popBackStack();
        } else {
            throw new NavigatorException("You don't go back to this point");
        }
    }

    /**
     *
     * @param tag
     * @throws NavigatorException
     */
    public void goBackToSpecificPoint(String tag) throws NavigatorException {

        FragmentManager fragmentManager = ((FragmentActivity)mContextReference.getContext())
                .getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(tag) != null) {
            List<FragmentManager.BackStackEntry> fragmentList = fragmentList();
            Collections.reverse(fragmentList);
            for (int i = 0; i < fragmentList.size(); i++) {
                if (!tag.equalsIgnoreCase(fragmentList.get(i).getName())) {
                    fragmentManager.popBackStack();
                }
                //else {
                //    listStep.add(tag);
                //    return;
                //}
            }
        } else {
            Log.e(TAG, "no fragment found");
            String message = "Fragment with TAG[" + tag + "] not found into backstack entry";
            throw new NavigatorException(message);
        }
    }

    /**
     *
     * @param fragmentManager
     * @return
     */
    public boolean canGoBack(FragmentManager fragmentManager) {
        return canGoBackToSpecificPoint(null, 0, fragmentManager);
    }

    /**
     *
     * @param tag
     * @param container
     * @param fragmentManager
     * @return
     */
    public boolean canGoBackToSpecificPoint(String tag, int container, FragmentManager fragmentManager) {
        if (TextUtils.isEmpty(tag)) {
            return (fragmentManager.getBackStackEntryCount() > 1);
        } else {
            List<FragmentManager.BackStackEntry> fragmentList = fragmentList();
            Fragment fragment = fragmentManager.findFragmentById(container);
            if (fragment != null && tag.equalsIgnoreCase(fragment.getTag())) {
                return false;
            }
            for (int i = 0; i < fragmentList.size(); i++) {
                if (tag.equalsIgnoreCase(fragmentList.get(i).getName())) {
                    return true;
                }
            }
            return false;
        }
    }

    private List<FragmentManager.BackStackEntry> fragmentList() {
        FragmentManager fragmentManager = ((FragmentActivity)mContextReference.getContext()).getSupportFragmentManager();
        List<FragmentManager.BackStackEntry> fragmentList = new ArrayList<>();
        int size = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < size; i++) {
            fragmentList.add(fragmentManager.getBackStackEntryAt(i));
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "position: " + i + " name: " + fragmentManager.getBackStackEntryAt(i).getName());
            }
        }
        return fragmentList;
    }

    private static long lastPressTime = 0;
    private final static String TAG = NavigatorUtils.class.getName();
    private static final long DOUBLE_PRESS_INTERVAL = 5 * 1000;


};