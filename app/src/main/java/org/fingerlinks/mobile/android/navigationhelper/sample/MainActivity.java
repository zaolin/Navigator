package org.fingerlinks.mobile.android.navigationhelper.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.fingerlinks.mobile.android.navigator.Navigator;
import org.fingerlinks.mobile.android.navigator.NavigatorHelper;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_activity) {
            Bundle bundle = new Bundle();
            bundle.putString("TITLE", "Title from bundle");

            /**
             * Activity example
             */
            Navigator
                .with(MainActivity.this)
                .build() //Enter in navigation mode
                .animation() //Add default animation
                .goTo(SecondActivity.class, bundle) //set destination and Bundle data
                .addRequestCode(9001) //set REQUEST_CODE
                .commit(); //Execute startActivity -- startActivityForResult

            /*NavigatorHelper
                    .with(MainActivity.this)
                    .goTo(SecondActivity.class, bundle)
                    .animation()
                    .commit();*/
            return true;
        }
        if (id == R.id.action_debug) {
            //TODO @Raphael ripristinare
            //NavigatorHelper.with(MainActivity.this)
            //        .printDebug();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Navigator.with(MainActivity.this)
                .utils()
                .confirmExitWithMessage("Press again to exit!");
    }

};