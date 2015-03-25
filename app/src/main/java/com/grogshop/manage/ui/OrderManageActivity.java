package com.grogshop.manage.ui;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.grogshop.manage.R;

/**
 * 订单管理界面
 */
public class OrderManageActivity extends ActionBarActivity {

    private ListView mOrderListView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle("订单管理");
        initViewId();
        initProgressDialog();
        initData();
    }


    private void initViewId() {
        mOrderListView = (ListView) this.findViewById(R.id.order_list);
    }

    private void initData() {

    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在获取订单");
    }

}
