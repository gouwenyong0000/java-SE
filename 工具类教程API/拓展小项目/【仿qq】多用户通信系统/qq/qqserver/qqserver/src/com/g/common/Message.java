package com.g.common;

import java.io.Serializable;

/**
 * 表示客户端和服务端通信时的消息对
 */
public class Message implements Serializable {
    private static final long serialVersionUID = -59111152847205676L;
    private String sender;//发送者
    private String getter;//接受者
    private String content;//消息内容
    private String sendTime;//发送时间
    private String msgType;//消息类型【可以再接口中定义已知的消息类型】

    //进行扩展和文件相关的成员
    private byte[] fileBytes;
    private int fileLen;
    private String dest;//传输到地点
    private String src;//源文件

    public Message(){}

    public Message(String sender, String getter, String content, String sendTime, String msgType) {
        this.sender = sender;
        this.getter = getter;
        this.content = content;
        this.sendTime = sendTime;
        this.msgType = msgType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}

