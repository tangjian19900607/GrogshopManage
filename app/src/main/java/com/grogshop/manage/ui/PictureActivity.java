package com.grogshop.manage.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.PicturePageAdapter;
import com.grogshop.manage.util.ImageLoaderUtil;

import java.util.ArrayList;

/**
 * Created by jiamengyu on 4/14/2015.
 */
public class PictureActivity extends Activity implements View.OnClickListener{
    public static final int RESULT_FINSH_PICTURE = 8008;
    private ViewPager mViewPager;
    private RelativeLayout mBottomLayout;
    private PicturePageAdapter mPicturePageAdapter;
    ArrayList<String> mSelectedImg;
    ImageLoaderUtil mImageLoaderUtil;
    private int mId;
    private Button mExitButton;
    private Button mEnterButton;
    private Button mDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        getIntentValue();
        initImageLoader();
        initView();
        setListener();
    }

    private void initImageLoader() {
        mImageLoaderUtil = new ImageLoaderUtil(this);
        ImageLoaderUtil.initDisplayImageOptions();
    }


    private void setListener() {
        mViewPager.setOnPageChangeListener(pageChangeListener);
        mDeleteButton.setOnClickListener(this);
        mEnterButton.setOnClickListener(this);
        mExitButton.setOnClickListener(this);
    }


    private void initView() {
        mBottomLayout = (RelativeLayout) findViewById(R.id.id_viewpager_bottom_layout);
        mBottomLayout.setBackgroundColor(0x70000000);
        mViewPager = (ViewPager) findViewById(R.id.id_picture_viewpager);
        mPicturePageAdapter = new PicturePageAdapter(this,mSelectedImg);
        mViewPager.setAdapter(mPicturePageAdapter);
        mViewPager.setCurrentItem(mId);
        mDeleteButton = (Button) findViewById(R.id.photo_bt_del);
        mEnterButton = (Button) findViewById(R.id.photo_bt_enter);
        mExitButton = (Button) findViewById(R.id.photo_bt_exit);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {//滑动中

        }

        @Override
        public void onPageSelected(int i) {//页面选择响应

            //mId当前选择位置，刚开始没有滑动过时是AddDishActivity页面gridview点击事件传过来的position
            // ，当开始滑动后，position动态改变。
            mId = i;
            //Toast.makeText(PictureActivity.this,"selected:"+i,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPageScrollStateChanged(int i) {//滑动状态改变

        }
    };

    public void getIntentValue() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mSelectedImg = bundle.getStringArrayList("SelectedImages");
        }
        mId = getIntent().getIntExtra("ID",0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.photo_bt_del:
                if(mSelectedImg.size() > 0){
                    mSelectedImg.remove(mId);
                    mPicturePageAdapter.setmSelectedImg(mSelectedImg);
                    mViewPager.setAdapter(mPicturePageAdapter);
                }else{
                    mDeleteButton.setEnabled(false);
                    Toast.makeText(PictureActivity.this,"没有更多可以删除的已选图片。",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.photo_bt_enter:
                mPicturePageAdapter.setmSelectedImg(mSelectedImg);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("finalChoosedImg", mSelectedImg);
                intent.putExtras(bundle);
                setResult(RESULT_FINSH_PICTURE,intent);
                finish();
                break;
            case R.id.photo_bt_exit:
                finish();
                break;
            default:
                break;
        }
    }
}
