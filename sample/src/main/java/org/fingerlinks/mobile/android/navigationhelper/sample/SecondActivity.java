package org.fingerlinks.mobile.android.navigationhelper.sample;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.fingerlinks.mobile.android.navigator.Navigator;
import org.fingerlinks.mobile.android.navigator.utils.Constant;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = SecondActivity.class.getName();
    int numFragment = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        Bundle bundle = getIntent().getBundleExtra(Constant.BUNDLE);
        setTitle(bundle.getString("TITLE"));
        Fragment fragment = new SecondFragment();
        Navigator.with(SecondActivity.this)
                .build() //Enter in navigation mode
                .goTo(fragment, bundle, R.id.container)
                .tag("HOME_FRAGMENT")
                .addToBackStack() //add backstack
                .replace() //CommitType
                .commit(); //commit operation
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_fragment:
                Bundle bundle = new Bundle();
                bundle.putString("TEST", "fragment number " + numFragment);
                Navigator.with(SecondActivity.this)
                        .build()
                        .goTo(Fragment.instantiate(SecondActivity.this, SecondTestFragment.class.getName()),
                                bundle,
                                R.id.container)
                        .addToBackStack()
                        .tag("fragment_" + numFragment)
                        .animation()
                        .add()
                        .commit();
                numFragment++;
                break;
            case R.id.action_close:
                Navigator.with(SecondActivity.this).utils().finishWithAnimation();
                break;
            case R.id.action_go_to:
                if (Navigator.with(SecondActivity.this).utils().canGoBackToSpecificPoint("fragment_4", R.id.container, getFragmentManager())) {
                    Navigator.with(SecondActivity.this).utils().goBackToSpecificPoint("fragment_4");
                } else {
                    Toast.makeText(SecondActivity.this, "Can't go to fragment_4", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_go_to_home:
                if (Navigator.with(SecondActivity.this).utils().canGoBackToSpecificPoint("HOME_FRAGMENT", R.id.container, getFragmentManager())) {
                    Navigator.with(SecondActivity.this)
                            .utils()
                            .goBackToSpecificPoint("HOME_FRAGMENT");
                } else {
                    Toast.makeText(SecondActivity.this, "Can't go HOME_FRAGMENT", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_back:
                boolean canGoBack = Navigator.with(SecondActivity.this).utils().canGoBack(getFragmentManager());
                Toast.makeText(SecondActivity.this, "Can go back? " + canGoBack, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (Navigator.with(SecondActivity.this).utils().canGoBack(getFragmentManager())) {
            Navigator.with(SecondActivity.this)
                    .utils()
                    .goToPreviousBackStack();
        } else {
            Navigator.with(SecondActivity.this).utils().finishWithAnimation();
        }
    }
}
