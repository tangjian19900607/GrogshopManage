package com.grogshop.manage.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.grogshop.manage.R;
import com.grogshop.manage.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by jiamengyu on 4/14/2015.
 */
public class PicturePageAdapter extends PagerAdapter {
    private ArrayList<String> mSelectedImg;
    private Context mContext;

    public PicturePageAdapter(Context context,ArrayList<String> imglist ) {
        mContext = context;
        mSelectedImg = imglist;
    }

    public void setmSelectedImg(ArrayList<String> mSelectedImg) {
        this.mSelectedImg = mSelectedImg;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return null == mSelectedImg ? 0 : mSelectedImg.size();
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View imageItemView = LayoutInflater.from(mContext).inflate(R.layout.view_pager_item,null);
        ImageView imageView = (ImageView) imageItemView.findViewById(R.id.id_viewpager_image_view);
        ImageLoader.getInstance().displayImage("file://"+mSelectedImg.get(position)
                ,imageView
                ,ImageLoaderUtil.options
                ,ImageLoaderUtil.AnimateFirstDisplayListener);
        container.addView(imageItemView,0);
        return imageItemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }


}
