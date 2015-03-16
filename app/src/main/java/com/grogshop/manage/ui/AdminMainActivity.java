package com.grogshop.manage.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.AdminAdapter;
import com.grogshop.manage.domain.AdminItem;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends ActionBarActivity {
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        initViewId();
    }

    private void initViewId() {
        mGridView = (GridView) this.findViewById(R.id.grid_view);
        List<AdminItem> list = new ArrayList<AdminItem>();
        list.add(new AdminItem(001, "新增\n菜品"));
        list.add(new AdminItem(002, "订单\n管理"));
        list.add(new AdminItem(003, "餐桌\n状态"));
        list.add(new AdminItem(004, "酒店\n公告"));
        AdminAdapter adminAdapter = new AdminAdapter(this, list);
        mGridView.setAdapter(adminAdapter);
    }
}
