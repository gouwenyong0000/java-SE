> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [blog.csdn.net](https://blog.csdn.net/weixin_37750248/article/details/131064293)

什么是 [WebSocket](https://so.csdn.net/so/search?q=WebSocket&spm=1001.2101.3001.7020)？
-----------------------------------------------------------------------------------

随着互联网的发展，传统的 [HTTP 协议](https://so.csdn.net/so/search?q=HTTP%E5%8D%8F%E8%AE%AE&spm=1001.2101.3001.7020)已经很难满足 Web 应用日益复杂的需求了。近年来，随着 HTML5 的诞生，WebSocket 协议被提出，**它实现了浏览器与服务器的全双工通信，扩展了浏览器与服务端的通信功能，使服务端也能主动向客户端发送数据**。  
　　我们知道，传统的 HTTP 协议是无状态的，每次请求（request）都要由客户端（如 浏览器）主动发起，服务端进行处理后返回 response 结果，而**服务端很难主动向客户端发送数据**；这种客户端是主动方，服务端是被动方的传统 Web 模式 对于信息变化不频繁的 Web 应用来说造成的麻烦较小，而对于涉及实时信息的 Web 应用却带来了很大的不便，如带有即时通信、实时数据、订阅推送等功能的应 用。在 WebSocket 规范提出之前，开发人员若要实现这些实时性较强的功能，经常会使用折衷的解决方法：**轮询（polling）和 Comet 技术**。其实后者本质上也是一种轮询，只不过有所改进。  
　　**轮询**是最原始的实现实时 Web 应用的解决方案。轮询技术要求客户端以设定的时间间隔周期性地向服务端发送请求，频繁地查询是否有新的数据改动。明显地，这种方法会导致过多不必要的请求，浪费流量和服务器资源。  
　　**Comet 技术**又可以分为长轮询和流技术。长轮询改进了上述的轮询技术，减小了无用的请求。它会为某些数据设定过期时间，当数据过期后才会向服务端发送请求；这种机制适合数据的改动不是特别频繁的情况。流技术通常是指客户端使用一个隐藏的窗口与服务端建立一个 HTTP 长连接，服务端会不断更新连接状态以保持 HTTP 长连接存活；这样的话，服务端就可以通过这条长连接主动将数据发送给客户端；流技术在大并发环境下，可能会考验到服务端的性能。  
　　这两种技术都是基于**请求 - 应答模式**，都不算是真正意义上的实时技术；它们的每一次请求、应答，都浪费了一定流量在相同的头部信息上，并且开发复杂度也较大。  
　　伴随着 HTML5 推出的 WebSocket，真正实现了 Web 的实时通信，使 B/S 模式具备了 C/S 模式的实时通信能力。**WebSocket 的工作流程是这样的：浏览器通过 JavaScript 向服务端发出建立 WebSocket 连接的请求，在 WebSocket 连接建立成功后，客户端和服务端就可以通过 TCP 连接传输数据。因为 WebSocket 连接本质上是 TCP 连接，不需要每次传输都带上重复的头部数据，所以它的数据传输量比轮询和 Comet 技术小 了很多**。本文不详细地介绍 WebSocket 规范，主要介绍下 WebSocket 在 Java Web 中的实现。  
　　JavaEE 7 中出了 JSR-356:Java API for WebSocket 规范。不少 Web 容器，如 Tomcat,Nginx,Jetty 等都支持 WebSocket。Tomcat 从 7.0.27 开始支持 WebSocket，从 7.0.47 开始支持 JSR-356，下面的 Demo 代码也是需要部署在 Tomcat7.0.47 以上的版本才能运行。  
　　**WebSocket 是 HTML5 开始提供的一种在单个 TCP 连接上进行全双工通讯的协议。**  
　　在 WebSocket API 中，浏览器和服务器只需要做一个握手的动作，然后，浏览器和服务器之间就形成了一条快速通道。两者之间就直接可以数据互相传送。  
　　浏览器通过 JavaScript 向服务器发出建立 WebSocket 连接的请求，连接建立以后，客户端和服务器端就可以通过 TCP 连接直接交换数据。  
　　当你获取 Web Socket 连接后，你可以通过 send() 方法来向服务器发送数据，并通过 onmessage 事件来接收服务器返回的数据。  
以下 API 用于创建 WebSocket 对象。

```
var Socket = new WebSocket(url, [protocol] );
```

以上代码中的第一个参数 url, 指定连接的 URL。第二个参数 protocol 是可选的，指定了可接受的子协议。

Tomcat 实现 websocket 方法
----------------------

### 服务端代码

```java
/**
* 服务器
*
*/
@ServerEndpoint("/webSocketByTomcat/{username}")  
public class WebSocket {  
   private static int onlineCount = 0;  
   private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();  
   private Session session;  
   private String username;  
     
   @OnOpen  
   public void onOpen(@PathParam("username") String username, Session session) throws IOException {  
 
       this.username = username;  
       this.session = session;  
         
       addOnlineCount();  
       clients.put(username, this);  
       System.out.println("已连接");  
       
   }  
 
   @OnClose  
   public void onClose() throws IOException {  
       clients.remove(username);  
       subOnlineCount();  
   }  
 
   @OnMessage  
   public void onMessage(String message) throws IOException {  
       JSONObject jsonTo = JSONObject.parseObject(message);  
       System.out.println(jsonTo.getString("to") +":"+ jsonTo.getString("msg"));
         
       if (!jsonTo.getString("to").toLowerCase().equals("all")){  
           sendMessageTo(jsonTo.getString("msg"), jsonTo.getString("to"));  
       }else{  
           sendMessageAll(jsonTo.getString("msg"));  
       }  
   }  
 
   @OnError  
   public void onError(Session session, Throwable error) {  
       error.printStackTrace();  
   }  
 
   public void sendMessageTo(String message, String To) throws IOException {  
       // session.getBasicRemote().sendText(message);  
       //session.getAsyncRemote().sendText(message);  
       for (WebSocket item : clients.values()) {  
           if (item.username.equals(To) )  
               item.session.getAsyncRemote().sendText(message);  
       }  
   }  
     
   public void sendMessageAll(String message) throws IOException {  
       for (WebSocket item : clients.values()) {  
           item.session.getAsyncRemote().sendText(message);  
       }  
   }  
     
     
 
   public static synchronized int getOnlineCount() {  
       return onlineCount;  
   }  
 
   public static synchronized void addOnlineCount() {  
       WebSocket.onlineCount++;  
   }  
 
   public static synchronized void subOnlineCount() {  
       WebSocket.onlineCount--;  
   }  
 
   public static synchronized Map<String, WebSocket> getClients() {  
       return clients;  
   }  
}
```

### 客户端代码

注意导入 socketjs 时要使用地址全称，并且连接使用的是 http 而不是 websocket 的 ws

```jsp
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ctxpath"
   value="${pageContext.request.scheme}${'://'}${pageContext.request.serverName}${':'}${pageContext.request.serverPort}${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset=UTF-8">
<title>登录测试</title>
</head>
<body>
   <h2>Hello World!</h2>
   <div>
       <span>sessionId:</span>
       <% 
           HttpSession s= request.getSession(); 
           out.println(s.getId());
       %>
   </div>
   
   <input id="sessionId" type="hidden" value="<%=session.getId() %>" />
   <input id="text" type="text" />
   <button onclick="send()">发送消息</button>
   <hr />
   <button onclick="closeWebSocket()">关闭WebSocket连接</button>
   <hr />
   <div id="message"></div>
</body>
<script type="text/javascript" src="http://localhost:8088/static/js/sockjs-0.3.min.js"></script> 
<script type="text/javascript">  
       var websocket = null;  
       if('WebSocket' in window) {
           websocket = new WebSocket("ws://localhost:8088/websocket/webSocketByTomcat/"+document.getElementById('sessionId').value);  
       } else if('MozWebSocket' in window) {
           websocket = new MozWebSocket("ws://localhost:8088/websocket/webSocketByTomcat/"+document.getElementById('sessionId').value);
       } else {
           websocket = new SockJS("localhost:8088/websocket/webSocketByTomcat/"+document.getElementById('sessionId').value);
       }
     
       //连接发生错误的回调方法  
       websocket.onerror = function () {  
           setMessageInnerHTML("WebSocket连接发生错误");  
       };  
     
       //连接成功建立的回调方法  
       websocket.onopen = function () {  
           setMessageInnerHTML("WebSocket连接成功");  
       }  
     
       //接收到消息的回调方法  
       websocket.onmessage = function (event) {  
           setMessageInnerHTML(event.data);  
       }  
     
       //连接关闭的回调方法  
       websocket.onclose = function () {  
           setMessageInnerHTML("WebSocket连接关闭");  
       }  
     
       //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。  
       window.onbeforeunload = function () {  
           closeWebSocket();  
       }  
     
       //将消息显示在网页上  
       function setMessageInnerHTML(innerHTML) {  
           document.getElementById('message').innerHTML += innerHTML + '<br/>';  
       }  
     
       //关闭WebSocket连接  
       function closeWebSocket() {  
           websocket.close();  
       }  
     
       //发送消息  
       function send() {  
           var message = document.getElementById('text').value;  
           websocket.send(message);  
       }  
   </script>
</html>
```

### 服务端向客户端推送消息

```java
/**
 * 服务端推送消息对客户端
 * @ClassName: ServiceClientController 
 * @Description: TODO
 * @author OnlyMate
 * @Date 2018年8月16日 下午2:45:22  
 *
 */
@Controller
@RequestMapping(value="webSocketByTomcat/serviceToClient")
public class ServiceClientByTomcatController {
    private WebSocket websocket = new WebSocket();
    
    @RequestMapping
    public void sendMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json = new JSONObject();
        json.put("to", request.getSession().getId());
        json.put("msg", "欢迎连接WebSocket！！！！");
        websocket.onMessage(json.toJSONString());
    }
}
```

### 效果如下图所示

![](https://img-blog.csdnimg.cn/5fc2d36db39d44b0a1d666ed6708c260.png)  
![](https://img-blog.csdnimg.cn/c2add7a25a8045f08561d2c41690972b.png)

spring 整合 websocket 方法
----------------------

springboot 对 websocket 支持很友好，只需要继承 webSocketHandler 类，重写几个方法就可以了

这个类是对消息的一些处理，比如是发给一个人，还是发给所有人，并且前端连接时触发的一些动作

```java
/**
 * 创建一个WebSocket server
 * 
 * @ClassName: CustomWebSocketHandler
 * @Description: TODO
 * @author OnlyMate
 * @Date 2018年8月16日 下午3:17:34
 *
 */
@Service
public class CustomWebSocketHandler extends TextWebSocketHandler implements WebSocketHandler {
    private Logger logger = LoggerFactory.getLogger(CustomWebSocketHandler.class);
    // 在线用户列表
    private static final Map<String, WebSocketSession> users;
    // 用户标识
    private static final String CLIENT_ID = "mchNo";
 
    static {
        users = new HashMap<>();
    }
 
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("成功建立websocket-spring连接");
        String mchNo = getMchNo(session);
        if (StringUtils.isNotEmpty(mchNo)) {
            users.put(mchNo, session);
            session.sendMessage(new TextMessage("成功建立websocket-spring连接"));
            logger.info("用户标识：{}，Session：{}", mchNo, session.toString());
        }
    }
 
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("收到客户端消息：{}", message.getPayload());
        JSONObject msgJson = JSONObject.parseObject(message.getPayload());
        String to = msgJson.getString("to");
        String msg = msgJson.getString("msg");
        WebSocketMessage<?> webSocketMessageServer = new TextMessage("server:" +message);
        try {
            session.sendMessage(webSocketMessageServer);
            if("all".equals(to.toLowerCase())) {
                sendMessageToAllUsers(new TextMessage(getMchNo(session) + ":" +msg));
            }else {
                sendMessageToUser(to, new TextMessage(getMchNo(session) + ":" +msg));
            }
        } catch (IOException e) {
            logger.info("handleTextMessage method error：{}", e);
        }
    }
 
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        logger.info("连接出错");
        users.remove(getMchNo(session));
    }
 
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("连接已关闭：" + status);
        users.remove(getMchNo(session));
    }
 
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
 
    public void sendMessage(String jsonData) {
        logger.info("收到客户端消息sendMessage：{}", jsonData);
        JSONObject msgJson = JSONObject.parseObject(jsonData);
        String mchNo = StringUtils.isEmpty(msgJson.getString(CLIENT_ID)) ? "陌生人" : msgJson.getString(CLIENT_ID);
        String to = msgJson.getString("to");
        String msg = msgJson.getString("msg");
        if("all".equals(to.toLowerCase())) {
            sendMessageToAllUsers(new TextMessage(mchNo + ":" +msg));
        }else {
            sendMessageToUser(to, new TextMessage(mchNo + ":" +msg));
        }
    }
    
    /**
     * 发送信息给指定用户
     * @Title: sendMessageToUser 
     * @Description: TODO
     * @Date 2018年8月21日 上午11:01:08 
     * @author OnlyMate
     * @param mchNo
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String mchNo, TextMessage message) {
        if (users.get(mchNo) == null)
            return false;
        WebSocketSession session = users.get(mchNo);
        logger.info("sendMessage：{} ,msg：{}", session, message.getPayload());
        if (!session.isOpen()) {
            logger.info("客户端:{},已断开连接，发送消息失败", mchNo);
            return false;
        }
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            logger.info("sendMessageToUser method error：{}", e);
            return false;
        }
        return true;
    }
 
    /**
     * 广播信息
     * @Title: sendMessageToAllUsers 
     * @Description: TODO
     * @Date 2018年8月21日 上午11:01:14 
     * @author OnlyMate
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<String> mchNos = users.keySet();
        WebSocketSession session = null;
        for (String mchNo : mchNos) {
            try {
                session = users.get(mchNo);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }else {
                    logger.info("客户端:{},已断开连接，发送消息失败", mchNo);
                }
            } catch (IOException e) {
                logger.info("sendMessageToAllUsers method error：{}", e);
                allSendSuccess = false;
            }
        }
 
        return allSendSuccess;
    }
    
    /**
     * 获取用户标识
     * @Title: getMchNo 
     * @Description: TODO
     * @Date 2018年8月21日 上午11:01:01 
     * @author OnlyMate
     * @param session
     * @return
     */
    private String getMchNo(WebSocketSession session) {
        try {
            String mchNo = session.getAttributes().get(CLIENT_ID).toString();
            return mchNo;
        } catch (Exception e) {
            return null;
        }
    }
}
```

这个类的作用就是在连接成功前和成功后增加一些额外的功能

我们希望能够把 websocketSession 和 httpsession 对应起来，这样就能根据当前不同的 session，定向对 websocketSession 进行数据返回; 在查询资料之后，发现 spring 中有一个拦截器接口，HandshakeInterceptor，可以实现这个接口，来拦截握手过程，向其中添加属性

```java
/**
 * WebSocket握手时的拦截器
 * @ClassName: CustomWebSocketInterceptor 
 * @Description: TODO
 * @author OnlyMate
 * @Date 2018年8月16日 下午3:17:04  
 *
 */
public class CustomWebSocketInterceptor implements HandshakeInterceptor {
    private Logger logger = LoggerFactory.getLogger(CustomWebSocketInterceptor.class);
    /**
     * 关联HeepSession和WebSocketSession，
     * beforeHandShake方法中的Map参数 就是对应websocketSession里的属性
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> map) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            logger.info("*****beforeHandshake******");
            HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            HttpSession session = httpServletRequest.getSession(true);
            
            logger.info("mchNo：{}", httpServletRequest.getParameter("mchNo"));
            if (session != null) {
                
                map.put("sessionId",session.getId());
                map.put("mchNo", httpServletRequest.getParameter("mchNo"));
            }
        }
        return true;
    }
 
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        logger.info("******afterHandshake******");
    }
}
```

这个类是配置类向 Spring 中注入 handler

```java
/**
 * websocket的配置类
 * @ClassName: CustomWebSocketConfig 
 * @Description: TODO
 * @author OnlyMate
 * @Date 2018年8月16日 下午3:17:26  
 *
 */
@Configuration
@EnableWebSocket
public class CustomWebSocketConfig implements WebSocketConfigurer {
 
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customWebSocketHandler(), "/webSocketBySpring/customWebSocketHandler").addInterceptors(new CustomWebSocketInterceptor()).setAllowedOrigins("*");
        registry.addHandler(customWebSocketHandler(), "/sockjs/webSocketBySpring/customWebSocketHandler").addInterceptors(new CustomWebSocketInterceptor()).setAllowedOrigins("*").withSockJS();
    }
 
    @Bean
    public WebSocketHandler customWebSocketHandler() {
        return new CustomWebSocketHandler();
    }
}
```

补充说明：

setAllowedOrigins(“*”) 一定要加上，不然只有访问 localhost，其他的不予许访问

setAllowedOrigins(String[] domains), 允许指定的域名或 IP(含端口号) 建立长连接，如果只允许自家域名访问，这里轻松设置。如果不限时使用 "*" 号，如果指定了域名，则必须要以 http 或 https 开头

经查阅官方文档 springwebsocket 4.1.5 版本前默认支持跨域访问，之后的版本默认不支持跨域，需要设置

使用 withSockJS() 的原因：

一些浏览器中缺少对 WebSocket 的支持, 因此，回退选项是必要的，而 Spring 框架提供了基于 SockJS 协议的透明的回退选项。

SockJS 的一大好处在于提供了浏览器兼容性。优先使用原生 WebSocket，如果在不支持 websocket 的浏览器中，会自动降为轮询的方式。  
除此之外，spring 也对 socketJS 提供了支持。

如果代码中添加了 withSockJS() 如下，服务器也会自动降级为轮询。

```
registry.addEndpoint("/coordination").withSockJS();
```

SockJS 的目标是让应用程序使用 WebSocket API，但在运行时需要在必要时返回到非 WebSocket 替代，即无需更改应用程序代码。

SockJS 是为在浏览器中使用而设计的。它使用各种各样的技术支持广泛的浏览器版本。对于 SockJS 传输类型和浏览器的完整列表，可以看到 SockJS 客户端页面。  
传输分为 3 类: WebSocket、HTTP 流和 HTTP 长轮询（按优秀选择的顺序分为 3 类）

### 客户端代码

```jsp
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ctxpath"
    value="${pageContext.request.scheme}${'://'}${pageContext.request.serverName}${':'}${pageContext.request.serverPort}${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset=UTF-8">
<title>登录测试</title>
</head>
<body>
    <h2>Hello World! Web Socket by Spring</h2>
    <div>
        <span>sessionId:</span>
        <% 
            HttpSession s= request.getSession(); 
            out.println(s.getId());
        %>
    </div>
    
    <input id="sessionId" type="hidden" value="<%=session.getId() %>" />
    <input id="text" type="text" />
    <button onclick="send()">发送消息</button>
    <hr />
    <button onclick="closeWebSocket()">关闭WebSocket连接</button>
    <hr />
    <div id="message"></div>
</body>
<script type="text/javascript" src="http://localhost:8088/static/js/sockjs-0.3.min.js"></script>
<script type="text/javascript">  
        var websocket = null;  
        //判断当前浏览器是否支持WebSocket  
        //判断当前浏览器是否支持WebSocket  
        if('WebSocket' in window) {
            websocket = new WebSocket("ws://localhost:8088/websocket/webSocketBySpring/customWebSocketHandler?mchNo="+ 123);  
        } else if('MozWebSocket' in window) {
            websocket = new MozWebSocket("ws://localhost:8088/websocket/webSocketBySpring/customWebSocketHandler?mchNo="+ 123);
        } else {
            websocket = new SockJS("http://localhost:8088/websocket/sockjs/webSocketBySpring/customWebSocketHandler?mchNo="+ 123);
        }
        //连接发生错误的回调方法  
        websocket.onerror = function () {  
            setMessageInnerHTML("WebSocket连接发生错误");  
        };  
      
        //连接成功建立的回调方法  
        websocket.onopen = function () {  
            setMessageInnerHTML("WebSocket连接成功");  
        }  
      
        //接收到消息的回调方法  
        websocket.onmessage = function (event) {  
            setMessageInnerHTML(event.data);  
        }  
      
        //连接关闭的回调方法  
        websocket.onclose = function () {  
            setMessageInnerHTML("WebSocket连接关闭");  
        }  
      
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。  
        window.onbeforeunload = function () {  
            closeWebSocket();  
        }  
      
        //将消息显示在网页上  
        function setMessageInnerHTML(innerHTML) {  
            document.getElementById('message').innerHTML += innerHTML + '<br/>';  
        }  
      
        //关闭WebSocket连接  
        function closeWebSocket() {  
            websocket.close();  
        }  
      
        //发送消息  
        function send() {  
            var message = document.getElementById('text').value;  
            websocket.send(message);  
        }  
    </script>
</html>
```

### 效果如下图所示

![](https://img-blog.csdnimg.cn/21e8615233254445b9246ebbee3c6ee4.png)  
![](https://img-blog.csdnimg.cn/117af71c2b9b461999d625be84c1f0ad.png)

原文链接：https://blog.csdn.net/weixin_50770886/article/details/117569005