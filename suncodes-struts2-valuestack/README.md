# 值栈（ValueStack）

1. 关于值栈:

1). helloWorld 时, ${productName} 读取 productName 值, 实际上该属性并不在 request 等域对象中, 而是从值栈中获取的. 

2). ValueStack: 

I.  可以从 ActionContext 中获取值栈对象
II. 值栈分为两个逻辑部分

> Map 栈: 实际上是 OgnlContext 类型, 是个 Map, 也是对 ActionContext 的一个引用. 里边保存着各种 Map:
	         requestMap, sessionMap, applicationMap, parametersMap, attr
	         
> 对象栈: 实际上是 CompoundRoot 类型, 是一个使用 ArrayList 定义的栈. 里边保存各种和当前 Action 实例相关的对象.
	                   是一个数据结构意义的栈.
	                   
 2. Struts2 利用 s:property 标签和 OGNL 表达式来读取值栈中的属性值
 
 1). 值栈中的属性值:
 
> 对于对象栈: 对象栈中某一个对象的属性值
 	
> Map 栈: request, session, application 的一个属性值 或 一个请求参数的值. 
 	
 2). 读取对象栈中对象的属性:
 
> 若想访问 Object Stack 里的某个对象的属性. 可以使用以下几种形式之一: 
		
	  object.propertyName ; object['propertyName'] ; object["propertyName"]	
		
> ObjectStack 里的对象可以通过一个从零开始的下标来引用. ObjectStack 里的栈顶对象可以用 [0] 来引用, 
	     它下面的那个对象可以用 [1] 引用. 
	   
	  [0].message   
	     
> [n] 的含义是从第 n 个开始搜索, 而不是只搜索第 n 个对象
	
> 若从栈顶对象开始搜索, 则可以省略下标部分: message 
	
> 结合 s:property 标签: <s:property value="[0].message" />  <s:property value="message" />
 
 3). 默认情况下, Action 对象会被 Struts2 自动的放到值栈的栈顶. 


## 什么是值栈

ValueStack是Struts2的一个接口，字面意义为值栈，OgnlValueStack是ValueStack的一个实现类，客户端发起一个请求struts2架构就会创建一个action实例同时创建一个OgnlValueStack值栈实例。OgnlValueStack贯穿整个Action的生命周期，struts2中使用OGNL将请求Action的参数封装为对象储存到值栈中，并通过ONGL表达式读取值栈中的对象属性值

ValueStack其实类似于一个数据中转站（strets2中的数据都在其中）

ValueStack贯穿Action整个生命周期（Action一旦创建了，框架就会创建一个ValueStack对象）

## 值栈的内部结构

```text
ValueStack主要有两个内部区域
root区域 ：其实就是一个ArrayList.里面一般放置对象。获取root对象不需要加#
context区域 ：其实就是一个Map.里面放置的是web开发常用对象的数据引用。获取context数据需要加#
request
session
application
parameters
attr
```

所说的操作值栈，通常指的是操作ValueStack中的root区域

## ActionContext和值栈的关系

ServletContext： Servlet的上下文

ActionContext: Action的上下文

Struts2中一个请求过来时，执行过滤器中的doFilter方法，在这个方法中创建ActionContext, 在创建ActionContext中创建ValueStack对象，将ValueStack对象传递到ActionContext中。所以可以通过ActionContext对象获得ValueStack对象

ActionContext对象之所以能够访问Servlet的API（访问的是域对象的数据）。因为在其内部有值栈的引用


值栈内部主要由两部分构成: root和context;

　　root --> compoundroot extends ArrayList --> root是由list集合构成的

　　context --> ognlcontext implements Map --> context是由map集合构成的

可以通过ognl表达式中的\<s:debug\>标签查看值栈的内部结构

　　在值栈中, 操作的数据一般是root数据(默认的在action中没有任何操作时, root栈顶元素就是action的引用). 而context中主要存放的是一些对象的引用.

　　OgnlContext的主要结构为:request, session, application, parameters, attr, 他们对应的值分别是:request对象的引用, HttpSession的引用, ServletContext对象的引用, 请求参数(action后面的请求参数), 获取域对象.


## 值栈对象的获取方法

每个action只对应一个值栈对象. 

通过ActionContext获取

通过request获取

```java
// 通过ActionContext获得
ValueStack valueStack = ActionContext.getContext().getValueStack();
// 通过request获得
ValueStack valueStack2 = (ValueStack) ServletActionContext.getRequest().getAttribute(ServletActionContext.STRUTS_VALUESTACK_KEY);
// 一个Action实例只会有一个值栈对象
System.out.println(valueStack ==valueStack2);
```

## 值栈中存放数据

