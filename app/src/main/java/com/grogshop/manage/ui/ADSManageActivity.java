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
import com.grogshop.manage.adapter.AdsAdapter;
import com.grogshop.manage.domain.Ads;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import static com.grogshop.manage.R.id.ads_list_view;

public class ADSManageActivity extends ActionBarActivity {
    private ListView mListView;
    private AdsAdapter mAdsAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        setTitle("公告管理");
        initViewId();
        initData();
    }

    private void initViewId() {
        mListView = (ListView) this.findViewById(ads_list_view);
    }

    private void initData() {
        // 从服务器获取数据，
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在获取公告数据");
        mProgressDialog.show();
        BmobQuery<Ads> adsBmobQuery = new BmobQuery<Ads>();
        adsBmobQuery.findObjects(this, new FindListener<Ads>() {
            @Override
            public void onSuccess(List<Ads> list) {
                mAdsAdapter = new AdsAdapter(ADSManageActivity.this, list);
                mListView.setAdapter(mAdsAdapter);
                mProgressDialog.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                mProgressDialog.dismiss();
                Toast.makeText(ADSManageActivity.this, "获取公告数据出错", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_ads) {
            Intent intent = new Intent(this, AddADSActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
