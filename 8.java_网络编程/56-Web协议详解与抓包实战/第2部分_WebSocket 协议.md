

## Websocket 解决什么问题

### 如何及时获得更新？从轮询到通知

![image-20241022005229882](images/第2部分_WebSocket 协议/image-20241022005229882.png)

### Chrome 请求列表：分析 WebSocket

![image-20241022005455517](images/第2部分_WebSocket 协议/image-20241022005455517.png)

![image-20241022005637740](images/第2部分_WebSocket 协议/image-20241022005637740.png)

## 支持双向通讯的 WebSocket

![image-20241022005813619](images/第2部分_WebSocket 协议/image-20241022005813619.png)

## WebSocket 的成本

![image-20241022005849558](images/第2部分_WebSocket 协议/image-20241022005849558.png)

## 长连接的心跳保持

![image-20241022010054381](images/第2部分_WebSocket 协议/image-20241022010054381.png)

## 兼容 HTTP 协议

![image-20241022010107907](images/第2部分_WebSocket 协议/image-20241022010107907.png)

## 设计哲学：在 Web 约束下暴露 TCP 给上层

![image-20241022010123886](images/第2部分_WebSocket 协议/image-20241022010123886.png)

## 帧格式示意图

![image-20241022010320535](images/第2部分_WebSocket 协议/image-20241022010320535.png)

### 数据帧格式：RSV 保留值

![image-20241022010521046](images/第2部分_WebSocket 协议/image-20241022010521046.png)

### 数据帧格式：帧类型

![image-20241022010535031](images/第2部分_WebSocket 协议/image-20241022010535031.png)

### ABNF 描述的帧格式

![image-20241022010647311](images/第2部分_WebSocket 协议/image-20241022010647311.png)

## 如何从 HTTP 升级到 WebSocket

### URI 格式

![image-20241022010821780](images/第2部分_WebSocket 协议/image-20241022010821780.png)

### 建立握手

![image-20241022010922169](images/第2部分_WebSocket 协议/image-20241022010922169.png)

### 如何证明握手被服务器接受？预防意外

![image-20241022011329608](images/第2部分_WebSocket 协议/image-20241022011329608.png)

## 消息与数据帧

![image-20241022012021854](images/第2部分_WebSocket 协议/image-20241022012021854.png)

### 非控制帧的消息分片：有序

![image-20241022012047311](images/第2部分_WebSocket 协议/image-20241022012047311.png)

### 数据帧格式：消息内容的长度

![image-20241022012221595](images/第2部分_WebSocket 协议/image-20241022012221595.png)

## 发送消息

• 确保 WebSocket 会话处于 OPEN 状态
• 以帧来承载消息，一条消息可以拆分多个数据帧
• 客户端发送的帧必须基于掩码编码
• 一旦发送或者接收到关闭帧，连接处于 CLOSING 状态
• 一旦发送了关闭帧，且接收到关闭帧，连接处于 CLOSED 状态
• TCP 连接关闭后，WebSocket 连接才完全被关闭

## 掩码及其所针对的代理污染攻击

### 针对代理服务器的缓存污染攻击

![image-20241022012522984](images/第2部分_WebSocket 协议/image-20241022012522984.png)

### frame-masking-key 掩码

![image-20241022012825095](images/第2部分_WebSocket 协议/image-20241022012825095.png)

### 掩码如何防止缓存污染攻击？

![image-20241022012847468](images/第2部分_WebSocket 协议/image-20241022012847468.png)

## 心跳帧

![image-20241022013330743](images/第2部分_WebSocket 协议/image-20241022013330743.png)

![image-20241022013513066](images/第2部分_WebSocket 协议/image-20241022013513066.png)



## 关闭会话的方式

![image-20241022013557479](images/第2部分_WebSocket 协议/image-20241022013557479.png)



### 关闭帧格式

![image-20241022013704990](images/第2部分_WebSocket 协议/image-20241022013704990.png)

![image-20241022013744947](images/第2部分_WebSocket 协议/image-20241022013744947.png)

#### 关闭帧的错误码

![image-20241022013804833](images/第2部分_WebSocket 协议/image-20241022013804833.png)