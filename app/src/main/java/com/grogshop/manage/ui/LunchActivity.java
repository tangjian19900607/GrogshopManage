package com.grogshop.manage.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.grogshop.manage.R;

/**
 *  启动界面，程序入口
 */
public class LunchActivity extends ActionBarActivity implements View.OnClickListener {

    private Button mAdminButton;
    private Button mCustomerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);
        initView();
        setListener();
    }


    private void initView() {
        mAdminButton = (Button) this.findViewById(R.id.admin);
        mCustomerButton = (Button) this.findViewById(R.id.customer);
    }

    private void setListener() {
        mAdminButton.setOnClickListener(this);
        mCustomerButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.admin:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            case R.id.customer:
                Intent customerIntent = new Intent(this, MainActivity.class);
                startActivity(customerIntent);
                finish();
                break;
            default:
                break;
        }
    }
}
