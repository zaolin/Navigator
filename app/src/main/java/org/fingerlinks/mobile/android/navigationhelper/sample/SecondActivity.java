package org.fingerlinks.mobile.android.navigationhelper.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.fingerlinks.mobile.android.navigator.Navigator;
import org.fingerlinks.mobile.android.navigator.NavigatorException;
import org.fingerlinks.mobile.android.navigator.utils.Constant;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = SecondActivity.class.getName();
    int numFragment = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Bundle bundle = getIntent().getBundleExtra(Constant.BUNDLE);
        setTitle(bundle.getString("TITLE"));
        Fragment fragment = new SecondFragment();

        Navigator
                .with(SecondActivity.this)
                .build() //Enter in navigation mode
                .goTo(fragment, bundle, R.id.container)
                .animation(R.anim.abc_slide_out_bottom, R.anim.slide_down, R.anim.abc_slide_out_bottom, R.anim.slide_down) //Add custom animation
                .tag("HOME_FRAGMENT")
                .addToBackStack() //add backstack
                .add() //CommitType
                .commit(); //commit operation

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

            Navigator
                    .with(SecondActivity.this)
                    .build() //Enter in navigation mode
                    .goTo(fragment, bundle, R.id.container)
                    .animation(R.anim.abc_slide_out_bottom, R.anim.slide_down, R.anim.abc_slide_out_bottom, R.anim.slide_down) //Add custom animation
                    .tag("fragment_" + numFragment)
                    .addToBackStack() //add backstack
                    .add() //CommitType
                    .commit(); //commit operation

            numFragment++;
            return true;
        }
        if (id == R.id.action_debug) {
            //NavigatorHelper.with(SecondActivity.this).printDebug();
        }
        if (id == R.id.action_close) {
            finish();
        }
        if (id == R.id.action_go_to) {
            try {
                Navigator
                        .with(SecondActivity.this).utils()
                        .goBackToSpecificPoint("fragment_4");
            } catch (NavigatorException _ex) {
                Toast.makeText(SecondActivity.this, _ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if (id == R.id.action_go_to_home) {
            try {
                Navigator
                        .with(SecondActivity.this)
                        .utils()
                        .goBackToSpecificPoint("HOME_FRAGMENT");
            } catch (NavigatorException _ex) {
                Toast.makeText(SecondActivity.this, "Can't go to home fragment", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.action_back) {

            Toast.makeText(SecondActivity.this, "Can go back? " +
                Navigator.with(SecondActivity.this).utils()
                        .canGoBack(getSupportFragmentManager()), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (Navigator.with(SecondActivity.this).utils().canGoBack(getSupportFragmentManager())) {
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
