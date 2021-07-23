# JSON数据交互

## 引入 pom 文件

        <!-- 有关 Jackson 依赖 -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.12.3</version>
        </dependency>

## 定义 JSON 转换器

第一种方式

    <mvc:annotation-driven/>
    
第二种方式

    <!-- 配置 编码为UTF-8 -->
    <bean id="stringConverter" class="org.springframework.http.converter.StringHttpMessageConverter">
        <property name="supportedMediaTypes">
            <list>
                <value>text/plain;charset=UTF-8</value>
            </list>
        </property>
    </bean>
    <!-- 配置 JSON 转换，使用 RequestBody 调用转换器转换为对象形式 -->
    <bean id="jsonConverter" class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"/>
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
        <property name="messageConverters">
            <list>
                <ref bean="stringConverter"/>
                <ref bean="jsonConverter"/>
            </list>
        </property>
    </bean>
    


## controller 类

```java
@Controller
public class JsonController {

    @RequestMapping("/hhh")
    public String f(@RequestBody LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "index";
    }
}
```

注意：需要加上 @RequestBody

@RequestBody注解用于读取http请求的内容(字符串)，通过SpringMVC提供的HttpMessageConverter接口将读到的内容转换为json、xml等格式的数据并绑定到Controller类方法的参数上。 

## 发出请求

```js
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
```

## 原理

在讲解<mvc:annotation-driven/>这个配置之前，我们先了解下Spring的消息转换机制。@ResponseBody这个注解就是使用消息转换机制，最终通过json的转换器转换成json数据的。

HttpMessageConverter接口就是Spring提供的http消息转换接口。

