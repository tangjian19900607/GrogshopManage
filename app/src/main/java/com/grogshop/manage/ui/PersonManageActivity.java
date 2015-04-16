package com.grogshop.manage.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.grogshop.manage.adapter.WatierAdapter;
import com.grogshop.manage.domain.Dish;
import com.grogshop.manage.domain.Waiter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class PersonManageActivity extends ActionBarActivity {

    private ListView mPersonListView;
    private ProgressDialog mProgressDialog;
    WatierAdapter mWatierAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        setTitle("人员管理");
        initViewId();
        registerForContextMenu(mPersonListView);
        initProgressDialog();
        initData();
    }

    private void initViewId() {
        mPersonListView = (ListView) this.findViewById(R.id.person_listview);
        mPersonListView.setEmptyView(findViewById(R.id.empty_view));
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在获取数据");
    }

    private void initData() {
        mProgressDialog.show();
        BmobQuery<Waiter> bmobQuery = new BmobQuery<Waiter>();
        bmobQuery.findObjects(this, new FindListener<Waiter>() {
            @Override
            public void onSuccess(List<Waiter> list) {
                mProgressDialog.dismiss();
                mWatierAdapter = new WatierAdapter(PersonManageActivity.this, list);
                mPersonListView.setAdapter(mWatierAdapter);
            }

            @Override
            public void onError(int i, String s) {
                mProgressDialog.dismiss();
                Toast.makeText(PersonManageActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.watier_add) {
            Intent intent = new Intent(this, WaiterAddActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("人员管理");
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        final List<Waiter> list = mWatierAdapter.getData();
        switch(item.getItemId()) {
            case R.id.menu_item_delete:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_dialog_message)
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Waiter waiter = list.get(info.position);
                                mWatierAdapter.getData().remove(waiter);
                                waiter.delete(PersonManageActivity.this,new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(PersonManageActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                                        mWatierAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(PersonManageActivity.this,"删除失败："+s,Toast.LENGTH_LONG).show();
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
