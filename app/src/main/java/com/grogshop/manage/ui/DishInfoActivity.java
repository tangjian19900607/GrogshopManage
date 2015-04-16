package com.grogshop.manage.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.InfoImageListAdapter;
import com.grogshop.manage.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DishInfoActivity extends ActionBarActivity {
    private static final String TAG = "DishInfoActivityTAG";
    ProgressDialog mProgressDialog;
    String dishName;
    String dishDesc;
    String dishImg;
    Double dishPrice;
    private TextView mNameTextView;
    private TextView mPriceTextView;
    private TextView mDescTextView;
    private ImageView mImageView;
    private ListView mImgListView;
    private ImageLoaderUtil mImageLoaderUtil;
    InfoImageListAdapter mInfoImageListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_info);
        setTitle("菜品详情");
        getIntentValue();
        initProgressDialog();
        initImageLoader();
        initView();
        SetFirstViewData();
        SetSecondViewImg();
    }

    private void SetSecondViewImg() {
        mInfoImageListAdapter = new InfoImageListAdapter(this,dishImg);
        mImgListView.setAdapter(mInfoImageListAdapter);
    }

    private void initImageLoader() {
        mImageLoaderUtil = new ImageLoaderUtil(this);
        ImageLoaderUtil.initDisplayImageOptions();
    }

    private void SetFirstViewData() {
        mNameTextView.setText(dishName);
        mPriceTextView.setText("¥" + dishPrice);
        mDescTextView.setText(dishDesc);
        String[] ImgArray =dishImg.split(",");
        Log.e(TAG,ImgArray[0]);
        ImageLoader.getInstance().displayImage(ImgArray[0]
                ,mImageView
                ,ImageLoaderUtil.options
                ,ImageLoaderUtil.AnimateFirstDisplayListener);
    }

    private void initView() {
        mNameTextView = (TextView) findViewById(R.id.info_title_text);
        mPriceTextView = (TextView) findViewById(R.id.info_price_text);
        mDescTextView = (TextView) findViewById(R.id.info_desc_text);
        mImageView = (ImageView) findViewById(R.id.info_img_imgview);
        mImgListView = (ListView) findViewById(R.id.info_img_listview);
    }

    private void getIntentValue() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            dishName = bundle.getString("dishName");
            dishDesc = bundle.getString("dishDesc");
            dishImg = bundle.getString("dishImg");
            dishPrice = bundle.getDouble("dishPrice");
        }
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setTitle("请稍等");
    }


}
