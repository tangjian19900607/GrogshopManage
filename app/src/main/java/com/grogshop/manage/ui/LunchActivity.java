package com.grogshop.manage.ui;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.User;
import com.grogshop.manage.util.Logger;

import cn.bmob.v3.listener.SaveListener;

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
//        initUser();
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

    /**
     * 初始化管理员账户
     */
    private void initUser(){
        User user = new User("1","admin","123456");
        user.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Logger.log("init user success!");
            }

            @Override
            public void onFailure(int i, String s) {
                Logger.log("init user failure!");
            }
        });
    }
}