###  通过值栈对象中的set方法

```java
//1.通过值栈中的set方法，往值栈的root中存值
ActionContext context = ActionContext.getContext();
ValueStack stack = context.getValueStack();
stack.set("key", "value");
```

### 通过值栈对象中的put方法存值

```java
//2.通过值栈中的push方法，往栈顶添加值
stack.push("TopstackElement");
```

### 在action中定义成员变量并生成相应的get方法

> 1、值栈中存放基本数据类型

```java
package com.rodge.stack;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

public class SaveValueStack extends ActionSupport {
    //设置序列化版本号
    private static final long serialVersionUID = 1L;
    private String username = "";
    public String getUsername() {
        return username;
    }
    @Override
    public String execute() throws Exception {     
        //3.通过成员变量+get方法，设置值栈中的值，不用单独分配内存空间
        username = "stackValue";
        return "save";
    }
}
```

> 2、值栈中存放对象
```java
package com.rodge.stack;
import com.opensymphony.xwork2.ActionSupport;
public class SaveObjectInStack extends ActionSupport {
    private static final long serialVersionUID = 1L;
    private User user = new User();
    public User getUser() {
        return user;
    }
    @Override
    public String execute() throws Exception {
        user.setUsername("小新");
        user.setAddress("Japen");
        user.setDesc("6毛");
        return "save";
    }  
}
```

> 3、将集合存放在值栈中

```java
package com.rodge.stack;
import java.util.ArrayList;
import java.util.List;
import com.opensymphony.xwork2.ActionSupport;
public class ListStackAction extends ActionSupport {
 static final long serialVersionUID = 1L;
    private List<User> list = new ArrayList<User>();
    public List<User> getList() {
        return list;
    }
    @Override
    public String execute() throws Exception {
        User user1 = new User();
        user1.setUsername("小明");
        user1.setAddress("北京");
        user1.setDesc("教师外面");


        User user2 = new User();
        user2.setUsername("小王");
        user2.setAddress("隔壁");
        user2.setDesc("小王叔叔");


        list.add(user1);
        list.add(user2);
        return "list";
    }
}
```

## 在jsp页面中获取值栈数据

使用struts2标签和ognl表达式获取值栈数据

```text
<%@ taglib uri="/struts-tags" prefix="s" %>
```

### 获取值栈中的字符串数据
```text
<s:property value="username" />
```

### 获取值栈中存放的对象数据

```text
<s:property value="user.username" />
<s:property value="user.address" />
<s:property value="user.desc" />
```

### 获取值栈中存放的list集合

```text
    <!-- 第一种获取list集合的方法 -->
    <s:property value="list[0].username" />
    <s:property value="list[0].address" />
    <s:property value="list[0].desc" />
    <hr/>
    <!-- 第二种获取list集合中数据的方法 -->
    <s:iterator value="list">
        <s:property value="username" />
        ----
        <s:property value="address" />
        ----
        <s:property value="desc" />
        <br/>
    </s:iterator>
    <hr/>

    <!-- 第三种获取list集合中数据的方法 -->
    <s:iterator value="list" var="user">
        <s:property value="#user.username" />
        --------
        <s:property value="#user.address" />
        --------
        <s:property value="#user.desc" />
        <br/>
    </s:iterator>
```

### 值栈中获取其他数据

- 获取始终值栈对象的set方法存放在值栈中的数据stack.set("key", "value");
```text
<s:property value="key" />
```

- 使用值栈对象里面的push方法把数据放到值栈里面

```text
<!-- 获取push方法存放到值栈中的数据 -->
    获取push方法存放到值栈中的数据：
    <s:property value="[0].top" />
```


- 为什么EL表达式可以获取值栈中的数据

EL表达式并不是直接获取值栈中的数据. 是因为struts2中增强了reqeust总的getAttribute方法, 增强的getAttribute方法首先会在request域中寻找是否有值, 如果request域中有, 则直接返回; 如果域对象里面如果没有值，得到值栈对象，从值栈对象里面把值获取到，最后放到域对象里面.


## Ognl中#和%的使用

1  #： 获取context里面的数据

![](https://images2015.cnblogs.com/blog/1095882/201703/1095882-20170303175651985-1557572034.png)

![](https://images2015.cnblogs.com/blog/1095882/201703/1095882-20170303175702204-1445504147.png)

2  %：  的使用

（1）使用struts2的表单标签，显示值，使用ognl表达式

```
<s:textfield value="#request.username"></s:textfield>
```

结果：#request.username

写ognl作为字符串显示出来，没有作为ognl表达式执行

（2）使用%让表单标签里面值作为ognl执行

```text
<s:textfield value="%request.username"></s:textfield>
```
