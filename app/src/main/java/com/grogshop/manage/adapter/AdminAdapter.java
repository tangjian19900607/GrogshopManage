package com.grogshop.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.AdminItem;

import java.util.List;

/**
 * Created by tangjian on 16/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public class AdminAdapter extends BaseAdapter {
    private List<AdminItem> mList;
    private LayoutInflater mLayoutInflater;

    public AdminAdapter(Context context, List<AdminItem> list) {
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
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

    public List<AdminItem> getData() {
            return mList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.activity_admin_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(mList.get(position).getName());
        return convertView;
    }
}
