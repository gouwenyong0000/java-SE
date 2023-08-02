package com.learn.springsecurity.core.api;
/**
 * Created by Administrator on 2020/2/6.
 */

/**
 * 接口的统一响应模型 ~ 规范
 * @Author:liman
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/2/6 17:26
 **/
public class BaseResponse<T> {

    private Integer code;
    private String msg;
    private T data;

    public BaseResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public BaseResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}