package org.fingerlinks.mobile.android.navigationhelper.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.fingerlinks.mobile.android.navigator.NavigatorException;
import org.fingerlinks.mobile.android.navigator.NavigatorHelper;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = SecondActivity.class.getName();
    int numFragment = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Bundle bundle = getIntent().getBundleExtra(NavigatorHelper.BUNDLE);
        setTitle(bundle.getString("TITLE"));
        Fragment fragment = new SecondFragment();
        NavigatorHelper.with(SecondActivity.this)
                .goTo(fragment, null, R.id.container)
                .tag("HOME_FRAGMENT")
                .add()
                .addToBackStack()
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_fragment) {
            Fragment fragment = new SecondTestFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TEST", "fragment number " + numFragment);
            NavigatorHelper.with(SecondActivity.this)
                    .goTo(fragment, bundle, R.id.container)
                    .animation()
                    .customAnimation(R.anim.abc_slide_out_bottom, R.anim.slide_down, R.anim.abc_slide_out_bottom, R.anim.slide_down)
                    .tag("fragment_" + numFragment)
                    .add()
                    .addToBackStack()
                    .commit();
            numFragment++;
            return true;
        }
        if (id == R.id.action_debug) {
            NavigatorHelper.with(SecondActivity.this).printDebug();
        }
        if (id == R.id.action_close) {
            finish();
        }
        if (id == R.id.action_go_to) {
            if (NavigatorHelper.with(SecondActivity.this).canGoBack("fragment_4", R.id.container, getSupportFragmentManager())) {
                try {
                    NavigatorHelper.
                            with(SecondActivity.this).
                            goBackTo("fragment_4");
                } catch (NavigatorException _ex) {
                    Log.d(TAG, _ex.getMessage());
                }

            } else {
                Toast.makeText(SecondActivity.this, "Can't go to fragment 4", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.action_go_to_home) {
            if (NavigatorHelper.with(SecondActivity.this).canGoBack("HOME_FRAGMENT", R.id.container, getSupportFragmentManager())) {
                try {
                    NavigatorHelper.with(SecondActivity.this).goBackTo("HOME_FRAGMENT");
                } catch (NavigatorException _ex) {
                }
            } else {
                Toast.makeText(SecondActivity.this, "Can't go to home fragment", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.action_back) {
            Toast.makeText(SecondActivity.this, "Can go back? " +
                    NavigatorHelper.with(SecondActivity.this).canGoBack(getSupportFragmentManager()), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (NavigatorHelper.with(SecondActivity.this).canGoBack(getSupportFragmentManager())) {
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
