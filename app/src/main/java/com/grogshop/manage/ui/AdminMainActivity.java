package com.grogshop.manage.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
        List<AdminItem> list = new ArrayList<>();
        list.add(new AdminItem(001, "菜品\n管理"));
        list.add(new AdminItem(002, "订单\n管理"));
        list.add(new AdminItem(003, "人员\n管理"));
        list.add(new AdminItem(004, "公告\n管理"));
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
                // 菜品管理
                Intent intent1 = new Intent(this, DishManageActivity.class);
                intent1.putExtra(NAME, "菜品管理");
                intent1.putExtra(POSITION, index);
                startActivity(intent1);
                break;
            case 1:
                // 订单管理
                Intent intent2 = new Intent(this, OrderManageActivity.class);
                intent2.putExtra(NAME, "订单管理");
                intent2.putExtra(POSITION, index);
                startActivity(intent2);
                break;
            case 2:
                // 人员管理
                Intent intent3 = new Intent(this, PersonManageActivity.class);
                intent3.putExtra(NAME, "人员管理");
                intent3.putExtra(POSITION, index);
                startActivity(intent3);
                break;
            case 3:
                // 公告管理
                Intent intent4 = new Intent(this, ADSManageActivity.class);
                intent4.putExtra(NAME, "公告管理");
                intent4.putExtra(POSITION, index);
                startActivity(intent4);
                break;
            default:
                break;
        }
    }
}
