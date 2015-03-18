package com.grogshop.manage.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.AdminAdapter;
import com.grogshop.manage.domain.AdminItem;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private GridView mGridView;
    private AdminAdapter mAdminAdapter;
    public static final String NAME = "name";
    public static final String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        initViewId();
        initData();
        setListener();
    }

    private void initViewId() {
        mGridView = (GridView) this.findViewById(R.id.grid_view);
    }

    private void initData() {
        List<AdminItem> list = new ArrayList<AdminItem>();
        list.add(new AdminItem(001, "新增\n菜品"));
        list.add(new AdminItem(002, "订单\n管理"));
        list.add(new AdminItem(003, "餐桌\n状态"));
        list.add(new AdminItem(004, "酒店\n公告"));
        mAdminAdapter = new AdminAdapter(this, list);
        mGridView.setAdapter(mAdminAdapter);
    }

    private void setListener() {
        mGridView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = mAdminAdapter.getData().get(position).getName();
        int index = mAdminAdapter.getData().get(position).getPosition();
        switch (position) {
            case 0:
                // 增加菜的种类
                Intent intent1 = new Intent(this, AddDishActivity.class);
                intent1.putExtra(NAME, name);
                intent1.putExtra(POSITION, index);
                startActivity(intent1);
                break;
            case 1:
                // 订单管理
                Intent intent2 = new Intent(this, OrderActivity.class);
                intent2.putExtra(NAME, name);
                intent2.putExtra(POSITION, index);
                startActivity(intent2);
                break;
            case 2:
                // 餐桌状态
                Intent intent3 = new Intent(this, DesktopStateActivity.class);
                intent3.putExtra(NAME, name);
                intent3.putExtra(POSITION, index);
                startActivity(intent3);
                break;
            case 3:
                // 酒店广告
                Intent intent4 = new Intent(this, ADSActivity.class);
                intent4.putExtra(NAME, name);
                intent4.putExtra(POSITION, index);
                startActivity(intent4);
                break;
            default:
                break;
        }
    }
}
