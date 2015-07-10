package org.fingerlinks.mobile.android.navigationhelper.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.fingerlinks.mobile.android.navigationhelper.NavHelper;


public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Bundle bundle = getIntent().getBundleExtra(NavHelper.BUNDLE);
        setTitle(bundle.getString("TITLE"));
        Fragment fragment = new SecondActivityFragment();
        NavHelper.with(SecondActivity.this)
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

    int numFragment = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_fragment) {
            Fragment fragment = new SecondTestActivityFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TEST", "fragment number " + numFragment);
            NavHelper.with(SecondActivity.this)
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
            NavHelper.with(SecondActivity.this).printDebug();
        }
        if (id == R.id.action_close) {
            finish();
        }
        if (id == R.id.action_go_to) {
            if (NavHelper.with(SecondActivity.this).canGoBack("fragment_4", R.id.container)) {
                NavHelper.with(SecondActivity.this).goBackTo("fragment_4");
            } else {
                Toast.makeText(SecondActivity.this, "Can't go to fragment 4", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.action_go_to_home) {
            if (NavHelper.with(SecondActivity.this).canGoBack("HOME_FRAGMENT", R.id.container)) {
                NavHelper.with(SecondActivity.this).goBackTo("HOME_FRAGMENT");
            } else {
                Toast.makeText(SecondActivity.this, "Can't go to home fragment", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.action_back) {
            Toast.makeText(SecondActivity.this, "Can go back? " + NavHelper.with(SecondActivity.this).canGoBack(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (NavHelper.with(SecondActivity.this).canGoBack()) {
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
