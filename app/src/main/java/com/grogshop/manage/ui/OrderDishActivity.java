package com.grogshop.manage.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.DishAdapter;
import com.grogshop.manage.domain.Dish;
import com.grogshop.manage.util.ImageLoaderUtil;
import com.grogshop.manage.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class OrderDishActivity extends ActionBarActivity implements  View.OnClickListener,AdapterView.OnItemClickListener {
    private static final String TAG = "OrderDishActivityTag";
    private ListView mDishListView;
    private List<Dish> mDishList;
    private ProgressDialog mProgressDialog;
    DishAdapter mDishAdapter;
    ModeCallBack modeCallBack;
    private Button mOrderButton;
    SparseBooleanArray mCheckStates;
    public static final String DESKTOP_NAME = "name";
    public static final String DESKTOP_POSITION = "position";
    private SharedPreferences mSharedPreferences;
    private String mDesktopName;
    private int mDesktopPosition;
    public static List<Dish> mOrderedList = new ArrayList<>();
    ImageLoaderUtil imageLoaderUtil;
    private RefreshLayout mRefreshLayout;
    private long localFirstDishTime;
    private Boolean isUpdate = false;
    private int currentPage = 1;
    private int limit = 10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dish);
        initImageLoader();
        getIntentValue();
        initView();
        initProgressDialog();
        initData();
        setListener();
    }


    private void initImageLoader() {
        imageLoaderUtil = new ImageLoaderUtil(this);
    }

    private void getIntentValue() {
        mSharedPreferences = getSharedPreferences("dish",MODE_PRIVATE);
        Intent intent = getIntent();
        if (null != intent) {
            mDesktopName = intent.getStringExtra(DESKTOP_NAME);
            if (null != mDesktopName) {
                mDesktopName = mDesktopName.replace("\n", "");
                mDesktopPosition = intent.getIntExtra(DESKTOP_POSITION, 0);
                mSharedPreferences.edit().putString("name", mDesktopName).commit();
                mSharedPreferences.edit().putInt("position", mDesktopPosition).commit();
            }
        }
    }
    private void initData() {
        mProgressDialog.show();
        BmobQuery<Dish> bmobQuery = new BmobQuery<Dish>();
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(OrderDishActivity.this,new FindListener<Dish>() {
            @Override
            public void onSuccess(List<Dish> dishs) {
                Dish localFirstDish = dishs.get(0);
                localFirstDishTime = localFirstDish.getTime();
                mProgressDialog.dismiss();
                mDishList = dishs;
                mDishAdapter = new DishAdapter(OrderDishActivity.this,dishs,mDishListView);
                mDishListView.setAdapter(mDishAdapter);
            }

            @Override
            public void onError(int i, String s) {
                mProgressDialog.dismiss();
                Toast.makeText(OrderDishActivity.this, "加载菜品失败：" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mOrderButton = (Button) findViewById(R.id.order_button);
        mDishListView = (ListView) findViewById(R.id.dish_listview);
        mDishListView.setEmptyView(findViewById(R.id.dish_empty_view));
        modeCallBack = new ModeCallBack();
        mDishListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        mDishListView.setMultiChoiceModeListener(modeCallBack);
        mDishListView.setOnItemClickListener(this);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.swipe_refreshLayout);
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在获取数据");
    }

    private void setListener() {
        mOrderButton.setOnClickListener(this);
        mRefreshLayout.setColorScheme(R.color.orange_normal, R.color.button_bg_normal, R.color.orange_pressed, R.color.purple_pressed);
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                Toast.makeText(OrderDishActivity.this, "loading...", Toast.LENGTH_LONG).show();
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
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(OrderDishActivity.this, "refresh...", Toast.LENGTH_LONG).show();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //刷新数据
                        PullDownToRefresh();
                        //结束刷新
                        mRefreshLayout.setRefreshing(false);
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
        query.findObjects(OrderDishActivity.this, new FindListener<Dish>() {
            @Override
            public void onSuccess(List<Dish> list) {
                if (list.size() > 0) {
                    mDishAdapter.addData(list);
                    if (mDishAdapter.getCount() > limit ) {
                        currentPage++;
                    }
                    Toast.makeText(OrderDishActivity.this, "第" + currentPage + "页加载完毕", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(OrderDishActivity.this, "没有更多可以加载的内容", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(OrderDishActivity.this, "查询更多菜品失败", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void PullDownToRefresh() {
        BmobQuery<Dish> query = new BmobQuery<Dish>();
        query.addWhereGreaterThan("time", localFirstDishTime);
        query.order("-createdAt");
        query.findObjects(OrderDishActivity.this, new FindListener<Dish>() {
            @Override
            public void onSuccess(List<Dish> list) {
                if (list.size() > 0) {
                    mDishAdapter.addNewData(list);
                    localFirstDishTime = list.get(0).getTime();
                } else {
                    Toast.makeText(OrderDishActivity.this, "没有更多数据", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(OrderDishActivity.this, "查询更新菜品失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        /*BmobQuery<Dish> query = new BmobQuery<Dish>();
        query.addWhereGreaterThan("time", localFirstDishTime);
        query.order("-createdAt");
        query.findObjects(OrderDishActivity.this, new FindListener<Dish>() {
            @Override
            public void onSuccess(final List<Dish> list) {
                if (list.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDishActivity.this);
                    builder.setTitle("请注意").setMessage("菜品有更新，现将为您更新新的菜品。")
                            .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDishAdapter.addNewData(list);
                                    localFirstDishTime = list.get(0).getTime();
                                }
                            }).create().show();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(OrderDishActivity.this, "查询失败", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_dish,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_order_dish){
            //进入多选模式
            mDishListView.setItemChecked(0,true);
            mDishListView.clearChoices();
            modeCallBack.updateSelectCount();
            mOrderButton.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.order_button:
                orderedDish();
                break;
            default:
                break;
        }
    }

    private void orderedDish() {
       mCheckStates = mDishListView.getCheckedItemPositions();
        for(int i = 0;i < mCheckStates.size(); ++i){
            if(mCheckStates.valueAt(i) == true){
                int position = mCheckStates.keyAt(i);
                Dish dish = (Dish) mDishAdapter.getItem(position);
                mOrderedList.add(dish);
                setOrderedList(mOrderedList);
                Toast.makeText(this,"点餐成功",Toast.LENGTH_SHORT).show();
            }
        }
        Intent showDishIntent = new Intent(this,ShowDishActivity.class);
        showDishIntent.putExtra(DESKTOP_NAME,mDesktopName);
        showDishIntent.putExtra(DESKTOP_POSITION,mDesktopPosition);
        startActivity(showDishIntent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this,"您点击的是："+position,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,DishInfoActivity.class);
        Dish currentDish = mDishList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("dishName",currentDish.getName());
        bundle.putString("dishDesc",currentDish.getInfo());
        bundle.putDouble("dishPrice",currentDish.getPrice());
        bundle.putString("dishImg",currentDish.getImage());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class ModeCallBack implements AbsListView.MultiChoiceModeListener{
        private View mMultiSelectActionBarView;
        private TextView mSelectedCountTextView;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.multi_select_menu, menu);
            if(mMultiSelectActionBarView == null){
                mMultiSelectActionBarView = LayoutInflater.from(OrderDishActivity.this).inflate(R.layout.list_multi_select_actionbar,null);
                mSelectedCountTextView = (TextView) mMultiSelectActionBarView.findViewById(R.id.selected_conv_count);
            }
            mode.setCustomView(mMultiSelectActionBarView);
            ((TextView)mMultiSelectActionBarView.findViewById(R.id.title)).setText(R.string.select_item);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if(mMultiSelectActionBarView == null){
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(OrderDishActivity.this).inflate(R.layout.list_multi_select_actionbar,null);
                mode.setCustomView(viewGroup);
                mSelectedCountTextView = (TextView) viewGroup.findViewById(R.id.selected_conv_count);

            }
            MenuItem menuItem = menu.findItem(R.id.action_slelect);
            if(mDishListView.getCheckedItemCount() == mDishAdapter.getCount()){
                menuItem.setTitle(R.string.action_deselect_all);
            }else{
                menuItem.setTitle(R.string.action_select_all);
            }
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_slelect:
                    if(mDishListView.getCheckedItemCount() == mDishAdapter.getCount()){
                        UnselectAll();
                    }else{
                        selectAll();
                    }
                    mDishAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mDishListView.clearChoices();
            mOrderButton.setVisibility(View.GONE);
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            updateSelectCount();
            mode.invalidate();
            mDishAdapter.notifyDataSetChanged();
        }

        public void updateSelectCount() {
            mSelectedCountTextView.setText(Integer.toString(mDishListView.getCheckedItemCount()));
        }
    }

    private void selectAll() {
       for(int i = 0;i < mDishAdapter.getCount();i++){
        mDishListView.setItemChecked(i,true);
        }
        modeCallBack.updateSelectCount();
    }

    private void UnselectAll() {
        mDishListView.clearChoices();
        mDishListView.setItemChecked(0,false);
        modeCallBack.updateSelectCount();
    }

    public static List<Dish> getOrderedList() {
        return mOrderedList;
    }

    public static void setOrderedList(List<Dish> mOrderedList) {
        OrderDishActivity.mOrderedList = mOrderedList;
    }

}
