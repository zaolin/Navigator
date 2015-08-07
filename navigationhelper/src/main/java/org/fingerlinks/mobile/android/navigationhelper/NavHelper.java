package org.fingerlinks.mobile.android.navigationhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Raphael on 13/07/2015.
 */
public class NavHelper {

    public final static String BUNDLE = "bundle";
    private final static String TAG = NavHelper.class.getName();
    private static final long DOUBLE_PRESS_INTERVAL = 5 * 1000;
    private static NavHelper instance;
    private static List<String> listStep;
    private static long lastPressTime;
    private Context context;
    private NavBean bean;

    public static NavHelper with(Context context) {
        if (instance == null) {
            instance = new NavHelper();
            instance.initDebug();
        }
        instance.initNavHelper(context);
        return instance;
    }

    private void initDebug() {
        listStep = new ArrayList<>();
    }

    public void printDebug() {
        Log.d(TAG, "--------------------------------------------------------");
        Log.d(TAG, "list of step");
        for (String step : listStep) {
            Log.d(TAG, step);
        }
    }

    private void initNavHelper(Context context) {
        this.context = context;
        this.bean = new NavBean();
    }

    public NavHelper goTo(Class<?> activity, Bundle bundle) {
        Intent intent = new Intent(context, activity);
        if(bundle != null) {
            intent.putExtra(BUNDLE, bundle);
        }
        bean.setIntent(intent);
        listStep.add(activity.getName());
        return this;
    }

    public NavHelper goTo(Class<?> activity) {
        return goTo(activity, null);
    }

