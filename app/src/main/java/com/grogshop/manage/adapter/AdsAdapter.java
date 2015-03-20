package com.grogshop.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.Ads;

import java.util.List;

/**
 * Created by tangjian on 20/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public class AdsAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<Ads> mList;

    public AdsAdapter(Context context, List<Ads> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mList = list;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.activity_ads_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.ads_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.ads_content);
            viewHolder.time = (TextView) convertView.findViewById(R.id.ads_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mList.get(position).getTitle());
        viewHolder.content.setText(mList.get(position).getContent());
        viewHolder.time.setText(mList.get(position).getTime());
        return convertView;
    }

    private static class ViewHolder {
        private TextView title;
        private TextView content;
        private TextView time;
    }
}
