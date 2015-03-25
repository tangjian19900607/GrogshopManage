package com.grogshop.manage.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by tangjian on 25/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public class Waiter extends BmobObject {

    private String name;
    private String time;
    private String type;
    private String zhize;

    public Waiter(String name, String time, String type, String zhize) {
        this.name = name;
        this.time = time;
        this.type = type;
        this.zhize = zhize;
    }

    public Waiter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZhize() {
        return zhize;
    }

    public void setZhize(String zhize) {
        this.zhize = zhize;
    }
}
