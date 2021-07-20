# 工程搭建：

## 1、新建工程

- 新建 maven 工程 suncodes-springmvc，作为父工程
- 修改 pom 文件，packaging 为 pom

- 新建 maven 工程 suncodes-springmvc-helloworld
- 新建 src/main/webapp 文件夹
- 新建 src/main/webapp/WEB-INF 文件夹
- 新建 src/main/webapp/WEB-INF/web.xml 文件
- 新建 src/main/webapp/index.jsp 文件
- 修改 pom 文件，packaging 为 war
- 刷新 pom 文件

## 2、配置 tomcat

- Add Configuration
- 点击 + 号，Tomcat ---&gt; Local
- 设置名称：helloworld
- 设置 Deployment，点击 + 号，选择 suncodes-springmvc-helloworld:war exploded
- 设置 Context path为：/helloworld

## 3、编写测试代码

### 3.1、引入 pom 依赖

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${springmvc.version}</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${springmvc.version}</version>
        </dependency>
```

分别引入 SpringMVC，servlet-api，spring 依赖

### 3.2、定义DispatcherServlet

Spring MVC 是基于 Servlet 的，DispatcherServlet 是整个 Spring MVC 框架的核心，主要负责截获请求并将其分派给相应的处理器处理。所以配置 Spring MVC，首先要定义 DispatcherServlet。跟所有 Servlet 一样，用户必须在 web.xml 中进行配置。

web.xml 中部署 DispatcherServlet，代码如下：

```xml

<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
    <display-name>springmvc-helloworld</display-name>
    <!-- 部署 DispatcherServlet -->
    <servlet>
        <servlet-name>helloworld</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 表示容器再启动时立即加载servlet -->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>helloworld</servlet-name>
        <!-- 处理所有URL -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>

```

Spring MVC 初始化时将在应用程序的 WEB-INF 目录下查找配置文件，该配置文件的命名规则是“servletName-servlet.xml”，例如 helloworld-servlet.xml。

也可以将 Spring MVC 的配置文件存放在应用程序目录中的任何地方，但需要使用 servlet 的 init-param 元素加载配置文件，通过 contextConfigLocation 参数来指定 Spring MVC 配置文件的位置，示例代码如下。 

```xml
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:helloworld-servlet.xml</param-value>
        </init-param>
```

classpath：只会到你的class路径中查找找文件;

classpath*：不仅包含class路径，还包括jar文件中(class路径)进行查找.

### 3.3、创建Spring MVC配置文件

在 WEB-INF 目录下创建 helloworld-servlet.xml 文件，如下所示。 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- LoginController控制器类，映射到"/login" -->
    <bean name="/login"
          class="helloworld.controller.LoginController"/>
    <!-- LoginController控制器类，映射到"/register" -->
    <bean name="/register"
          class="helloworld.controller.RegisterController"/>
</beans>
```

### 3.4、创建Controller

```java
package helloworld.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {
        return new ModelAndView("/WEB-INF/jsp/register.jsp");
    }
}
```

```java
package helloworld.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class RegisterController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {
        return new ModelAndView("/WEB-INF/jsp/login.jsp");
    }
}
```


### 3.5、创建View

在 webapp 下的 index.jsp 中

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
</head>
<body>
未注册的用户，请
<a href="${pageContext.request.contextPath }/register"> 注册</a>！
<br/>
已注册的用户，去<a href="${pageContext.request.contextPath }/login"> 登录</a>！
</body>
</html>
```

在 WEB-INF 下创建 jsp 文件夹，将 login.jsp 和 register.jsp 放到 jsp 文件夹下。login.jsp 代码如下。 

```jsp
    <%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    </head>
    <body>
        登录页面！
    </body>
    </html>
```

 register.jsp 代码如下。

```jsp
    <%@ page language="java" contentType="text/html; charset=UTF-8"
             pageEncoding="UTF-8" %>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insert title here</title>
    <body>
        注册页面！
    </body>
    </html>
    </head>
```

## 4、部署运行

报错：Error:build: Cannot determine build data storage root for project E:/github/myself/suncodes-springmvc

解决：关掉 IDEA，删除.idea，重新打开

配置 tomcat ，运行即可。

## 5、有关 contextConfigLocation 资源文件找不到问题

在设置 contextConfigLocation 的时候，通过 设置 classpath:helloworld-servlet.xml 指定，但是报错。

说是 java.io.FileNotFoundException: class path resource

解决：查看 target 目录下的文件夹，根据目录结构，调整：
```xml
<init-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/helloworld-servlet.xml</param-value>
</init-param>
```