package com.grogshop.manage.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.grogshop.manage.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

public class DishManageActivity extends ActionBarActivity implements View.OnClickListener {

    public static final int CAPTURE_IMAGE = 1;
    private Button mTakePhotoButton;
    private Button mUploadButton;
    private ImageView mImageView;
    private String mCurrentPhotoPath;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);
        String name = getIntent().getStringExtra(AdminMainActivity.NAME);
        setTitle(name);
        initViewId();
        setListener();
    }

    private void initViewId() {
        mTakePhotoButton = (Button) this.findViewById(R.id.take_photo);
        mUploadButton = (Button) this.findViewById(R.id.upload);
        mImageView = (ImageView) this.findViewById(R.id.image);
    }

    private void setListener() {
        mTakePhotoButton.setOnClickListener(this);
        mUploadButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
                uploadImage();
                break;
            case R.id.take_photo:
                takePhoto();
                break;
            default:
                break;
        }
    }

    private void uploadImage() {
        File file = new File(mCurrentPhotoPath);
        BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(DishManageActivity.this, "Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(DishManageActivity.this, "Failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == CAPTURE_IMAGE) && (resultCode == RESULT_OK)) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            mImageView.setImageBitmap(bitmap);
            FileOutputStream b = null;
            File file = new File("/sdcard/myImage/");
            if (!file.exists()) {
                file.mkdirs();// 创建文件夹
            }
            mCurrentPhotoPath = getCacheDir()+"aa.png";
            try {
                b = new FileOutputStream(mCurrentPhotoPath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
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
}