    public NavHelper goTo(Fragment fragment, Bundle bundle, int container) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragment.setArguments(bundle);
        bean.setFragment(fragment);
        bean.setFragmentManager(fragmentManager);
        bean.setContainer(container);
        return this;
    }

    public NavHelper tag(String tag) {
        bean.setTag(tag);
        return this;
    }

    public NavHelper replace() {
        COMMIT_TYPE type = COMMIT_TYPE.REPLACE;
        bean.setType(type);
        return this;
    }

    public NavHelper add() {
        COMMIT_TYPE type = COMMIT_TYPE.ADD;
        bean.setType(type);
        return this;
    }

    public NavHelper addToBackStack() {
        bean.setAddToBackStack(true);
        return this;
    }

    public void commit() {
        commit(-1);
    }

    public void commit(int REQUEST_CODE) {
        if (bean.getIntent() != null) {
            commitActivity(REQUEST_CODE);
        }
        if (bean.getFragmentManager() != null) {
            commitFragment();
        }
    }

    public NavHelper animation() {
        bean.setAnimation(true);
        return this;
    }

    public NavHelper customAnimation(int enter, int exit) {
        int[] animations = new int[2];
        animations[0] = enter;
        animations[1] = exit;
        bean.setAnimations(animations);
        return this;
    }

    public NavHelper customAnimation(int enter, int exit, int popEnter, int popExit) {
        int[] animations = new int[4];
        animations[0] = enter;
        animations[1] = exit;
        animations[2] = popEnter;
        animations[3] = popExit;
        bean.setAnimations(animations);
        return this;
    }

    private void commitActivity() {
        commitActivity(-1);
    }

    private void commitActivity(int REQUEST_CODE) {
        if(REQUEST_CODE > 0) {
            ((Activity)context).startActivityForResult(bean.getIntent(), REQUEST_CODE);
        } else {
            context.startActivity(bean.getIntent());
        }
        if (bean.isAnimation()) {
            switch (bean.getAnimations().length) {
                case 2:
                    ((Activity) context).overridePendingTransition(bean.getAnimations()[0], bean.getAnimations()[1]);
                    break;
                default:
                    ((Activity) context).overridePendingTransition(R.anim.anim_window_in, R.anim.anim_window_out);
                    break;
            }
        }
    }

    private void commitFragment() {
        listStep.add(bean.getTag());

        FragmentTransaction fragmentTransaction = bean.getFragmentManager().beginTransaction();
        if (bean.isAnimation()) {
            switch (bean.getAnimations().length) {
                case 2:
                    fragmentTransaction.setCustomAnimations(bean.getAnimations()[0], bean.getAnimations()[1]);
                    break;
                case 4:
                    fragmentTransaction.setCustomAnimations(bean.getAnimations()[0], bean.getAnimations()[1], bean.getAnimations()[2], bean.getAnimations()[3]);
                    break;
                default:
                    fragmentTransaction.setCustomAnimations(R.anim.view_flipper_transition_in_left, R.anim.view_flipper_transition_out_left, R.anim.view_flipper_transition_in_right, R.anim.view_flipper_transition_out_right);
                    break;
            }
        }
        switch (bean.getType()) {
            case REPLACE:
                fragmentTransaction.replace(bean.getContainer(), bean.getFragment(), bean.getTag());
                break;
            case ADD:
                fragmentTransaction.add(bean.getContainer(), bean.getFragment(), bean.getTag());
                break;
        }
        if (bean.isAddToBackStack()) {
            fragmentTransaction.addToBackStack(bean.getTag());
        }
        fragmentTransaction.commit();
    }

    public boolean canGoBack(String tag, int container, FragmentManager fragmentManager) {
        //FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
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

    public boolean canGoBack(FragmentManager fragmentManager) {
        return canGoBack(null, 0, fragmentManager);
    }

    public void goBackTo(String tag) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(tag) != null) {
            List<FragmentManager.BackStackEntry> fragmentList = fragmentList();
            Collections.reverse(fragmentList);
            for (int i = 0; i < fragmentList.size(); i++) {
                if (!tag.equalsIgnoreCase(fragmentList.get(i).getName())) {
                    fragmentManager.popBackStack();
                } else {
                    listStep.add(tag);
                    return;
                }
            }
        } else {
            Log.e(TAG, "no fragment found");
        }
    }

    public void goBack() {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        if (canGoBack(fragmentManager)) {
            fragmentManager.popBackStack();
        }
    }

    private List<FragmentManager.BackStackEntry> fragmentList() {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        List<FragmentManager.BackStackEntry> fragmentList = new ArrayList<>();
        int size = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < size; i++) {
            fragmentList.add(fragmentManager.getBackStackEntryAt(i));
            Log.d(TAG, "position: " + i + " name: " + fragmentManager.getBackStackEntryAt(i).getName());
        }
        return fragmentList;
    }

    public void backHome(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        long pressTime = System.currentTimeMillis();
        if ((pressTime - lastPressTime) <= DOUBLE_PRESS_INTERVAL) {
            activity.finish();
        }
        lastPressTime = pressTime;
    }

    private enum COMMIT_TYPE {

        ADD(1),
        REPLACE(0);

        private final int getCommitType;

        COMMIT_TYPE(int getCommitType) {
            this.getCommitType = getCommitType;
        }

        public int getGetCommitType() {
            return getCommitType;
        }
    }

    private class NavBean {

        private Intent intent = null;
        private FragmentManager fragmentManager = null;
        private Fragment fragment = null;
        private int container;
        private COMMIT_TYPE type;
        private boolean addToBackStack = false;
        private String tag = null;
        private boolean animation = false;
        private int[] animations = new int[0];

        public Intent getIntent() {
            return intent;
        }

        public void setIntent(Intent intent) {
            this.intent = intent;
        }

        public FragmentManager getFragmentManager() {
            return fragmentManager;
        }

        public void setFragmentManager(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public int getContainer() {
            return container;
        }

        public void setContainer(int container) {
            this.container = container;
        }

        public COMMIT_TYPE getType() {
            return type;
        }

        public void setType(COMMIT_TYPE type) {
            this.type = type;
        }

        public boolean isAddToBackStack() {
            return addToBackStack;
        }

        public void setAddToBackStack(boolean addToBackStack) {
            this.addToBackStack = addToBackStack;
        }

        public String getTag() {
            if (tag == null) {
                return getFragment().getClass().getName();
            }
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public boolean isAnimation() {
            return animation;
        }

        public void setAnimation(boolean animation) {
            this.animation = animation;
        }

        public int[] getAnimations() {
            return animations;
        }

        public void setAnimations(int[] animations) {
            this.animations = animations;
        }

    }

}
