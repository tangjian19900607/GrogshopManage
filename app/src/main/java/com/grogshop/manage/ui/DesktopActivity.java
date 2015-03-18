package com.grogshop.manage.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.grogshop.manage.R;

/**
 * 餐桌界面
 */
public class DesktopActivity extends ActionBarActivity {
    public static final String DESKTOP_NAME = "name";
    public static final String DESKTOP_POSITION = "position";

    private String mDesktopName;
    private int mDesktopPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop);
        getIntentValue();
        initData();
    }


    private void getIntentValue() {
        Intent intent = getIntent();
        if (null != intent) {
            mDesktopName = intent.getStringExtra(DESKTOP_NAME);
            mDesktopPosition = intent.getIntExtra(DESKTOP_POSITION, 0);
        }
    }

    private void initData() {
        setTitle(mDesktopName);
    }

}
