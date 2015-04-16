package com.grogshop.manage.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * 登录界面，仅限管理员
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private Button mLoginButton;
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private ProgressBar mContentLoadingProgressBar;

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
        mContentLoadingProgressBar = (ProgressBar) this.findViewById(R.id.progressbar);
    }

    private void setListener() {
        mLoginButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        mContentLoadingProgressBar.setVisibility(View.VISIBLE);
        final String username = mUserNameEditText.getText().toString().trim();
        final String password = mPasswordEditText.getText().toString().trim();
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        userBmobQuery.addWhereEqualTo("userName", username);
        userBmobQuery.addWhereEqualTo("password", password);
        userBmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                // TODO 将要修改
                mContentLoadingProgressBar.setVisibility(View.GONE);
                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                startActivity(intent);
//                if (list.size() > 0) {
//                    Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onError(int i, String s) {
                mContentLoadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
