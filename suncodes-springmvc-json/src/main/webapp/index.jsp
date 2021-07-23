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

    <script>
        window.onload = function () {
            document.getElementById("h").onclick = function () {
                var data = JSON.stringify({"city":"1","person":"2","target":"3"});

                var xhr = new XMLHttpRequest();
                xhr.withCredentials = true;

                xhr.addEventListener("readystatechange", function() {
                    if(this.readyState === 4) {
                        console.log(this.responseText);
                        alert("成功了！");
                    }
                });

                xhr.open("POST", "${pageContext.request.contextPath }/hhh");
                xhr.setRequestHeader("Content-Type", "application/json");

                xhr.send(data);
            }
        }
    </script>
</head>
<body>

<button id="h">点击发送ajax</button>

<br/>
<br/>

<a href="${pageContext.request.contextPath}/testExceptionHandle?i=0">错误页面</a>

</body>
</html>
