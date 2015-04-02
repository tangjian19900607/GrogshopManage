package com.grogshop.manage.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.Waiter;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.SaveListener;

/**
 * 人员添加
 */
public class WaiterAddActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText mNameEditText;
    private EditText mZhizeEditText;
    private Spinner mSpinner;
    private Button mAddButton;
    private String mType = "管理员";
    private static final String[] mPersonType = {"管理员", "服务员", "收银员"};
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_add);
        initViewId();
        initSpinner();
        setListener();
        initProgressDialog();
    }

    private void initViewId() {
        mNameEditText = (EditText) this.findViewById(R.id.watier_name);
        mZhizeEditText = (EditText) this.findViewById(R.id.watier_zhize);
        mSpinner = (Spinner) this.findViewById(R.id.watier_spinner);
        mAddButton = (Button) this.findViewById(R.id.add_person);
    }

    private void initSpinner() {
        mSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mPersonType));
    }

    private void setListener() {
        mSpinner.setOnItemSelectedListener(this);
        mAddButton.setOnClickListener(this);
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在添加");
    }

    @Override
    public void onClick(View v) {
        mProgressDialog.show();
        String name = mNameEditText.getText().toString().trim();
        String zhize = mZhizeEditText.getText().toString().trim();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Waiter waiter = new Waiter(name, simpleDateFormat.format(new Date()), mType, zhize);
        waiter.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                mProgressDialog.dismiss();
                Toast.makeText(WaiterAddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                mProgressDialog.dismiss();
                Toast.makeText(WaiterAddActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mType = mPersonType[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
