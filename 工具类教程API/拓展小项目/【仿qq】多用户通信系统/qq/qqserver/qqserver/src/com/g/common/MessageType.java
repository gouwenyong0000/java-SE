package com.g.common;

/**
 * 消息类型
 * 1，在接口中定义了一些常量
 * 2，不同的常量的值，表示不同的消息类型。
 */
public interface MessageType {
    /**表示登录成功     */
    String MESSAGE_LOGIN_SUCCEED = "1";
    /**表示登录失败     */
    String MESSAGE_LOGIN_FAIL = "2";
    /**表示登录失败     */
    String MESSAGE_COMM_MES  = "3";
    /**表示登录失败     */
    String MESSAGE_GET_ONLINE_FRIEND = "4";
    /**返回在线用户列表    */
    String MESSAGE_RET_ONLINE_FRIEND = "5";
    /**客户端请求退出    */
    String MESSAGE_CLIENT_EXIT = "6";
    /**群发消息    */
    String MESSAGE_TO_ALL = "7";
    /**文件消息    */
    String MESSAGE_FILE_MSG = "8";

}
