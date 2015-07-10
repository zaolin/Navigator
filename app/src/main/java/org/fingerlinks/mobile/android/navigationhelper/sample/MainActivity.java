package org.fingerlinks.mobile.android.navigationhelper.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.fingerlinks.mobile.android.navigationhelper.NavHelper;


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
            NavHelper.with(MainActivity.this)
                    .goTo(SecondActivity.class, bundle)
                    .animation()
                    .commit();
            return true;
        }
        if (id == R.id.action_debug) {
            NavHelper.with(MainActivity.this)
                    .printDebug();
        }
        return super.onOptionsItemSelected(item);
    }
}
