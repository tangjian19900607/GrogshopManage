package com.grogshop.manage.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.grogshop.manage.R;
import com.grogshop.manage.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by jiamengyu on 4/7/2015.
 */
public class PictureChooserAdapter extends BaseAdapter{
    /**
     * 用户选择的图片（存储为全路径）
     */
    public static List<String> mSelectedImage = new ArrayList<String>();

    /**
     * 文件夹路径
     */
    private String mDirPath;
    private Context mContext;
    private List<String> mDatas;
    LayoutInflater mInflater;

    public PictureChooserAdapter(Context context,List<String> datas,String dirPath){
        this.mContext = context;
        this.mDatas = datas;
        this.mDirPath = dirPath;
        mInflater = LayoutInflater.from(context);
        ImageLoaderUtil.initDisplayImageOptions();
    }

    public static List<String> getSelectedImage() {
        return mSelectedImage;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_choose_picture_grid_item,null);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.id_choose_grid_item_select);
            holder.imageView = (ImageView) convertView.findViewById(R.id.id_choose_grid_item_image);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(R.drawable.pictures_no);
        holder.imageButton.setImageResource(R.drawable.picture_unselected);
        ImageLoader.getInstance().displayImage("file://" + mDirPath + "/" + mDatas.get(position), holder.imageView,
                ImageLoaderUtil.options, ImageLoaderUtil.AnimateFirstDisplayListener);
        holder.imageView.setColorFilter(null);
        final ViewHolder finalHolder = holder;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //已选择过该图片
                if(mSelectedImage.contains(mDirPath + "/" + mDatas.get(position))){
                    mSelectedImage.remove(mDirPath + "/" + mDatas.get(position));
                    finalHolder.imageButton.setImageResource(R.drawable.picture_unselected);
                    finalHolder.imageView.setBackgroundResource(0x00000000);

                }else{
                    //未选择过该图片
                    mSelectedImage.add(mDirPath + "/" + mDatas.get(position));
                    finalHolder.imageButton.setImageResource(R.drawable.pictures_selected);
                    finalHolder.imageView.setBackgroundResource(R.drawable.bgd_relatly_line);
                }
            }
        });
        //已选择的图片设置显示效果
        if(mSelectedImage.contains(mDirPath + "/" + mDatas.get(position))){
            holder.imageButton.setImageResource(R.drawable.pictures_selected);
            holder.imageView.setBackgroundResource(R.drawable.bgd_relatly_line);
        }

        return convertView;
    }
/*
    public interface OnChooseListner{
        void onPictureChoosed();
    }

    private OnChooseListner onChooseListner;

    public void setOnChooseListner(OnChooseListner onChooseListner) {
        this.onChooseListner = onChooseListner;
    }*/

    private static class ViewHolder{
        private ImageView imageView;
        private ImageButton imageButton;

    }


}
