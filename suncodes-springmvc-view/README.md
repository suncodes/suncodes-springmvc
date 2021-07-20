## 常用的视图解析器

### URLBasedViewResolver

UrlBasedViewResolver 是对 ViewResolver 的一种简单实现，主要提供了一种拼接 URL 的方式来解析视图。

UrlBasedViewResolver 通过 prefix 属性指定前缀，suffix 属性指定后缀。当 ModelAndView 对象返回具体的 View 名称时，它会将前缀 prefix 和后缀 suffix 与具体的视图名称拼接，得到一个视图资源文件的具体加载路径，从而加载真正的视图文件并反馈给用户。

使用 UrlBasedViewResolver 除了要配置前缀和后缀属性之外，还需要配置“viewClass”，表示解析成哪种视图。

配置 DispatcherServlet：

```xml
    <servlet>
        <servlet-name>view-resolver</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/view-base-servlet.xml</param-value>
        </init-param>
        <!-- 表示容器再启动时立即加载servlet -->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>view-resolver</servlet-name>
        <!-- 处理所有URL -->
        <url-pattern>/base/*</url-pattern>
    </servlet-mapping>
```

配置 视图解析器 view-base-servlet.xml：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- LoginController控制器类，映射到"/login" -->
    <bean name="/login" class="controller.LoginController"/>
    <!-- LoginController控制器类，映射到"/register" -->
    <bean name="/register" class="controller.RegisterController"/>

    <bean id="viewResolver" class="org.springframework.web.servlet.view.UrlBasedViewResolver">
        <property name="viewClass" value="org.springframework.web.servlet.view.InternalResourceView" />
        <property name="prefix" value="/WEB-INF/jsp"/>
        <property name="suffix" value=".jsp"/>
    </bean>

</beans>
```

controller 类：
```java
public class LoginController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {
        return new ModelAndView("/register");
    }
}
```
 另外，存放在 /WEB-INF/ 目录下的内容不能直接通过 request 请求得到，所以为了安全性考虑，通常把 jsp 文件放在 WEB-INF 目录下。

### InternalResourceViewResolver

InternalResourceViewResolver 为“内部资源视图解析器”，是日常开发中最常用的视图解析器类型。它是 URLBasedViewResolver 的子类，拥有 URLBasedViewResolver 的一切特性。

InternalResourceViewResolver 能自动将返回的视图名称解析为 InternalResourceView 类型的对象。InternalResourceView 会把 Controller 处理器方法返回的模型属性都存放到对应的 request 属性中，然后通过 RequestDispatcher 在服务器端把请求 forword 重定向到目标 URL。也就是说，使用 InternalResourceViewResolver 视图解析时，无需再单独指定 viewClass 属性。

```xml
    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="viewClass" value="org.springframework.web.servlet.view.InternalResourceView" />
        <property name="prefix" value="/WEB-INF/jsp"/>
        <property name="suffix" value=".jsp"/>
    </bean>
```

### MappingJackson2JsonView

POM 依赖：
```xml
        <!-- 有关 Jackson 依赖 -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.12.3</version>
        </dependency>
```

视图解析器：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean name="/jsonViewResolver" class="controller.JsonViewController"/>
    <!--Basic application beans. -->
    <bean id="viewResolver"
          class="org.springframework.web.servlet.view.ContentNegotiatingViewResolver">
        <!--<property name="contentNegotiationManager" ref="cnManager"/>-->
        <property name="defaultViews">
            <list>
                <bean class="org.springframework.web.servlet.view.json.MappingJackson2JsonView">
                    <!-- <property name="extractValueFromSingleKeyModel" value="true"/> -->
                    <property name="prettyPrint" value="true"/>
                    <property name="contentType" value="text/plain"/>
                </bean>
            </list>
        </property>
    </bean>
</beans>
```

controller 类：

```java
public class JsonViewController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        /*
         * {
         *   "jsonBO" : {
         *     "address" : "郑州",
         *     "weather" : "暴雨"
         *   }
         * }
         */
        JsonBO jsonBO = new JsonBO();
        jsonBO.setAddress("郑州");
        jsonBO.setWeather("暴雨");
        modelAndView.addObject(jsonBO);
        return modelAndView;
    }
}
```

