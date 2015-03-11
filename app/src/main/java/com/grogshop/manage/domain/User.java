package com.grogshop.manage.domain;

import java.io.Serializable;

/**
 * Created by tangjian on 11/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 * 用户实体
 */
public class User implements Serializable {

    /**
     * 用户类型
     * 1 为管理员 2为顾客
     */
    private String type;
    /**
     * 用户名称
     */
    private String userName;

    public User() {
    }

    public User(String type, String userName) {
        this.type = type;
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
