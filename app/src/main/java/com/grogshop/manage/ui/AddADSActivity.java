package com.grogshop.manage.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.grogshop.manage.R;

public class AddADSActivity extends ActionBarActivity implements View.OnClickListener{

    private Button mPublishButton;
    private EditText mTitleEditText;
    private EditText mContentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ads);
        setTitle("新增公告");
        initViewId();
        setListener();
    }

    private void initViewId() {
        mPublishButton = (Button) this.findViewById(R.id.publish);
        mTitleEditText = (EditText) this.findViewById(R.id.ads_title);
        mContentEditText = (EditText) this.findViewById(R.id.ads_content);
    }

    private void setListener() {
        mPublishButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        publish();
    }

    private void publish() {
        
    }
}
