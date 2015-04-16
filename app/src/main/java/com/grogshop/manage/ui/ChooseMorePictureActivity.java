package com.grogshop.manage.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.PictureChooserAdapter;
import com.grogshop.manage.domain.ImageFloder;
import com.grogshop.manage.util.ImageLoaderUtil;
import com.grogshop.manage.util.ListImageDirPopupWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by jiamengyu on 4/7/2015.
 */
public class ChooseMorePictureActivity extends Activity implements View.OnClickListener{
    protected static final int CHOOSE_FINISH_RESULT = 700;
    private DisplayMetrics outMetrics;
    private int mScreenHeight;
    private GridView mPictureGridView;
    private TextView mChooseDirTextView;
    private TextView mImageCountTextView;
    private Button mFinishButton;
    private RelativeLayout mBottomLayout;
    private ProgressDialog mProgressDialog;
    private ContentResolver mContentResolver;
    private Cursor mCursor;
    ImageLoaderUtil mImageLoaderUtil;
    private static final String TAG = "ChooseMorePictureTag";
    /**
     * 扫描拿到所有的图片的文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    /**
     * 临时的辅助HashSet，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    /**
     * 存储文件夹的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    int totalCount;
    /**
     * 所有图片
     */
    private List<String> mImgs;
    ListImageDirPopupWindow mListImageDirPopupWindow;
    PictureChooserAdapter mPictureChooserAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 0x110){
                mProgressDialog.dismiss();
                //给View绑定数据
                data2View();
                initListDirPopupWindow();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_more_picture);
        init();
        initView();
        initImage();
        initEvent();
    }



    private void initEvent() {
        mFinishButton.setOnClickListener(this);
        mBottomLayout.setOnClickListener(this);
    }


    private void init() {
        mImageLoaderUtil = new ImageLoaderUtil(this);
        outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
    }

    private void initView() {
        mPictureGridView = (GridView) findViewById(R.id.id_more_picture_gridview);
        mChooseDirTextView = (TextView) findViewById(R.id.id_choose_dir);
        mImageCountTextView = (TextView) findViewById(R.id.id_total_count);
        mBottomLayout = (RelativeLayout) findViewById(R.id.id_bottom_layout);
        mFinishButton = (Button) findViewById(R.id.id_finish_choose_picture);
        if(PictureChooserAdapter.mSelectedImage.size() != 0){
            PictureChooserAdapter.mSelectedImage.clear();
        }
    }


    private void initImage() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"外部存储不存在",Toast.LENGTH_LONG).show();
            return;
        }
        mProgressDialog = ProgressDialog.show(this,null,"正在扫描…");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                mContentResolver = ChooseMorePictureActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED);

                Log.e(TAG, mCursor.getCount() + "");
                while (mCursor.moveToNext())
                {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    Log.e(TAG, path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹
                    if (mDirPaths.contains(dirPath))
                    {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }


                    int picSize = 0;
                    File []files = parentFile.listFiles();
                    if (null != files){
                        for(int i = 0; i < files.length; i++) {
                            if (files[i].isFile()) {
                                picSize++;
                            }
                        }
                    }
                    Log.e(TAG,"picSize:"+picSize);
                    totalCount += picSize;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                    if (picSize > mPicsSize)
                    {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_finish_choose_picture:
                finishChoosePicture();
                break;
            case R.id.id_bottom_layout:
                mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(mBottomLayout,0,0);
                //设置背景颜色变暗
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = .3f;
                getWindow().setAttributes(layoutParams);

                break;
            default:
                break;
        }
    }

    private void finishChoosePicture() {
        if(PictureChooserAdapter.mSelectedImage.size() == 0){
            Toast.makeText(this,"您还没有选择图片哦",Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(this,AddDishActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("SelectedImage",
                    (ArrayList<String>) PictureChooserAdapter.mSelectedImage);
            intent.putExtras(bundle);
            setResult(CHOOSE_FINISH_RESULT, intent);
            finish();
        }

    }


    private void data2View() {
        if(mImgDir == null){
            Toast.makeText(this,"没有扫描到图片！",Toast.LENGTH_LONG).show();
            return;
        }
        mImgs = Arrays.asList(mImgDir.list());
        mPictureChooserAdapter = new PictureChooserAdapter(this,mImgs,mImgDir.getAbsolutePath());
        mPictureGridView.setAdapter(mPictureChooserAdapter);
        mImageCountTextView.setText(totalCount + "张");
    }

    private void initListDirPopupWindow() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(this,
                ActionBar.LayoutParams.MATCH_PARENT,
                (int)(mScreenHeight * 0.7),
                mImageFloders,
                LayoutInflater.from(this).inflate(R.layout.popup_window_dir_list,
                        null));
        mListImageDirPopupWindow.setOnImgDirSelected(new ListImageDirPopupWindow.OnImgDirSelected() {
            @Override
            public void selected(ImageFloder imageFloder) {
                PictureChooserAdapter.mSelectedImage.clear();
                mImgDir = new File(imageFloder.getDir());
                mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") ||
                                filename.endsWith(".jpeg") ||
                                filename.endsWith(".png"))
                            return true;
                        return false;
                    }
                }));
                mPictureChooserAdapter = new PictureChooserAdapter(ChooseMorePictureActivity.this, mImgs, mImgDir.getAbsolutePath());
                mPictureGridView.setAdapter(mPictureChooserAdapter);
                mImageCountTextView.setText(imageFloder.getCount() + "张");
                mChooseDirTextView.setText(imageFloder.getName());
                mListImageDirPopupWindow.dismiss();
            }
        });
        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //设置背景颜色变亮
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = 1.0f;
                getWindow().setAttributes(layoutParams);
            }
        });
    }
}
