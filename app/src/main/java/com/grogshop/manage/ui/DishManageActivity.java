package com.grogshop.manage.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.DishAdapter;
import com.grogshop.manage.domain.Dish;
import com.grogshop.manage.util.ImageLoaderUtil;
import com.grogshop.manage.view.RefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class DishManageActivity extends ActionBarActivity {
    private ListView mDishListView;
    private ProgressDialog mProgressDialog;
    private String mActivityName;
    DishAdapter mDishAdapter;
    ImageLoaderUtil mImageLoaderUtil;
    private RefreshLayout mRefreshLayout;
    private int currentPage = 1;
    private int limit = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        mActivityName = getIntent().getStringExtra(AdminMainActivity.NAME);
        setTitle(mActivityName);
        initImageLoader();
        initViewId();
        initProgressDialog();
        initData();
        setListener();
        registerForContextMenu(mDishListView);
    }

    private void setListener() {
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                Toast.makeText(DishManageActivity.this, "loading...", Toast.LENGTH_LONG).show();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载数据
                        PullUpToLoad();
                        //更新后结束加载
                        mRefreshLayout.setLoading(false);
                    }
                }, 2000);
            }
        });
    }
    private void PullUpToLoad() {
        BmobQuery<Dish> query = new BmobQuery<Dish>();
        query.setLimit(limit);
        query.setSkip(currentPage * limit);
        query.order("-createdAt");
        query.findObjects(DishManageActivity.this, new FindListener<Dish>() {
            @Override
            public void onSuccess(List<Dish> list) {
                if (list.size() > 0) {
                    mDishAdapter.addData(list);
                    if (mDishAdapter.getCount() > limit) {
                        currentPage++;
                    }
                    Toast.makeText(DishManageActivity.this, "第" + currentPage + "页加载完毕", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DishManageActivity.this, "没有更多可以加载的内容", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(DishManageActivity.this, "查询更多菜品失败", Toast.LENGTH_LONG).show();

            }
        });
    }



    private void initImageLoader() {
        mImageLoaderUtil = new ImageLoaderUtil(this);
    }

    private void initData() {
        mProgressDialog.show();
        BmobQuery<Dish> bmobQuery = new BmobQuery<Dish>();
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(DishManageActivity.this,new FindListener<Dish>() {
            @Override
            public void onSuccess(List<Dish> dishs) {
                mProgressDialog.dismiss();
                mDishAdapter = new DishAdapter(DishManageActivity.this,dishs,mDishListView);
                mDishListView.setAdapter(mDishAdapter);
            }

            @Override
            public void onError(int i, String s) {
                mProgressDialog.dismiss();
                Toast.makeText(DishManageActivity.this, "加载菜品失败："+s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewId() {
        mDishListView = (ListView) findViewById(R.id.dish_listview);
        mDishListView.setEmptyView(findViewById(R.id.dish_empty_view));
        mRefreshLayout = (RefreshLayout) findViewById(R.id.dishmag_swipe_refreshLayout);
    }
    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在获取数据");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_dish,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add_dish){
            Intent intent = new Intent(this,AddDishActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(mActivityName);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        final List<Dish> list = mDishAdapter.getData();
        switch(item.getItemId()) {
            case R.id.menu_item_delete:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_dialog_message)
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dish dish = list.get(info.position);
                                mDishAdapter.getData().remove(dish);
                                dish.delete(DishManageActivity.this,new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(DishManageActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                                        mDishAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(DishManageActivity.this,"删除失败："+s,Toast.LENGTH_LONG).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
