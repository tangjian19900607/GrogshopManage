package com.grogshop.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grogshop.manage.R;
import com.grogshop.manage.domain.Waiter;

import java.util.List;

/**
 * Created by tangjian on 25/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public class WatierAdapter extends BaseAdapter {

    private List<Waiter> mList;
    private LayoutInflater mLayoutInflater;

    public WatierAdapter(Context context, List<Waiter> list) {
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }
    public List<Waiter> getData() {
        return this.mList;
    }

    public void setData(List<Waiter> list) {
        this.mList = list;
        notifyDataSetChanged();
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
            convertView = mLayoutInflater.inflate(R.layout.activity_person_watier_item, null);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.zhize = (TextView) convertView.findViewById(R.id.zhize);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.type.setText(mList.get(position).getType());
        viewHolder.time.setText(mList.get(position).getTime());
        viewHolder.name.setText(mList.get(position).getName());
        viewHolder.zhize.setText(mList.get(position).getZhize());
        return convertView;
    }

    private static class ViewHolder {
        private TextView type;
        private TextView time;
        private TextView name;
        private TextView zhize;
    }
}
