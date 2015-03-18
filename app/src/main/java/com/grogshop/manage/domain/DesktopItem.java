package com.grogshop.manage.domain;

import java.io.Serializable;

/**
 * Created by tangjian on 17/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public class DesktopItem implements Serializable {

    private int position;
    private String name;

    public DesktopItem() {
    }

    public DesktopItem(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
