package com.grogshop.manage.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.grogshop.manage.R;
import com.grogshop.manage.adapter.DishPictureGridViewAdapter;
import com.grogshop.manage.domain.Dish;
import com.grogshop.manage.util.CircleProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by jiamengyu on 3/30/2015.
 */
public class AddDishActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static final String TAG = "AddDishActivityTag";
    private static final int REQUEST_BIG_PICTURE = 2002;
    protected EditText mDishNameEditText;
    protected EditText mDishInfoEditText;
    protected EditText mDishpriceEditText;
    private ImageView mAddPictureButton;
    String saveFilePath;
    //private String mType = "";
    //private String[] mDishType;
    //private Spinner mDishTypeSpinner;
    private GridView mDishGridView;
    private ImageView mDishImageView;
    private ProgressDialog mProgressDialog;
    private Button mAddDishButton;
    public static final int REQUEST_CAPTURE = 1000;
    public static final int REQUEST_CHOOSE = 2000;
    public static final int REQUEST_MORE_CHOOSE = 3000;
    private int SELECT_PICTURE = 1;
    private int SELECT_CAMERA = 0;
    private String mCurrentPhotoPath;
    private Bitmap bmp;
    private Uri outputFileUri;
    ArrayList<String> mSelectedImg;
    DishPictureGridViewAdapter mDishPictureGridViewAdapter;
    CircleProgressBar circleProgressBar;
    private boolean isUploadFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);

        initViewId();
        initProgressDialog();
        setListener();
    }



    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("添加");
        mProgressDialog.setMessage("正在添加新菜品..");
    }

    private void initViewId() {
        mDishImageView = (ImageView) findViewById(R.id.dish_image);
        mDishGridView = (GridView) findViewById(R.id.dish_grid_image);
        mDishGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mDishNameEditText = (EditText) findViewById(R.id.dish_name);
        //mDishTypeSpinner = (Spinner) findViewById(R.id.dish_spinner);
        mDishInfoEditText = (EditText) findViewById(R.id.dish_info);
        mDishpriceEditText = (EditText) findViewById(R.id.dish_price);
        mAddDishButton = (Button) findViewById(R.id.add_dish);
        mAddPictureButton = (ImageView) findViewById(R.id.dish_add_picture_button);
        circleProgressBar = new CircleProgressBar(this,null);
        circleProgressBar = (CircleProgressBar) findViewById(R.id.circle_progressbar);
    }

    private void setListener() {
        //mDishTypeSpinner.setOnItemSelectedListener(this);
        mAddDishButton.setOnClickListener(this);
        mAddPictureButton.setOnClickListener(this);
        mDishGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddDishActivity.this, PictureActivity.class);
                intent.putExtra("ID", position);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("SelectedImages",
                        mSelectedImg);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_BIG_PICTURE);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_dish:
                //AddDish();
                AddDishBatch();
                break;
            case R.id.dish_add_picture_button:
                AddDishPicture();
                break;
        }

    }

    private void AddDishPicture() {
        String[] obtainPicture = new String[]{"打开相机", "从相册选择"};
        Dialog alertDialog = new AlertDialog.Builder(this)
                .setItems(obtainPicture, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == SELECT_CAMERA) {
                            capturePicture();
                        } else if (which == SELECT_PICTURE) {
                            //choosePicture();
                            chooseMorePicture();
                        }
                    }
                }).create();
        alertDialog.show();

    }

    /**
     * 选择图片（多选）
     */
    private void chooseMorePicture() {
        mDishImageView.setVisibility(View.GONE);
        mDishGridView.setVisibility(View.VISIBLE);
        Intent chooseMorePictureIntent = new Intent(this, ChooseMorePictureActivity.class);
        startActivityForResult(chooseMorePictureIntent, REQUEST_MORE_CHOOSE);
    }

    /**
     * 从相册选择（单选）
     */
    private void choosePicture() {
        mDishImageView.setVisibility(View.VISIBLE);
        mDishGridView.setVisibility(View.GONE);
        Intent choosePictureIntent = new Intent();
        choosePictureIntent.setType("image/*");
        choosePictureIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(choosePictureIntent, REQUEST_CHOOSE);

    }

    private void capturePicture() {
        File file = new File(Environment.getExternalStorageDirectory(), "textphoto.jpg");
        outputFileUri = Uri.fromFile(file);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(takePictureIntent, REQUEST_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle;
        if (resultCode == RESULT_OK) {
            mDishGridView.setVisibility(View.GONE);
            mDishImageView.setVisibility(View.VISIBLE);
            switch (requestCode) {
                case REQUEST_CHOOSE://从图库选择图片---单选
                    Uri uri = data.getData();
                    mCurrentPhotoPath = uri2filePath(uri);
                    if (bmp != null) {
                        bmp.recycle();
                    }
                    bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    mDishImageView.setImageBitmap(bmp);
                    break;
                case REQUEST_CAPTURE:
                    mCurrentPhotoPath = outputFileUri.getPath();
                    bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    mDishImageView.setImageBitmap(bmp);
                    break;
            }
        }

        if (requestCode == REQUEST_MORE_CHOOSE && resultCode == ChooseMorePictureActivity.CHOOSE_FINISH_RESULT) {
            bundle = data.getExtras();
            if (bundle != null) {
                mSelectedImg = bundle.getStringArrayList("SelectedImage");
            }
            if (mSelectedImg.size() == 1) {
                mDishImageView.setVisibility(View.VISIBLE);
                mDishGridView.setVisibility(View.GONE);
                mCurrentPhotoPath = mSelectedImg.get(0);
                if (bmp != null) {
                    bmp.recycle();
                }
                bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
                mDishImageView.setImageBitmap(bmp);
            } else {
                Log.e(TAG, "mSelectedImg::::" + mSelectedImg);
                mDishPictureGridViewAdapter = new DishPictureGridViewAdapter(AddDishActivity.this, mSelectedImg);
                mDishGridView.setAdapter(mDishPictureGridViewAdapter);
            }
            //mCurrentPhotoPath = getStringpathByList(mSelectedImg);
        }
        if (requestCode == REQUEST_BIG_PICTURE && resultCode == PictureActivity.RESULT_FINSH_PICTURE) {
            bundle = data.getExtras();
            if (bundle != null) {
                mSelectedImg = bundle.getStringArrayList("finalChoosedImg");
            }
            Log.e(TAG, "mfinalChoosedImg!!!!" + mSelectedImg);
            mDishPictureGridViewAdapter.setmImgList(mSelectedImg);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mDishPictureGridViewAdapter) {
            mDishPictureGridViewAdapter.notifyDataSetChanged();
        }
        mDishGridView.setAdapter(mDishPictureGridViewAdapter);
    }

    private String uri2filePath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        return path;

    }

    public String getStringpathByList(List<String> list) {
        String[] ImgsPath = list.toArray(new String[list.size()]);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ImgsPath.length; i++) {
            sb.append(ImgsPath[i] + ",");
        }
        String path = String.valueOf(sb);
        Log.e(TAG, path);
        return path;
    }

    private void AddDishBatch() {
        String[] selectImgPath = mSelectedImg.toArray(new String[mSelectedImg.size()]);
        Bmob.uploadBatch(this, selectImgPath, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> filelist, List<String> urllist) {
                if(isUploadFinish){
                    saveFilePath = getStringpathByList(urllist);
                    String name = mDishNameEditText.getText().toString().trim();
                    String info = mDishInfoEditText.getText().toString().trim();
                    String price = mDishpriceEditText.getText().toString().trim();

                    if (price == null || "".equals(price) || saveFilePath == null || "".equals(saveFilePath)) {
                        Toast.makeText(AddDishActivity.this, "请输入单价/照片", Toast.LENGTH_SHORT).show();
                    } else {
                        mProgressDialog.show();
                        Dish dish = new Dish();
                        dish.setName(null == name || "".equals(name) ? "暂无名称" : name);
                        dish.setInfo(null == info || "".equals(info) ? "暂无简介" : info);
                        dish.setPrice(Double.parseDouble(price));
                        dish.setImage(saveFilePath);
                        dish.setTime(new Date().getTime());
                        dish.save(AddDishActivity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                                Toast.makeText(AddDishActivity.this, "添加新菜品成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                                Toast.makeText(AddDishActivity.this, "添加菜品失败：" + s, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                if (totalPercent == 100) {
                    isUploadFinish = true;
                }
                Log.e(TAG, totalPercent + "%");
                Log.e(TAG, "上传文件：" + total + "个");
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(AddDishActivity.this, "上传图片失败:" + i + ",原因：" + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // mDishType = getResources().getStringArray(R.array.dish_type);
        // mType = mDishType[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
