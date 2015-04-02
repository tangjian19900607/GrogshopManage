package com.grogshop.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grogshop.manage.R;
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
public class OrderAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<Order> mList;

    public OrderAdapter(Context context, List<Order> list) {
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
    public List<Order> getData() {
        return this.mList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.activity_order_item, null);
            viewHolder.desktopNumber = (TextView) convertView.findViewById(R.id.desktop_number);
            viewHolder.customerName = (TextView) convertView.findViewById(R.id.customer_name);
            viewHolder.totalMoney = (TextView) convertView.findViewById(R.id.totoal_money);
            viewHolder.watierName = (TextView) convertView.findViewById(R.id.watier_name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.order_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.desktopNumber.setText("餐桌:"+mList.get(position).getDesktopNumber());
        viewHolder.customerName.setText("顾客:"+mList.get(position).getOrderName());
        viewHolder.totalMoney.setText("消费:"+mList.get(position).getTotalMoney() + "元");
        viewHolder.watierName.setText("操作员:"+mList.get(position).getWatierName());
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
