package com.learn.springsecurity.core.properties;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment: 图形验证码的配置类
 */
public class ImageVerifyCodeProperties extends BaseVerifyCodeProperties {

    public ImageVerifyCodeProperties() {
        setLength(4);
    }

    private int width = 67;
    private int height = 23;


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}