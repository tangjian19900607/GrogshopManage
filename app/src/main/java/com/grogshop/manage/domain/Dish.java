package com.grogshop.manage.domain;

import java.io.Serializable;

/**
 * Created by tangjian on 11/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 * 菜品的实体
 */
public class Dish implements Serializable {
    /**
     * 菜的名称
     */
    private String name;
    /**
     * 菜的图片
     */
    private String image;
    /**
     * 菜的单价
     */
    private double price;
    /**
     * 菜的介绍
     */
    private String info;

    public Dish() {
    }

    public Dish(String name, String image, double price, String info) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
