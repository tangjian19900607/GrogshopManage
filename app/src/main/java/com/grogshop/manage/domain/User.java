package com.grogshop.manage.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by tangjian on 11/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 * 用户实体
 */
public class User extends BmobObject implements Serializable {

    /**
     * 用户类型
     * 1 为管理员 2为顾客
     */
    private String type;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户密码
     */
    private String password;

    public User() {
    }

    public User(String type, String userName, String password) {
        this.type = type;
        this.userName = userName;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
