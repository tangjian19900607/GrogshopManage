package com.grogshop.manage.ui;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.OrderAdapter;
import com.grogshop.manage.domain.Order;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

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
        mOrderListView.setEmptyView(findViewById(R.id.empty_view));
    }

    private void initData() {
        mProgressDialog.show();
        BmobQuery<Order> bmobQuery = new BmobQuery<Order>();
        bmobQuery.findObjects(this, new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> list) {
                mProgressDialog.dismiss();
                OrderAdapter orderAdapter = new OrderAdapter(OrderManageActivity.this, list);
                mOrderListView.setAdapter(orderAdapter);
            }

            @Override
            public void onError(int i, String s) {
                mProgressDialog.dismiss();
            }
        });
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在获取订单");
    }

}
