package org.fingerlinks.mobile.android.navigationhelper.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.fingerlinks.mobile.android.navigator.Navigator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_activity:
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", "Title from bundle");
                Navigator.with(MainActivity.this)
                        .build() //Enter in navigation mode
                        .goTo(SecondActivity.class, bundle) //set destination and Bundle data
                        .animation()
                        .commit(); //Execute startActivity -- startActivityForResult
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Navigator.with(MainActivity.this)
                .utils()
                .confirmExitWithMessage("Press again to exit!");
    }

}