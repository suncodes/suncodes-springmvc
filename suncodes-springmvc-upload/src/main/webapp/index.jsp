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

<form method="POST" action="${pageContext.request.contextPath}/uploadFile" enctype="multipart/form-data">
    File to upload: <input type="file" name="file"> <br/>
    <input type="submit" value="Upload"> <br/>
    Press here to upload the file!
</form>

<br/>
<br/>
<br/>

<form method="POST" action="${pageContext.request.contextPath}/uploadFile1" enctype="multipart/form-data">
    File to upload: <input type="file" name="file"> <br/>
    <input type="text" name="param"> <br/>
    <input type="submit" value="Upload"> <br/>
    Press here to upload the file!
</form>

<br/>
<br/>
<br/>

多文件上传：
<form method="POST" action="${pageContext.request.contextPath}/uploadFile2" enctype="multipart/form-data">
    File to upload: <input type="file" name="file"> <br/>
    File to upload: <input type="file" name="file"> <br/>
    <input type="text" name="param"> <br/>
    <input type="submit" value="Upload"> <br/>
    Press here to upload the file!
</form>

<br/>
<br/>
<br/>

<form method="POST" action="${pageContext.request.contextPath}/uploadFile3" enctype="multipart/form-data">
    File to upload: <input type="file" name="file"> <br/>
    <input type="text" name="param"> <br/>
    <input type="submit" value="Upload"> <br/>
    Press here to upload the file!
</form>

</body>
</html>
