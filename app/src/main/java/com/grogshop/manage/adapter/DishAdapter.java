package com.grogshop.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grogshop.manage.domain.Dish;
import com.grogshop.manage.domain.Order;

import java.util.List;

/**
 * Created by tangjian on 11/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 * 菜单适配器
 */
public class DishAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<Order> mList;

    public DishAdapter(Context context, List<Order> list) {
        this.mList = list;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            // TODO
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.desktopNumber.setText(mList.get(position).getDesktopNumber());
        viewHolder.customerName.setText(mList.get(position).getOrderName());
        viewHolder.totalMoney.setText(mList.get(position).getTotalMoney() + "");
        viewHolder.watierName.setText(mList.get(position).getWatierName());
        viewHolder.time.setText(mList.get(position).getTime());
        return convertView;
    }

    private static class ViewHolder {
        private TextView desktopNumber;
        private TextView customerName;
        private TextView totalMoney;
        private TextView watierName;
        private TextView time;
    }
}
