package com.grogshop.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.ImageFloder;
import com.grogshop.manage.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiamengyu on 4/9/2015.
 */

public class ListImgDirPopupWindowAdapter extends BaseAdapter{
    private Context mContext;
    private List<ImageFloder> mList;
    /**
     * 用户选择的目录
     */
    public static List<String> mSelectedDir = new LinkedList<String>();

    public ListImgDirPopupWindowAdapter(Context mContext, List<ImageFloder> mList) {
        this.mContext = mContext;
        this.mList = mList;
        ImageLoaderUtil.initDisplayImageOptions();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_list_dir_item,null);
            viewHolder.mDirItemImgView = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
            viewHolder.mDirItemNameTextView = (TextView) convertView.findViewById(R.id.id_dir_item_name);
            viewHolder.mDirItemCountTextView = (TextView) convertView.findViewById(R.id.id_dir_item_count);
            viewHolder.mChooseImageView = (ImageView) convertView.findViewById(R.id.id_dir_choosed);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mDirItemNameTextView.setText(mList.get(position).getName());
        viewHolder.mDirItemCountTextView.setText(mList.get(position).getCount() + "");
        ImageLoader.getInstance().displayImage("file://" + mList.get(position).getFirstImagePath(),
                viewHolder.mDirItemImgView,
                ImageLoaderUtil.options,
                ImageLoaderUtil.AnimateFirstDisplayListener);
        return convertView;
    }

    private static class ViewHolder{
        private ImageView mDirItemImgView;
        private TextView mDirItemNameTextView;
        private TextView mDirItemCountTextView;
        private static ImageView mChooseImageView;

    }

}
