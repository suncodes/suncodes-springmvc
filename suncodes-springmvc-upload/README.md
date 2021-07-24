# Spring MVC 上传文件(upload files)

上传功能是一个web应用很常用的一个功能，比如在一些社交网站上传些图片、视频等。本篇文章主要研究了spring mvc是如何实现文件上传功能的，在具体讲解spring mvc如何实现处理文件上传之前，必须弄明白与文件上传相关的multipart请求。

## 一、关于multipart 请求

我们传统的表单提交的一般都是文本类型的数据，比如我们的注册表单，当提交表单时，表单中的“属性-值”对会被拼接成一个字符串：

    firstName=Charles&lastName=Xavier&email=professorx%40xmen.org&username=professorx&password=letmein01

这种处理方式很简单也很有效，但是对于图片、视频等二进制数据就不能这么处理了，这里就要用到multipart表单了。multipart表单和上面介绍的普通表单不同，它会把表单分割成块，表单中的每个字段对应一个块，每个块都有自己的数据类型。也就是说，对于上传字段对应的块，它的数据类型就可以是二进制了：

```text
------WebKitFormBoundaryqgkaBn8IHJCuNmiW
Content-Disposition: form-data; name="firstName"
Charles
------WebKitFormBoundaryqgkaBn8IHJCuNmiW
Content-Disposition: form-data; name="lastName"
Xavier
------WebKitFormBoundaryqgkaBn8IHJCuNmiW
Content-Disposition: form-data; name="email"
charles@xmen.com
------WebKitFormBoundaryqgkaBn8IHJCuNmiW
Content-Disposition: form-data; name="username"
professorx
------WebKitFormBoundaryqgkaBn8IHJCuNmiW
Content-Disposition: form-data; name="password"
letmein01
------WebKitFormBoundaryqgkaBn8IHJCuNmiW
Content-Disposition: form-data; name="profilePicture"; filename="me.jpg"
Content-Type: image/jpeg
[[ Binary image data goes here ]]
------WebKitFormBoundaryqgkaBn8IHJCuNmiW--

```

在上面这个请求就是mutipart 请求，最后一个字段profilePicture有自己的Content-Type，值是image/jpeg，而其它字段都是简单的文本类型。

虽然mutipart请求看起来比较复杂，但是在spring mvc中处理起来是非常简单的。在写我们处理上传文件的controller之前，我们得先配置一个Mutipart Resolver来告诉DispatchServlet如何解析一个mutipart 请求。

## 二、配置mutipart resolver

实现文件上传，其实就是解析一个Mutipart请求。DispatchServlet自己并不负责去解析mutipart 请求，而是委托一个实现了MultipartResolver接口的类来解析mutipart请求。在Spring3.1之后Spring提供了两个现成的MultipartResolver接口的实现类：

- CommonMutipartResolver：通过利用Jakarta Commons FileUpload来解析mutipart 请求
- StandardServletMutipartResolver：依赖Servlet3.0来解析mutipart请求

`所以要实现文件上传功能，只需在我们的项目中配置好这两个bean中的任何一个即可。` 其实这两个都很好用，如果我们部署的容器支持Servlet3.0，我们完全可以使用StandardServletMutipartResolver。但是如果我们的应用部署的容器不支持Servlet3.0或者用到的Spring版本是3.1以前的，那么我们就需要用到CommonMutipartResolver了。下面就具体介绍一下两种bean的配置，当然也是实现文件上传的两种配置。

方式一： 通过StandardServletMutipartResolver解析mutipart 请求

1.配置multipartResolver的bean

```text
<bean id="viewResolver" class="org.springframework.web.servlet.view.UrlBasedViewResolver">
    <property name="viewClass" value="org.springframework.web.servlet.view.InternalResourceView"/>
    <property name="prefix" value="/WEB-INF/jsp/"/>
    <property name="suffix" value=".jsp"/>
</bean>

<bean id="multipartResolver" class="org.springframework.web.multipart.support.StandardServletMultipartResolver"/>
```

2.配置MutipartResolver相关属性

StandardServletMutipartResolver依赖于Servlet3.0，所以要想使用StandardServletMutipartResolver，我们还必须在DispatchServlet配置里面 注册一个 MultipartConfigElement元素，具体配置方式如下：

```text
<servlet>
    <servlet-name>appServlet</servlet-name>
    <servlet-class> org.springframework.web.servlet.DispatcherServlet </servlet-class>
    <load-on-startup>1</load-on-startup>
    <multipart-config>
        <location>/tmp/spittr/uploads</location>
        <max-file-size>2097152</max-file-size>
        <max-request-size>4194304</max-request-size>
    </multipart-config>
</servlet>
```

mutipart-config里面有三个配置项：

1. location：上传文件用到的临时文件夹，是一个绝对路径，需要注意，这个属性是必填的
2. max-file-size:上传文件的最大值，单位是byte，默认没有限制
3. max-request-size:整个mutipart请求的最大值，单位是byte，默认没有限制


方式二：通过CommonMutipartResolver 解析mutipart 请求

当然，如果我们部署的容器不是Servlet3.0，我们还可以使用CommonMutipartResolver，不过这个需要依赖Apache的commons-fileupload第三方类库。

1.配置第三方依赖

```text
<dependency>
        <groupId>commons-fileupload</groupId>
        <artifactId>commons-fileupload</artifactId>
        <version>1.4</version>
</dependency>
```

2.配置multipartResolver的bean

```text
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
       <property name="maxUploadSize" value="100000" />
       <property name="maxInMemorySize" value="100000" />
</bean>
```

使用CommonMutipartResolver不需要在Servlet中配置MultipartConfigElement元素，上传文件的location属性也是可选的。

