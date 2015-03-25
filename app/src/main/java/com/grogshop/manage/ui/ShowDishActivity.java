package com.grogshop.manage.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.grogshop.manage.R;

/**
 * 餐桌界面
 */
public class ShowDishActivity extends ActionBarActivity {
    public static final String DESKTOP_NAME = "name";
    public static final String DESKTOP_POSITION = "position";

    private String mDesktopName;
    private int mDesktopPosition;
    private ListView mListView;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop);
        getIntentValue();
        initViewId();
        initData();
    }

    private void getIntentValue() {
        mSharedPreferences = getSharedPreferences("dish",MODE_PRIVATE);
        Intent intent = getIntent();
        if (null != intent) {
            mDesktopName = intent.getStringExtra(DESKTOP_NAME);
            if (null != mDesktopName) {
                mDesktopName = mDesktopName.replace("\n", "");
                mDesktopPosition = intent.getIntExtra(DESKTOP_POSITION, 0);
                mSharedPreferences.edit().putString("name", mDesktopName).commit();
                mSharedPreferences.edit().putInt("position", mDesktopPosition).commit();
            }
        }
    }

    private void initData() {
        setTitle(mDesktopName);
    }

    private void initViewId() {
        mListView = (ListView) this.findViewById(R.id.dish_list_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_dish, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDesktopName = mSharedPreferences.getString("name", "");
        mDesktopPosition = mSharedPreferences.getInt("position",0);
        setTitle(mDesktopName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.choose_dish) {
            Intent intent = new Intent(this, OrderDishActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
