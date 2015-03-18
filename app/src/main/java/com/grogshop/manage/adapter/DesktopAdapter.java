package com.grogshop.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.DesktopItem;

import java.util.List;

/**
 * Created by tangjian on 17/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public class DesktopAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<DesktopItem> mList;

    public DesktopAdapter(Context context, List<DesktopItem> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    public List<DesktopItem> getData() {
        return mList;
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
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.activity_main_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.desktop);
        textView.setText(mList.get(position).getName());
        return convertView;
    }
}
