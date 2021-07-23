<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/7/20
  Time: 14:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
</head>
<body>
<a href="${pageContext.request.contextPath }/index?name=111"> URL</a>

<br/>
<br/>

<form method="post" action="${pageContext.request.contextPath }/converter">
    输入：<input type="text" name="goods">
</form>

</body>
</html>
