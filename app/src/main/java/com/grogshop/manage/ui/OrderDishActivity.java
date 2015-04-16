package com.grogshop.manage.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class OrderDishActivity extends ActionBarActivity implements  View.OnClickListener,AdapterView.OnItemClickListener {
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
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在获取数据");
    }

    private void setListener() {
        mOrderButton.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
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