大家可能有个小疑问，上面两种方式都配置了一个id=”multipartResolver”的bean，那么DispatchServlet是如何找到这个bean的呢？我们可以看一下DispatchServlet的源码，里面有这么一个方法：

```java
 private void initMultipartResolver(ApplicationContext context) {
    try {
        this.multipartResolver = (MultipartResolver)context.getBean("multipartResolver", MultipartResolver.class);
        if(this.logger.isDebugEnabled()) {
            this.logger.debug("Using MultipartResolver [" + this.multipartResolver + "]");
        }
    } catch (NoSuchBeanDefinitionException var3) {
        this.multipartResolver = null;
        if(this.logger.isDebugEnabled()) {
            this.logger.debug("Unable to locate MultipartResolver with name \'multipartResolver\': no multipart request handling provided");
        }
    }

}
```

这个方法会默认从Spring的上下文中获取id为multipartResolver的bean作为它的MutipartResolver。

## 三、写一个上传文件的controller

按照上面的任何一种方式配置好，Spring就已经准备好接受mutipart请求了，下面就需要写一个controller来接收上传的文件了，请看代码：

```java
package controller;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
public class UploadController {

    @RequestMapping("/uploadFile")
    public String f(MultipartFile file) throws IOException {
        String name = file.getName();
        String originalFilename = file.getOriginalFilename();
        System.out.println("name: " + name);
        System.out.println("originalFilename: " + originalFilename);

        // ------------------

        byte[] bytes = file.getBytes();
        FileUtils.writeByteArrayToFile(new File("1.docx"), bytes);

        return "index";
    }
}

```

uploadFileHandler方法中有一个参数file，它的类型是MutipartFile，也就是说Spring 会自动把mutipart请求中的二进制文件转换成MutipartFile类型的对象，这么做有什么好处呢？我们具体看一下MutipartFile这个接口：

```text
public interface MultipartFile {
    String getName();
    String getOriginalFilename();
    String getContentType();
    boolean isEmpty();
    long getSize();
    byte[] getBytes() throws IOException;
    InputStream getInputStream() throws IOException;
    void transferTo(File var1) throws IOException, IllegalStateException;
}
```

我们可以看到MutipartFile接口提供了很多方法，诸如获取上传文件的名称、内容类型、大小等等，甚至还提供了转换成File类型文件的方法。想想如果我们接收到仅仅是一个字节数组，那用起来该多么麻烦，感激这个MutipartFile吧。

## 四、写个upload.jsp页面测试一下

```jsp
<form method="POST" action="${pageContext.request.contextPath}/uploadFile" enctype="multipart/form-data">
    File to upload: <input type="file" name="file"> <br/>
    <input type="submit" value="Upload"> <br/>
    Press here to upload the file!
</form>
```

其中只有一点需要注意，就是表单的enctype属性，这个属性值multipart/form-data会告诉浏览器我们提交的是一个Mutipart请求而不是一个普通的form请求。


## 五、有关乱码问题

### 1. 请求参数乱码问题
```xml
    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

### 2、响应参数乱码问题

    <!-- 响应参数 utf-8编码 -->
    <mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8" />
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>


## 六、文件上传及参数接受

controller 类：

```java
    @RequestMapping("/uploadFile1")
    public String f1(MultipartFile file, String param) throws IOException {

        System.out.println("参数param：" + param);

        String name = file.getName();
        String originalFilename = file.getOriginalFilename();
        System.out.println("name: " + name);
        System.out.println("originalFilename: " + originalFilename);

        // ------------------

        byte[] bytes = file.getBytes();
        FileUtils.writeByteArrayToFile(new File("1.docx"), bytes);

        return "index";
    }
```

jsp 文件：

```jsp
<form method="POST" action="${pageContext.request.contextPath}/uploadFile1" enctype="multipart/form-data">
    File to upload: <input type="file" name="file"> <br/>
    <input type="text" name="param"> <br/>
    <input type="submit" value="Upload"> <br/>
    Press here to upload the file!
</form>
```

## 七、多文件上传

controller 类：

```java
    @RequestMapping("/uploadFile2")
    public String f1(MultipartFile[] file, String param) throws IOException {

        System.out.println("参数param：" + param);
        for (MultipartFile multipartFile : file) {
            System.out.println(multipartFile.getOriginalFilename());
        }
        return "index";
    }
```

jsp：

```jsp
多文件上传：
<form method="POST" action="${pageContext.request.contextPath}/uploadFile2" enctype="multipart/form-data">
    File to upload: <input type="file" name="file"> <br/>
    File to upload: <input type="file" name="file"> <br/>
    <input type="text" name="param"> <br/>
    <input type="submit" value="Upload"> <br/>
    Press here to upload the file!
</form>
```

## 八、使用 POJO 接收参数

实体类：
```java
public class UploadBO {
    private MultipartFile file;
    private String param;
}
```

controller 类：
```java
    @RequestMapping("/uploadFile3")
    public String f3(UploadBO uploadBO) throws IOException {

        System.out.println("参数param：" + uploadBO.getParam());

        String name = uploadBO.getFile().getName();
        String originalFilename = uploadBO.getFile().getOriginalFilename();
        System.out.println("name: " + name);
        System.out.println("originalFilename: " + originalFilename);

        return "index";
    }
```

JSP：
```jsp
<form method="POST" action="${pageContext.request.contextPath}/uploadFile3" enctype="multipart/form-data">
    File to upload: <input type="file" name="file"> <br/>
    <input type="text" name="param"> <br/>
    <input type="submit" value="Upload"> <br/>
    Press here to upload the file!
</form>
```


