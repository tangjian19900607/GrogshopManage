package com.grogshop.manage.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by tangjian on 19/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public class Ads extends BmobObject {
    private String title;
    private String content;
    private String time;

    public Ads() {
    }

    public Ads(String title, String content, String time) {
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
