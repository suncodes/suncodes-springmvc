# @Controller 注解 和 @RequestMapping 注解

## Controller注解

@Controller 注解用于声明某类的实例是一个控制器。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- LoginController控制器类，映射到"/login" -->
    <bean class="controller.LoginController"/>
    <!-- LoginController控制器类，映射到"/register" -->
    <bean class="controller.RegisterController"/>

    <bean id="viewResolver" class="org.springframework.web.servlet.view.UrlBasedViewResolver">
        <property name="viewClass" value="org.springframework.web.servlet.view.InternalResourceView" />
        <property name="prefix" value="/WEB-INF/jsp"/>
        <property name="suffix" value=".jsp"/>
    </bean>

</beans>
```

```java
@Controller
public class LoginController {

    @RequestMapping("/login")
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {
        return new ModelAndView("/register");
    }
}

```

## RequestMapping注解

一个控制器内有多个处理请求的方法，如 UserController 里通常有增加用户、修改用户信息、删除指定用户、根据条件获取用户列表等。每个方法负责不同的请求操作，而 @RequestMapping 就负责将请求映射到对应的控制器方法上。

在基于注解的控制器类中可以为每个请求编写对应的处理方法。使用 @RequestMapping 注解将请求与处理方法一 一对应即可。

@RequestMapping 注解可用于类或方法上。用于类上，表示类中的所有响应请求的方法都以该地址作为父路径。

@RequestMapping 注解常用属性如下。

### 1. value 属性
value 属性是 @RequestMapping 注解的默认属性，因此如果只有 value 属性时，可以省略该属性名，如果有其它属性，则必须写上 value 属性名称。如下。

value 属性支持通配符匹配

Ant 风格资源地址支持 3 种匹配符：
- ?：匹配文件名中的一个字符 –
- \*：匹配文件名中的任意字符 –
- \*\*：\*\* 匹配多层路径

@RequestMapping 还支持 Ant 风格的 URL ：
```text
/user/*/createUser: 匹配 –
  /user/aaa/createUser、/user/bbb/createUser 等 URL
/user/**/createUser: 匹配 –
  /user/createUser、/user/aaa/bbb/createUser 等 URL
/user/createUser??: 匹配 –
  /user/createUseraa、/user/createUserbb 等 URL
```

### 2. path属性

path 属性和 value 属性都用来作为映射使用。（作用一模一样）即 @RequestMapping(value="toUser") 和 @RequestMapping(path="toUser") 都能访问 toUser() 方法。

path 属性支持通配符匹配，如 @RequestMapping(path="toUser/*") 表示 http://localhost:8080/toUser/1 或 http://localhost:8080/toUser/hahaha 都能够正常访问。 

`特别注意`：value 属性和 path 属性只能二选一

### 3. name属性

name属性相当于方法的注释，使方法更易理解。如 @RequestMapping(value = "toUser",name = "获取用户信息")。 

### 4. method属性
method 属性用于表示该方法支持哪些 HTTP 请求。如果省略 method 属性，则说明该方法支持**全部**的 HTTP 请求。

@RequestMapping(value = "toUser",method = RequestMethod.GET) 表示该方法只支持 GET 请求。也可指定多个 HTTP 请求，如 @RequestMapping(value = "toUser",method = {RequestMethod.GET,RequestMethod.POST})，说明该方法同时支持 GET 和 POST 请求。


### 5. params属性

params 属性用于指定请求中规定的参数

```text
param1: 表示请求必须包含名为 param1 的请求参数
!param1: 表示请求不能包含名为 param1 的请求参数
param1 != value1: 表示请求包含名为 param1 的请求参数，但其值不能为 value1
{“param1=value1”, “param2”}: 请求必须包含名为 param1 和param2 的两个请求参数，且 param1 参数的值必须为 value1
```

```java
    @RequestMapping(value = "/params", params = "type")
    public ModelAndView f() {
        return new ModelAndView("/params");
    }
```
以上代码表示请求中必须包含 type 参数时才能执行该请求。

```java
    @RequestMapping(value = "/params1", params = "type=1")
    public ModelAndView f1() {
        return new ModelAndView("/params");
    }
```
以上代码表示请求中必须包含 type 参数，且 type 参数为 1 时才能够执行该请求。

### 6. header属性

header 属性表示请求中必须包含某些指定的 header 值。

@RequestMapping(value = "toUser",headers = "Referer=http://www.xxx.com") 表示请求的 header 中必须包含了指定的“Referer”请求头，以及值为“http://www.xxx.com”时，才能执行该请求。 

### 7. consumers 属性

consumers 属性用于指定处理请求的提交内容类型（Content-Type），例如：application/json、text/html。如 
@RequestMapping(value = "toUser",consumes = "application/json")。

```java
    @RequestMapping(value = "/params2", consumes = "application/json")
    public ModelAndView f2() {
        return new ModelAndView("/params");
    }

    @RequestMapping(value = "/params3", consumes = "application/x-www-form-urlencoded")
    public ModelAndView f3() {
        return new ModelAndView("/params");
    }
```


`特别说明`：consumers 指的是请求头中的 Content-Type 的值。

### 8. produces 属性

produces 属性用于指定返回的内容类型，返回的内容类型必须是 request 请求头（Accept）中所包含的类型。如 @RequestMapping(value = "toUser",produces = "application/json")。

除此之外，produces 属性还可以指定返回值的编码。如 @RequestMapping(value = "toUser",produces = "application/json,charset=utf-8")，表示返回 utf-8 编码。



