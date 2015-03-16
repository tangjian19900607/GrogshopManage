package com.grogshop.manage.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

/**
 * 登录界面，仅限管理员
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener{

    private Button mLoginButton;
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViewId();
        setListener();
    }

    private void initViewId() {
        mLoginButton = (Button) this.findViewById(R.id.login);
        mUserNameEditText = (EditText) this.findViewById(R.id.username);
        mPasswordEditText = (EditText) this.findViewById(R.id.password);
    }

    private void setListener() {
        mLoginButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String username = mUserNameEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        User user = new User("1",username,password);
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        userBmobQuery.getObject(this, "31d9e5d1a9", new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
//                Toast.makeText(LoginActivity.this,"Success",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoginActivity.this,"Failure",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
