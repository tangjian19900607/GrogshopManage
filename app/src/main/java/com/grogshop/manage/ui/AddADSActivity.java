package com.grogshop.manage.ui;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.Ads;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.SaveListener;

public class AddADSActivity extends ActionBarActivity implements View.OnClickListener {

    private Button mPublishButton;
    private EditText mTitleEditText;
    private EditText mContentEditText;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ads);
        setTitle("新增公告");
        initViewId();
        setListener();
        initProgressDialog();
    }

    private void initViewId() {
        mPublishButton = (Button) this.findViewById(R.id.publish);
        mTitleEditText = (EditText) this.findViewById(R.id.ads_title);
        mContentEditText = (EditText) this.findViewById(R.id.ads_content);
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在发布,请稍等");
        mProgressDialog.setTitle("提示");
    }

    private void setListener() {
        mPublishButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        publish();
    }

    private void publish() {
        String title = mTitleEditText.getText().toString();
        String content = mContentEditText.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:MM");
        String time = simpleDateFormat.format(new Date());
        Ads ads = new Ads(title, content, time);
        mProgressDialog.show();
        ads.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                mProgressDialog.dismiss();
                finish();
                Toast.makeText(AddADSActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                mProgressDialog.dismiss();
                Toast.makeText(AddADSActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