![1](https://images0.cnblogs.com/i/411512/201405/101510002604230.png)

在AnnotationDrivenBeanDefinitionParser源码的152行parse方法中：

分别实例化了RequestMappingHandlerMapping，ConfigurableWebBindingInitializer，RequestMappingHandlerAdapter等诸多类。

其中`RequestMappingHandlerMapping`和`RequestMappingHandlerAdapter`这两个类比较重要。

RequestMappingHandlerMapping处理请求映射的，处理@RequestMapping跟请求地址之间的关系。

RequestMappingHandlerAdapter是请求处理的适配器，也就是请求之后处理具体逻辑的执行，关系到哪个类的哪个方法以及转换器等工作，这个类是我们讲的重点，其中它的属性messageConverters是本文要讲的重点。

具体参见：

[SpringMVC：与前台的json数据交互以及底层实现](https://blog.csdn.net/lch_2016/article/details/81022646)



# Spring MVC拦截器（Interceptor）

在系统中，经常需要在处理用户请求之前和之后执行一些行为，例如检测用户的权限，或者将请求的信息记录到日志中，即平时所说的“权限检测”及“日志记录”。当然不仅仅这些，所以需要一种机制，拦截用户的请求，在请求的前后添加处理逻辑。

Spring MVC 提供了 Interceptor 拦截器机制，用于请求的预处理和后处理。

在开发一个网站时可能有这样的需求：某些页面只希望几个特定的用户浏览。对于这样的访问权限控制，应该如何实现呢？拦截器就可以实现上述需求。在 Struts2 框架中，拦截器是其重要的组成部分，Spring MVC 框架也提供了拦截器功能。

Spring MVC 的拦截器（Interceptor）与 Java Servlet 的过滤器（Filter）类似，它主要用于拦截用户的请求并做相应的处理，通常应用在权限验证、记录请求信息的日志、判断用户是否登录等功能上。

## 拦截器的定义

在 Spring MVC 框架中定义一个拦截器需要对拦截器进行定义和配置，主要有以下 2 种方式。

    通过实现 HandlerInterceptor 接口或继承 HandlerInterceptor 接口的实现类（例如 HandlerInterceptorAdapter）来定义；
    通过实现 WebRequestInterceptor 接口或继承 WebRequestInterceptor 接口的实现类来定义。

本节以实现 HandlerInterceptor 接口的定义方式为例讲解自定义拦截器的使用方法。示例代码如下。

    package net.biancheng.interceptor;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.web.servlet.HandlerInterceptor;
    import org.springframework.web.servlet.ModelAndView;
    public class TestInterceptor implements HandlerInterceptor {
        @Override
        public void afterCompletion(HttpServletRequest request,
                HttpServletResponse response, Object handler, Exception ex)
                throws Exception {
            System.out.println("afterCompletion方法在控制器的处理请求方法执行完成后执行，即视图渲染结束之后执行");
        }
        @Override
        public void postHandle(HttpServletRequest request,
                HttpServletResponse response, Object handler,
                ModelAndView modelAndView) throws Exception {
            System.out.println("postHandle方法在控制器的处理请求方法调用之后，解析视图之前执行");
        }
        @Override
        public boolean preHandle(HttpServletRequest request,
                HttpServletResponse response, Object handler) throws Exception {
            System.out.println("preHandle方法在控制器的处理请求方法调用之前执行");
            return false;
        }
    }

上述拦截器的定义中实现了 HandlerInterceptor 接口，并实现了接口中的 3 个方法，说明如下。

    preHandle( )：该方法在控制器的处理请求方法前执行，其返回值表示是否中断后续操作，返回 true 表示继续向下执行，返回 false 表示中断后续操作。
    postHandle( )：该方法在控制器的处理请求方法调用之后、解析视图之前执行，可以通过此方法对请求域中的模型和视图做进一步的修改。
    afterCompletion( )：该方法在控制器的处理请求方法执行完成后执行，即视图渲染结束后执行，可以通过此方法实现一些资源清理、记录日志信息等工作。


## 拦截器的配置

让自定义的拦截器生效需要在 Spring MVC 的配置文件中进行配置，配置示例代码如下：

    <!-- 配置拦截器 -->
    <mvc:interceptors>
        <!-- 配置一个全局拦截器，拦截所有请求 -->
        <bean class="net.biancheng.interceptor.TestInterceptor" /> 
        <mvc:interceptor>
            <!-- 配置拦截器作用的路径 -->
            <mvc:mapping path="/**" />
            <!-- 配置不需要拦截作用的路径 -->
            <mvc:exclude-mapping path="" />
            <!-- 定义<mvc:interceptor>元素中，表示匹配指定路径的请求才进行拦截 -->
            <bean class="net.biancheng.interceptor.Interceptor1" />
        </mvc:interceptor>
        <mvc:interceptor>
            <!-- 配置拦截器作用的路径 -->
            <mvc:mapping path="/gotoTest" />
            <!-- 定义在<mvc:interceptor>元素中，表示匹配指定路径的请求才进行拦截 -->
            <bean class="net.biancheng.interceptor.Interceptor2" />
        </mvc:interceptor>
    </mvc:interceptors>

在上述示例代码中，元素说明如下。

    <mvc:interceptors>：该元素用于配置一组拦截器。
    <bean>：该元素是 <mvc:interceptors> 的子元素，用于定义全局拦截器，即拦截所有的请求。
    <mvc:interceptor>：该元素用于定义指定路径的拦截器。
    <mvc:mapping>：该元素是 <mvc:interceptor> 的子元素，用于配置拦截器作用的路径，该路径在其属性 path 中定义。path 的属性值为/**时，表示拦截所有路径，值为/gotoTest时，表示拦截所有以/gotoTest结尾的路径。如果在请求路径中包含不需要拦截的内容，可以通过 <mvc:exclude-mapping> 子元素进行配置。


需要注意的是，<mvc:interceptor> 元素的子元素必须按照 <mvc:mapping.../>、<mvc:exclude-mapping.../>、<bean.../> 的顺序配置。 



# Spring MVC异常处理

在 Spring MVC 应用的开发中，不管是操作底层数据库，还是业务层或控制层，都会不可避免地遇到各种可预知的、不可预知的异常。我们需要捕捉处理异常，才能保证程序不被终止。

Spring MVC 有以下 3 种处理异常的方式：

    使用 Spring MVC 提供的简单异常处理器 SimpleMappingExceptionResolver。
    实现 Spring 的异常处理接口 HandlerExceptionResolver，自定义自己的异常处理器。
    使用 @ExceptionHandler 注解实现异常处理

##  1. @ExceptionHandler

局部异常处理仅能处理`指定` Controller 中的异常。

示例 1：下面使用 @ExceptionHandler 注解实现。定义一个处理过程中可能会存在异常情况的 testExceptionHandle 方法。

    @RequestMapping("/testExceptionHandle")
    public String testExceptionHandle(@RequestParam("i") Integer i) {
        System.out.println(10 / i);
        return "success";
    }

显然，当 i=0 时会产生算术运算异常。

下面在同一个类中定义处理异常的方法。

    @ExceptionHandler({ ArithmeticException.class })
    public String testArithmeticException(Exception e) {
        System.out.println("打印错误信息 ===> ArithmeticException:" + e);
        // 跳转到指定页面
        return "error";
    }

注意：该注解不是加在产生异常的方法上，而是加在处理异常的方法上。

异常页面 error.jsp 代码如下。 

    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>
    <head>
        <title>Error</title>
    </head>
    <body>
    <h1>错误页面！</h1>
    </body>
    </html>

控制器输出结果如下。

打印错误信息 ===> ArithmeticException:java.lang.ArithmeticException: / by zero
@ExceptionHandler 注解定义的方法优先级问题：例如发生的是 NullPointerException，但是声明的异常有 RuntimeException 和 Exception，这时候会根据异常的最近继承关系找到继承深度最浅的那个@ExceptionHandler 注解方法，即标记了 RuntimeException 的方法。

    被 @ExceptionHandler 标记为异常处理方法，不能在方法中设置别的形参。但是可以使用 ModelAndView 向前台传递数据。

使用局部异常处理，仅能处理某个 Controller 中的异常，若需要对所有异常进行统一处理，可使用以下两种方法。


## 2. HandlerExceptionResolver

Spring MVC 通过 HandlerExceptionResolver 处理程序异常，包括处理器异常、数据绑定异常以及控制器执行时发生的异常。HandlerExceptionResolver 仅有一个接口方法，源码如下。

    public interface HandlerExceptionResolver {
        @Nullable
        ModelAndView resolveException(
                HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex);
    }

发生异常时，Spring MVC 会调用 resolveException() 方法，并转到 ModelAndView 对应的视图中，返回一个异常报告页面反馈给用户。

示例 2：在 net.biancheng.exception 包中创建一个 HandlerExceptionResolver 接口的实现类 MyExceptionHandler，代码如下。

    package net.biancheng.exception;
    import java.util.HashMap;
    import java.util.Map;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.web.servlet.HandlerExceptionResolver;
    import org.springframework.web.servlet.ModelAndView;
    public class MyExceptionHandler implements HandlerExceptionResolver {
        @Override
        public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
                Exception arg3) {
            Map<String, Object> model = new HashMap<String, Object>();
            // 根据不同错误转向不同页面（统一处理），即异常与View的对应关系
            if (arg3 instanceof ArithmeticException) {
                return new ModelAndView("error", model);
            }
            return new ModelAndView("error-2", model);
        }
    }

在 springmvc-servlet.xml 文件中添加以下代码。

    <!--托管MyExceptionHandler-->
    <bean class="net.biancheng.exception.MyExceptionHandler"/>

再次访问 http://localhost:8080/json/testExceptionHandle?i=0，页面跳转到 error.jsp 页面


## 3. SimpleMappingExceptionResolver

全局异常处理可使用 SimpleMappingExceptionResolver 来实现。它将异常类名映射为视图名，即发生异常时使用对应的视图报告异常。

示例 3：在 springmvc-servlet.xml 中配置全局异常，代码如下。

    <bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
        <!-- 定义默认的异常处理页面，当该异常类型注册时使用 -->
        <property name="defaultErrorView" value="error"></property>
        <!-- 定义异常处理页面用来获取异常信息的变量名，默认名为exception -->
        <property name="exceptionAttribute" value="ex"></property>
        <!-- 定义需要特殊处理的异常，用类名或完全路径名作为key，异常页名作为值 -->
        <property name="exceptionMappings">
            <props>
                <prop key="ArithmeticException">error</prop>
                <!-- 在这里还可以继续扩展对不同异常类型的处理 -->
            </props>
        </property>
    </bean>

再次访问 http://localhost:8080/json/testExceptionHandle?i=0，页面跳转到 error.jsp 页面


