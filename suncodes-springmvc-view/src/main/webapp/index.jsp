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
未注册的用户，请
<a href="${pageContext.request.contextPath }/base/register"> 注册</a>！
<br/>
已注册的用户，去<a href="${pageContext.request.contextPath }/base/login"> 登录</a>！
<br/>
<br/>
internalResourceViewResolver 视图解析器：
<a href="${pageContext.request.contextPath }/default/internalResourceViewResolver">
    internalResourceViewResolver 视图解析器
</a>

<br/>
<br/>
JSON：<a href="${pageContext.request.contextPath }/json/jsonViewResolver">JSON</a>
</body>
</html>
