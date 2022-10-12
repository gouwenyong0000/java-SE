12.1 创建 web 资源
--------------

### 12.1.1 项目结构

![](https://img-blog.csdnimg.cn/3e8cfceb26594f4fb12c7c20cb2f9ed7.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_15,color_FFFFFF,t_70,g_se,x_16)

### 12.1.2 导入项目

说明: 将课前资料中的文件导入到项目中.  
![](https://img-blog.csdnimg.cn/f6aadf5b5ee24036aa4e67efad3abb30.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)  

3. 添加资源文件  
   ![](https://img-blog.csdnimg.cn/07df73ebec5a4030a5935c2f3f0349f2.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

12.2 关于 Base 标签的说明
------------------

### 12.2.1 为什么要使用 base 标签统一页面基础访问路径

因为在页面中有很多的 a 标签、表单以及 Ajax 请求 (以后会学) 都需要写访问路径，而在访问路径中**项目路径**是一样的，所以如果不统一编写**项目路径**的话，就会发生当项目路径发生改变的时候该页面所有用到项目路径的地方都需要更改的情况

### 12.2.2 base 标签的语法规则

*   base 标签要写在 head 标签内
*   base 标签必须写在所有其他有路径的标签的前面
*   base 标签使用 href 属性设置路径的基准
*   base 标签生效的机制是：最终的访问地址 = base 标签 href 属性设置的基准 + 具体标签内的路径
*   如果某个路径想要基于 base 中的路径进行路径编写，那么它不能以`/`开头

```
<head>
    <meta charset="UTF-8"/>
    <meta />
    <title>书城首页</title>
    <base href="/bookstore/"/>
    <link rel="stylesheet" href="static/css/minireset.css"/>
    <link rel="stylesheet" href="static/css/common.css"/>
    <link rel="stylesheet" href="static/css/iconfont.css"/>
    <link rel="stylesheet" href="static/css/index.css"/>
    <link rel="stylesheet" href="static/css/swiper.min.css"/>
</head>
```

12.3 MVC 设计思想
-------------

### 12.3.1 MVC 介绍

MVC 模式代表 Model-View-Controller（模型 - 视图 - 控制器） 模式。这种模式用于应用程序的分层开发。

*   Model（模型） - 模型代表一个存取数据的对象或 JAVA POJO。它也可以带有逻辑，在数据变化时更新控制器。
*   View（视图） - 视图代表模型包含的数据的可视化。
*   Controller（控制器） - 控制器作用于模型和视图上。它控制数据流向模型对象，并在数据变化时更新视图。它使视图与模型分离开。

![](https://img-blog.csdnimg.cn/c82918f9a15c4b9aa9487f1c7299795e.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_20,color_FFFFFF,t_70,g_se,x_16)

### 12.3.2 三层代码结构

基于 MVC 思想, 将代码进行分层处理. 实现代码松耦合  
![](https://img-blog.csdnimg.cn/c5799bf4f2d94473a8912f0369cfd671.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_13,color_FFFFFF,t_70,g_se,x_16)  
所有和当前业务功能需求相关的代码全部耦合在一起，如果其中有任何一个部分出现了问题，牵一发而动全身，导致其他无关代码也要进行相应的修改。这样的话代码会非常难以维护。

所以为了提高开发效率，需要对代码进行模块化的拆分。整个项目模块化、组件化程度越高，越容易管理和维护，出现问题更容易排查。

### 12.3.3 三层架构的划分

![](https://img-blog.csdnimg.cn/ffd329c888b14d4eb19574e5384f617b.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA6Zeq6ICA5aSq6Ziz,size_17,color_FFFFFF,t_70,g_se,x_16)

*   表述层：又可以称之为控制层，负责处理浏览器请求、返回响应、页面调度
*   业务逻辑层：负责处理业务逻辑，根据业务逻辑把持久化层从数据库查询出来的数据进行运算、组装，封装好后返回给表述层，也可以根据业务功能的需要调用持久化层把数据保存到数据库、修改数据库中的数据、删除数据库中的数据
*   持久化层：根据上一层的调用对数据库中的数据执行增删改查的操作

12.4 用户注册实现
-----------

### 12.4.1 编辑注册页面

```
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <base href="http://localhost:8090/bookstore_02/">
    <title>尚硅谷会员注册页面</title>
    <link type="text/css" rel="stylesheet" href="static/css/style.css" />
    <link rel="stylesheet" href="static/css/register.css" />
		<script src="static/script/vue.js"></script>
    <style type="text/css">
      .login_form {
        height: 420px;
        margin-top: 25px;
      }
    </style>
  </head>
  <body>
		<div id="app">
			<div id="login_header">
				<a href="index.html">
					<img class="logo_img" alt="" src="static/img/logo.gif" />
				</a>
			</div>

			<div class="login_banner">
				<div class="register_form">
					<h1>注册尚硅谷会员</h1>
					<!--<form action="regist_success.html" method="post">-->
					<form action="regist" method="post">
						<div class="form-item">
							<div>
								<label>用户名称:</label>
								<input type="text" placeholder="请输入用户名"  @blur="checkUsername"/>
							</div>
							<span class="errMess" v-text="msg.usernameErrorMsg"></span>
						</div>
						<div class="form-item">
							<div>
								<label>用户密码:</label>
								<input type="password" placeholder="请输入密码"  @blur="checkPassword"/>
							</div>
							<span class="errMess" v-text="msg.passwordErrorMsg"></span>
						</div>
						<div class="form-item">
							<div>
								<label>确认密码:</label>
								<input type="password" placeholder="请输入确认密码" v-model="user.password2" @blur="checkPassword2"/>
							</div>
							<span class="errMess"v-text="msg.passwordErrorMsg2"></span>
						</div>
						<div class="form-item">
							<div>
								<label>用户邮箱:</label>
								<input type="text" placeholder="请输入邮箱"  @blur="checkEmail"/>
							</div>
							<span class="errMess" v-text="msg.emailErrorMsg"></span>
						</div>
			 
						<div class="form-item">
							<div>
								<label>验证码:</label>
								<div class="verify">
									<input type="text" placeholder="" />
									<img src="static/img/code.bmp" alt="" height="50px"/>
								</div>
							</div>
							<span class="errMess" v-text="msg.codeMsg"></span>
						</div>
						<button class="btn" @click="regist">注册</button>
					</form>
				</div>
			</div>
			<div id="bottom">
				<span>
					尚硅谷书城.Copyright ©2015
				</span>
			</div>
		</div>
		
		<!-- 定义vue对象 -->
		<script type="text/javascript">
			
			const app = new Vue({
				el: "#app",
				data: {
					msg: {
						usernameErrorMsg: "用户名应为6~16位数字和字母组成",
						passwordErrorMsg: "密码的长度至少为8位",
						passwordErrorMsg2: "密码两次输入不一致",
						emailErrorMsg: "请输入正确的邮箱格式",
						codeMsg: "请输入正确的验证码"
					},
					user: {
						usermame: '',
						password: '',
						password2: '',
						email: ''
					},
					//定义全局表单提交标识符
					flagArray : [0,0,0,0]
				},
				methods: {
					checkUsername(){
						 //1.定义正则表达式语法
						 let usernameRege = /^[a-zA-Z0-9]{6,16}$/
						 let usernameFlag = usernameRege.test(this.user.usermame)
						 if(usernameFlag){
							 this.msg.usernameErrorMsg = "√"
							 this.flagArray[0] = 1
						 }else{
							 this.msg.usernameErrorMsg = "用户名应为6~16位数字和字母组成"
							 this.flagArray[0] = 0
						 }
					},
					checkPassword(){
						let passwordRege = /^.{8,}$/
						let passwordFlag = passwordRege.test(this.user.password)
						if(passwordFlag){
							this.msg.passwordErrorMsg = "√"
							this.flagArray[1] = 1
						}else{
							this.msg.passwordErrorMsg = "密码的长度至少为8位"
							this.flagArray[1] = 0
						}
					},
					checkPassword2(){
						if(this.user.password === this.user.password2){
							this.msg.passwordErrorMsg2 = "√"
							this.flagArray[2] = 1
						}else{
							this.msg.passwordErrorMsg2 = "密码两次输入不一致"
							this.flagArray[2] = 0
						}
					},
					checkEmail(){
						let emailRege = /^[a-zA-Z0-9_.-]+@([a-zA-Z0-9-]+[.]{1})+[a-zA-Z]+$/
						if(emailRege.test(this.user.email)){
							this.msg.emailErrorMsg = '√'
							this.flagArray[3] = 1
						}else{
							this.msg.emailErrorMsg = "请输入正确的邮箱格式"
							this.flagArray[3] = 0
						}
					},
					regist(){
						//再次校验密码是否相同即可.
						this.checkPassword2()
						let flagStr = this.flagArray.join(",")
						if(flagStr !== "1,1,1,1"){
							//应该阻止表单提交
							event.preventDefault()
							alert("表单校验不通过!!!")
						}else{
							alert("表单校验成功")
						}
						
					}
				}
			})
		
		</script>
  </body>
</html>
```

### 12.4.2 编辑 RegistServlet

```
public class RegistServlet extends HttpServlet {

    //抽取业务层代码
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    /**
     * 业务实现思路:
     *      1. 获取用户提交的数据
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //完成用户注册操作,注册成功返回布尔类型数据  true表示成功,  false表示失败
        boolean flag = userService.addUser(user);

        //如果注册成功,则转发到注册成功页面,否则转发到注册页面
        if(flag){
            request.getRequestDispatcher("/pages/user/regist_success.html").forward(request,response);
        }else{
            request.getRequestDispatcher("/pages/user/regist.html").forward(request,response);
        }
    }
}
```

### 12.4.3 编辑 UserService

1.  编辑 UserService 接口

```
public interface UserService {
    boolean addUser(User user);

    User findUserByUP(User user);
}
```

2.  编辑 UserServiceImpl 实现类

```
package com.atguigu.service.impl;

import com.atguigu.bean.User;
import com.atguigu.dao.UserDao;
import com.atguigu.dao.impl.UserDaoImpl;
import com.atguigu.service.UserService;
import com.atguigu.util.MD5Util;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * 用户注册时,需要将密码加密处理
     * @param user
     * @return
     */
    @Override
    public boolean addUser(User user) {
        String password = user.getPassword();
        String md5Pass = MD5Util.encode(password);
        user.setPassword(md5Pass);
        return userDao.addUser(user);
    }

    //根据用户名和密码查询数据
    @Override
    public User findUserByUP(User user) {
        String md5Pass = MD5Util.encode(user.getPassword());
        user.setPassword(md5Pass);

        //根据用户名和密码查询数据
        User userDB = userDao.findUserByUP(user);
        //如果查询结果为null,表示用户名或者密码错误
        return userDB==null?null:userDB;
    }
}
```

### 12.4.4 编辑 UserDao

1.  编辑 UserDao 接口

```
public interface UserDao {
    boolean addUser(User user);

    User findUserByUP(User user);
}
```

2.  编辑 UserDaoImpl 实现类

```
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    @Override
    public boolean addUser(User user) {
        String sql = "insert into user(id,username,password,email) values (null,?,?,?)";
        int rows = this.update(sql,user.getUsername(),user.getPassword(),user.getEmail());
        return rows>0;
    }

    @Override
    public User findUserByUP(User user) {
        String sql = "select * from user where username=? and password=?";
        return this.getBean(User.class, sql,user.getUsername(),user.getPassword());
    }
}
```

12.5 用户登录页面
-----------

### 12.5.1 编辑登录页面

```
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>尚硅谷会员登录页面</title>
    <base href="http://localhost:8090/bookstore_02/">
    <link type="text/css" rel="stylesheet" href="static/css/style.css" />
    <!--1. 导入vue.js文件-->
		<script src="static/script/vue.js"></script>
  </head>
  <body>
		
		<div id="app">
			<div id="login_header">
				<a href="index.html">
					<img class="logo_img" alt="" src="static/img/logo.gif" />
				</a>
			</div>

			<div class="login_banner">
				<div id="l_content">
					<span class="login_word">欢迎登录</span>
				</div>

				<div id="content">
					<div class="login_form">
						<div class="login_box">
							<div class="tit">
								<h1>尚硅谷会员</h1>
							</div>
							<div class="msg_cont">
								<b></b>
								<span class="errorMsg" v-text="msg"></span>
							</div>
							<div class="form">
								<!--<form action="login_success.html" >-->
								<form action="loginServlet" method="post">
									<label>用户名称：</label>
									<input
										class="itxt"
										type="text"
										placeholder="请输入用户名"
										autocomplete="off"
										tabindex="1"
										
										id="username"
										v-model="username"
									/>
									<br />
									<br />
									<label>用户密码：</label>
									<input
										class="itxt"
										type="password"
										placeholder="请输入密码"
										autocomplete="off"
										tabindex="1"
										
										id="password"
										v-model="password"
									/>
									<br />
									<br />
									<input type="submit" value="登录" id="sub_btn" @click="login"/>
								</form>
								<div class="tit">
									<a href="regist.html">立即注册</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="bottom">
				<span>
					尚硅谷书城.Copyright ©2015
				</span>
			</div>
		</div>
		<script>
			
			 const app = new Vue({
				 el: "#app",
				 data: {
					 username: '',
					 password: '',
					 msg: '请输入用户名和密码'
				 },
				 methods: {
					 login(){
						 if(this.username === ''){
							 this.msg = "请输入用户名"
							 //阻止事件提交
							 event.preventDefault()
						 }
						 if(this.password === ''){
							 this.msg = "请输入密码"
							 //阻止事件提交
							 event.preventDefault()
						 }
					 }
				 }
			 })
				
		</script>
  </body>
</html>
```

### 12.5.2 编辑 LoginServlet

```
package com.atguigu.servlet;

import com.atguigu.bean.User;
import com.atguigu.service.UserService;
import com.atguigu.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }


    //完成用户登录操作
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //用户用户提交的数据信息
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //2.实现用户登录操作
        User userDB = userService.findUserByUP(user);

        //3.判断查询数据有结果 如果有数据则重定向到首页  否则转发到登录页面
        if(userDB == null){
            request.getRequestDispatcher("/pages/user/login.html").forward(request,response);
        }else{
            response.sendRedirect(request.getContextPath()+"/index.html");
        }
    }
}
```


------------------