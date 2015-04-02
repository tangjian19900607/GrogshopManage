package com.grogshop.manage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.Dish;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiamengyu on 3/31/2015.
 */
public class DishAdapter extends BaseAdapter{
    private List<Dish> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ListView mListView;
    DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public DishAdapter(Context context,List<Dish> list,ListView listview) {
            mContext = context;
            mListView = listview;
            mList = list;
            mLayoutInflater = LayoutInflater.from(context);
            initDisplayImageOptions();
    }
    private void initDisplayImageOptions() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
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

    public void setData(List<Dish> list) {
        this.mList = list;
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
        ImageLoader.getInstance().displayImage(mList.get(position).getImage(), viewHolder.image, options, animateFirstListener);
        updateBackground(position,convertView);
        return convertView;
    }
    private static class ViewHolder {
        private ImageView image;
        private TextView info;
        private TextView name;
        private TextView price;
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener{
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }

        }
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
