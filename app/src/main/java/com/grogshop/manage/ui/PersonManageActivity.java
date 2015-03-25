package com.grogshop.manage.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.WatierAdapter;
import com.grogshop.manage.domain.Waiter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class PersonManageActivity extends ActionBarActivity {

    private ListView mPersonListView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        setTitle("人员管理");
        initViewId();
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
                WatierAdapter watierAdapter = new WatierAdapter(PersonManageActivity.this, list);
                mPersonListView.setAdapter(watierAdapter);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.watier_add) {
            Intent intent = new Intent(this, WaiterAddActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
