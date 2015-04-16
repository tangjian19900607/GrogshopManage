package com.grogshop.manage.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.OrderAdapter;
import com.grogshop.manage.domain.Order;
import com.grogshop.manage.domain.Waiter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * 订单管理界面
 */
public class OrderManageActivity extends ActionBarActivity {

    private ListView mOrderListView;
    private ProgressDialog mProgressDialog;
    OrderAdapter mOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setTitle("订单管理");
        initViewId();
        initProgressDialog();
        initData();
        registerForContextMenu(mOrderListView);
    }



    private void initViewId() {
        mOrderListView = (ListView) this.findViewById(R.id.order_list);
        mOrderListView.setEmptyView(findViewById(R.id.empty_view));
    }

    private void initData() {
        mProgressDialog.show();
        BmobQuery<Order> bmobQuery = new BmobQuery<Order>();
        bmobQuery.order("-time");
        bmobQuery.findObjects(this, new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> list) {
                mProgressDialog.dismiss();
                mOrderAdapter = new OrderAdapter(OrderManageActivity.this, list);
                mOrderListView.setAdapter(mOrderAdapter);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("订单管理");
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        final List<Order> list = mOrderAdapter.getData();
        switch(item.getItemId()) {
            case R.id.menu_item_delete:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_dialog_message)
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Order order = list.get(info.position);
                                mOrderAdapter.getData().remove(order);
                                order.delete(OrderManageActivity.this,new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(OrderManageActivity.this, "删除订单成功", Toast.LENGTH_LONG).show();
                                        mOrderAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(OrderManageActivity.this,"删除订单失败："+s,Toast.LENGTH_LONG).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

}
