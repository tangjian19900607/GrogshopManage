package com.grogshop.manage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.Dish;
import com.grogshop.manage.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import static com.grogshop.manage.util.ImageLoaderUtil.*;

/**
 * Created by jiamengyu on 3/31/2015.
 */
public class DishAdapter extends BaseAdapter{
    private static final String TAG = "DishAdapterTAG";
    private List<Dish> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ListView mListView;

    public DishAdapter(Context context,List<Dish> list,ListView listview) {
            mContext = context;
            mListView = listview;
            mList = list;
            mLayoutInflater = LayoutInflater.from(context);
            ImageLoaderUtil.initDisplayImageOptions();
    }




    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Dish> getData() {
        return this.mList;
    }

    public void addNewData(List<Dish> list) {
        list.addAll(mList);
        mList = list;
        notifyDataSetChanged();
    }
    public void setData(List<Dish> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void addData(List<Dish> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.activity_dish_item,null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.dish_show_name_text);
            viewHolder.price = (TextView) convertView.findViewById(R.id.dish_show_price_text);
            viewHolder.info = (TextView) convertView.findViewById(R.id.dish_show_info_text);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.dish_show_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(mList.get(position).getName());
        viewHolder.price.setText("¥"+mList.get(position).getPrice());
        viewHolder.info.setText(mList.get(position).getInfo());
        //拿到图片集的第一张图片
        String[] ImgArray =mList.get(position).getImage().split(",");
        ImageLoader.getInstance().displayImage(ImgArray[0], viewHolder.image,
                options, ImageLoaderUtil.AnimateFirstDisplayListener);
        updateBackground(position, convertView);
        return convertView;
    }



    private static class ViewHolder {
        private ImageView image;
        private TextView info;
        private TextView name;
        private TextView price;
    }

    @SuppressLint("NewApi")
    public void updateBackground(int position, View view) {
        int backgroundId;
        if (mListView.isItemChecked(position)) {
            backgroundId = R.drawable.list_selected_holo_light;
        } else {
            backgroundId = R.drawable.conversation_item_background_read;
        }
        Drawable background = mContext.getResources().getDrawable(backgroundId);
        view.setBackground(background);
    }


}
