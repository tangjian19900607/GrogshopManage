package com.grogshop.manage.domain;

import java.io.Serializable;

/**
 * Created by tangjian on 16/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public class AdminItem implements Serializable {

    private int position;
    private String title;

    public AdminItem() {
    }

    public AdminItem(int position, String title) {
        this.position = position;
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
