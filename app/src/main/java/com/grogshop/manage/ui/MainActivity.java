package com.grogshop.manage.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.DesktopAdapter;
import com.grogshop.manage.domain.DesktopItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 顾客菜单界面
 */
public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private GridView mGridView;
    private DesktopAdapter mDesktopAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewId();
        initData();
        setListener();
    }
    private void initViewId(){
        mGridView = (GridView) this.findViewById(R.id.grid_desktop);
    }
    private void initData(){
        List<DesktopItem> list = new ArrayList<DesktopItem>();
        list.add(new DesktopItem(1,"001\n餐桌"));
        list.add(new DesktopItem(2,"002\n餐桌"));
        list.add(new DesktopItem(3,"003\n餐桌"));
        list.add(new DesktopItem(4,"004\n餐桌"));
        list.add(new DesktopItem(5,"005\n餐桌"));
        list.add(new DesktopItem(6,"006\n餐桌"));
        list.add(new DesktopItem(7,"007\n餐桌"));
        list.add(new DesktopItem(8,"008\n餐桌"));
        list.add(new DesktopItem(9,"009\n餐桌"));
        list.add(new DesktopItem(10,"010\n餐桌"));
        list.add(new DesktopItem(11,"011\n餐桌"));
        list.add(new DesktopItem(12,"012\n餐桌"));
        list.add(new DesktopItem(13,"013\n餐桌"));
        list.add(new DesktopItem(14,"014\n餐桌"));
        list.add(new DesktopItem(15,"015\n餐桌"));
        list.add(new DesktopItem(16,"016\n餐桌"));
        list.add(new DesktopItem(17,"017\n餐桌"));
        list.add(new DesktopItem(18,"018\n餐桌"));
        list.add(new DesktopItem(19,"019\n餐桌"));
        list.add(new DesktopItem(20,"020\n餐桌"));
        list.add(new DesktopItem(21,"021\n餐桌"));
        mDesktopAdapter = new DesktopAdapter(this,list);
    mGridView.setAdapter(mDesktopAdapter);
}
    private void setListener(){
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = mDesktopAdapter.getData().get(position).getName();
        int index = mDesktopAdapter.getData().get(position).getPosition();
        Intent intent = new Intent(MainActivity.this,OrderDishActivity.class);
        intent.putExtra(ShowDishActivity.DESKTOP_NAME,name);
        intent.putExtra(ShowDishActivity.DESKTOP_POSITION,index);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_check_login,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_login){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
