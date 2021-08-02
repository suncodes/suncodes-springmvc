## 在 Action 中访问 WEB 资源

1). 什么是 WEB 资源 ?

	HttpServletRequest, HttpSession, ServletContext 等原生的 Servlet API。 

2). 为什么访问 WEB 资源?

	B\S 的应用的 Controller 中必然需要访问 WEB 资源: 向域对象中读写属性, 读写 Cookie, 获取 realPath ....

3). 如何访问 ?

I. 和 Servlet API 解耦的方式: 只能访问有限的 Servlet API 对象, 且只能访问其有限的方法(读取请求参数, 读写域对象的属性, 使 session 失效...). 

> 使用 ActionContext
	
> 实现 XxxAware 接口
	
> 选用的建议: 若一个 Action 类中有多个 action 方法, 且多个方法都需要使用域对象的 Map 或 parameters, 则建议使用
	Aware 接口的方式
	
> session 对应的 Map 实际上是 SessionMap 类型的! 强转后若调用其 invalidate() 方法, 可以使其 session 失效!

II. 和 Servlet API 耦合的方式: 可以访问更多的 Servlet API 对象, 且可以调用其原生的方法.  

> 使用 ServletActionContext
	
> 实现 ServletXxxAware 接口.

## 关于 Struts2 请求的扩展名问题

1). org.apache.struts2 包下的 default.properties 中配置了 Struts2 应用个的一些常量

2). struts.action.extension 定义了当前 Struts2 应用可以接受的请求的扩展名.

3). 可以在 struts.xml 文件中以常量配置的方式修改 default.properties 所配置的常量.

```xml
<constant name="struts.action.extension" value="action,do,"></constant>
```

## ActionSupport

1). ActionSupport 是默认的 Action 类: 若某个 action 节点没有配置 class 属性, 则 ActionSupport 即为待执行的 Action 类. 而 execute 方法即为要默认执行的 action 方法

```xml
<action name="testActionSupport">
	<result>/testActionSupport.jsp</result>
</action>
```

等同于

```xml
<action name="testActionSupport"
	class="com.opensymphony.xwork2.ActionSupport"
	method="execute">
	<result>/testActionSupport.jsp</result>
</action>
```

2). 在手工完成字段验证, 显示错误消息, 国际化等情况下, 推荐继承 ActionSupport. （ActionSupport自己实现了对应的功能）

## result

1). result 是 action 节点的子节点

2). result 代表 action 方法执行后, 可能去的一个目的地

3). 一个 action 节点可以配置多个 result 子节点. 

4). result 的 name 属性值对应着 action 方法可能有的一个返回值. 

```xml
<result name="index">/index.jsp</result>
```

5). result 一共有 2 个属性, 还有一个是 type: 表示结果的响应类型

6). result 的 type 属性值在 struts-default 包的 result-types 节点的 name 属性中定义.

- dispatcher(默认的): 转发. 同 Servlet 中的转发. 
- redirect: 重定向
- redirectAction: 重定向到一个 Action（注意: 通过 redirect 的响应类型也可以便捷的实现 redirectAction 的功能!）

```xml
<result name="index" type="redirectAction">
	<param name="actionName">testAction</param>
	<param name="namespace">/atguigu</param>
</result>
```
OR
```xml
<result name="index" type="redirect">/atguigu/testAction.do</result>
```

- chain: 转发到一个 Action（注意: 不能通过 type=dispatcher 的方式转发到一个 Action）
```xml
<result name="test" type="chain">
	<param name="actionName">testAction</param>
	<param name="namespace">/atguigu</param>
</result>
```

