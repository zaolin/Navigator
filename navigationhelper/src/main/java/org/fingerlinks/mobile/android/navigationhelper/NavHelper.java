package org.fingerlinks.mobile.android.navigationhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Raphaël on 09/07/2015.
 */
public class NavHelper {

    private Context context;
    private static NavHelper navHelper;
    private NavBean bean;

    public NavHelper(Context context) {
        bean = new NavBean();
        this.context = context;
    }

    public static NavHelper with(Context context) {
        return navHelper = new NavHelper(context);
    }

    public void goTo(Activity activity, Bundle bundle) {
        Intent intent = new Intent(context, activity.getClass());
        intent.putExtra(BUNDLE, bundle);
        bean.setIntent(intent);
    }

    public void goTo(Fragment fragment, Bundle bundle, int container) {
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        fragment.setArguments(bundle);
        bean.setFragment(fragment);
        bean.setFragmentManager(fragmentManager);
    }

    public void commit() {
        if (bean.getIntent() != null) {
            context.startActivity(bean.getIntent());
        }
        if (bean.getFragmentManager() != null) {

        }
    }

    public final static String BUNDLE = "bundle";

    private class NavBean {

        private Intent intent = null;
        private FragmentManager fragmentManager = null;
        private Fragment fragment;
        private int container;

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
    }

}
