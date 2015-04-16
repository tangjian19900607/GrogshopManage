package com.grogshop.manage.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grogshop.manage.R;
import com.grogshop.manage.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by jiamengyu on 4/15/2015.
 */
public class InfoImageListAdapter extends BaseAdapter{
    private static final String TAG = "InfoImageListAdapterTAG";
    Context mContext;
    String mImgPath;
    private LayoutInflater mLayoutInflater;
    String []arrayString;
    int size ;

    public InfoImageListAdapter(Context context, String imgPath) {
        this.mContext = context;
        this.mImgPath = imgPath;
        mLayoutInflater = LayoutInflater.from(context);
        arrayString= mImgPath.split(",");
        ImageLoaderUtil.initDisplayImageOptions();
        size = arrayString.length;
    }


    @Override
    public int getCount() {
        return arrayString.length - 1;
    }

    @Override
    public Object getItem(int position) {
        return arrayString[position];
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
            convertView = mLayoutInflater.inflate(R.layout.info_imge_item,null);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.info_list_item_imgView);
            viewHolder.mTipTextView = (TextView) convertView.findViewById(R.id.info_list_item_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(size == 1){
            viewHolder.mTipTextView.setVisibility(View.VISIBLE);
            viewHolder.mImageView.setVisibility(View.GONE);
        }else{
            ImageLoader.getInstance().displayImage(arrayString[++position]
                    ,viewHolder.mImageView
                    ,ImageLoaderUtil.options
                    ,ImageLoaderUtil.AnimateFirstDisplayListener);
        }
        return convertView;
    }
    private static class ViewHolder{
        private ImageView mImageView;
        private TextView mTipTextView;
    }
}
