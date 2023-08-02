package com.learn.springsecurity.demo.exceptionhandler.exception;

/**
 * autor:liman
 * createtime:2021/6/30
 * comment:
 */
public class CommonException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -6112780192479692859L;

    private String message;

    public CommonException(String message) {
        super("common exception");
        this.message = message;
    }

    public String getId() {
        return message;
    }

    public void setId(String id) {
        this.message = id;
    }
}
