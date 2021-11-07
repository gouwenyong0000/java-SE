<%--
  Created by IntelliJ IDEA.
  User: gouwenyong0000
  Date: 2021/8/26
  Time: 0:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="http://localhost:8080/09/file" method="post" enctype="multipart/form-data" >
  用户名：<input type="text" name="username">
  头像：<input type="file" name="photo">
    <input type="submit" value="提交">
</form>

</body>
</html>
