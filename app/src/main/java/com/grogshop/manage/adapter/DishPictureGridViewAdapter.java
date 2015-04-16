package com.grogshop.manage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.grogshop.manage.R;
import com.grogshop.manage.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import static com.grogshop.manage.R.drawable.icon_addpic_unfocused;

/**
 * Created by jiamengyu on 4/14/2015.
 */
public class DishPictureGridViewAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> mImgList;
    public DishPictureGridViewAdapter(Context context,ArrayList<String> mSelectedImg) {
        mImgList = mSelectedImg;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        ImageLoaderUtil.initDisplayImageOptions();
    }

    public void setmImgList(ArrayList<String> mImgList) {
        this.mImgList = mImgList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.grid_item,null);
            viewHolder.imageview = (ImageView) convertView.findViewById(R.id.grid_item_imgview);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


            ImageLoader.getInstance().displayImage("file://"+mImgList.get(position)
                    , viewHolder.imageview
                    , ImageLoaderUtil.options
                    , ImageLoaderUtil.AnimateFirstDisplayListener);


        return convertView;
    }

    private static class ViewHolder{
       private ImageView imageview;
    }
}
