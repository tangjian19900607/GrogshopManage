package com.grogshop.manage.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by tangjian on 25/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public class Order extends BmobObject{
    /**
     * 订餐者
     */
    private String orderName;
    /**
     * 结账时间
     */
    private String time;
    /**
     * 总额
     */
    private double totalMoney;
    /**
     * 收银员姓名
     */
    private String watierName;
    /**
     * 餐桌号
     */
    private String desktopNumber;

    public Order(String orderName, String time, double totalMoney, String watierName, String desktopNumber) {
        this.orderName = orderName;
        this.time = time;
        this.totalMoney = totalMoney;
        this.watierName = watierName;
        this.desktopNumber = desktopNumber;
    }

    public Order() {
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getWatierName() {
        return watierName;
    }

    public void setWatierName(String watierName) {
        this.watierName = watierName;
    }

    public String getDesktopNumber() {
        return desktopNumber;
    }

    public void setDesktopNumber(String desktopNumber) {
        this.desktopNumber = desktopNumber;
    }
}
