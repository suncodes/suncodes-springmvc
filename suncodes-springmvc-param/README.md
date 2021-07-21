# Spring MVC传递参数

根据 Content-Type 进行分类，来看每种常用的有几种接收参数的方式。

Spring MVC Controller 接收请求参数的方式有很多种，有的适合 get 请求方式，有的适合 post 请求方式，有的两者都适合。主要有以下几种方式：

- 通过实体 Bean 接收请求参数
- 通过处理方法的形参接收请求参数
- 通过 HttpServletRequest 接收请求参数
- 通过 @PathVariable 接收 URL 中的请求参数
- 通过 @RequestParam 接收请求参数
- 通过 @ModelAttribute 接收请求参数
- 通过 @RequestBody 接收 JSON 参数（Bean，Map等）

## 1、GET 方式/URL 参数

### 1.1、通过实体 Bean 接收请求参数

实体 Bean 可以接收 URL 参数，Bean 的属性名称必须与请求参数名称相同。

```java
    @RequestMapping(value = "/url", method = RequestMethod.GET)
    public String f(LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }
```

URL 请求：

```text
GET /param/url?city=郑州&person=SUNCHUIZHE&target=LIVE HTTP/1.1
Host: localhost:8080
Cookie: JSESSIONID=8851E398038E4BB6EB1599E2CAF63BDE
```

结果：
```text
LifeBO{city='郑州', person='SUNCHUIZHE', target='LIVE'}
```

特别说明：`POST` 方式异同。

### 1.2、通过处理方法的形参接收请求参数

```java
    @RequestMapping(value = "/url1", method = RequestMethod.GET)
    public String f1(String city, String person, String target) {
        System.out.println("city  : " + city);
        System.out.println("person: " + person);
        System.out.println("target: " + target);
        return "param";
    }
```

http://localhost:8080/param/url1?city=郑州&person=SUNCHUIZHE&target=LIVE

```text
city  : 郑州
person: SUNCHUIZHE
target: LIVE
```

### 1.3、通过 HttpServletRequest 接收请求参数

略

### 1.4、通过 @PathVariable 接收 URL 中的请求参数

```java
    @RequestMapping(value = "/url2/{city}/{person}/{target}", method = RequestMethod.GET)
    public String f2(@PathVariable String city, @PathVariable String person, @PathVariable String target) {
        System.out.println("city  : " + city);
        System.out.println("person: " + person);
        System.out.println("target: " + target);
        return "param";
    }
```

在访问“http://localhost:8080/param/url2/1/2/中文”路径时，上述代码会自动将 URL 中的模板变量 {city}、{person} 和 {target} 绑定到通过 @PathVariable 注解的同名参数上，即:
    
    city  : 1
    person: 2
    target: 中文

### 1.5、通过 @RequestParam 接收请求参数

在方法入参处使用 @RequestParam 注解指定其对应的请求参数。@RequestParam 有以下三个参数：

    value：参数名
    required：是否必须，默认为 true，表示请求中必须包含对应的参数名，若不存在将抛出异常
    defaultValue：参数默认值

略

### 1.6、通过 @ModelAttribute 接收请求参数

@ModelAttribute 注解用于将多个请求参数封装到一个实体对象中，从而简化数据绑定流程，而且自动暴露为模型数据，在视图页面展示时使用。

而“通过实体 Bean 接收请求参数”中只是将多个请求参数封装到一个实体对象，并不能暴露为模型数据（需要使用 model.addAttribute 语句才能暴露为模型数据，数据绑定与模型数据展示后面教程中会讲解）。

通过 @ModelAttribute 注解接收请求参数适用于 get 和 post 提交请求方式

```java
    @RequestMapping(value = "/url3", method = RequestMethod.GET)
    public String f3(@ModelAttribute("user") LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }
```

@ModelAttribute 声明在方法参数的作用：（1）获取参数；（2）把获取的参数放入request.addAttribute

```jsp
<%=request.getAttribute("user")%>
```


## 2、POST 方式/urlencoded 参数

### 2.1、通过实体 Bean 接收请求参数
```java
    @RequestMapping(value = "/urlencoded", method = RequestMethod.POST)
    public String u(LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }
```

### 2.2、通过处理方法的形参接收请求参数
```java
    @RequestMapping(value = "/urlencoded1", method = RequestMethod.POST)
    public String u1(String city, String person, String target) {
        System.out.println("city  : " + city);
        System.out.println("person: " + person);
        System.out.println("target: " + target);
        return "param";
    }
```

### 2.3、通过 HttpServletRequest 接收请求参数
略

### 2.4、通过 @RequestParam 接收请求参数
略

### 2.5、通过 @ModelAttribute 接收请求参数
```java
    @RequestMapping(value = "/urlencoded2", method = RequestMethod.POST)
    public String u2(@ModelAttribute("user") LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }
```


## 3、POST 方式/formdata 参数

```text

POST /param/form-data HTTP/1.1
Host: localhost:8080
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
Cookie: JSESSIONID=B5ED422FF03DFE26DE1F9F32A392A3E5
Content-Length: 297

----WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="city"

1
----WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="person"

2
----WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="target"

3
----WebKitFormBoundary7MA4YWxkTrZu0gW

```

`说明：`

Spring MVC 框架的对解析 mutilpart/form-data 基于 commons-fileupload 组件，并在该组件上做了进一步的封装，简化了文件上传的代码实现，取消了不同上传组件上的编程差异。 

具体信息，见 文件上传下载

添加 Jar 包：

```xml
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.4</version>
        </dependency>
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.4</version>
        </dependency>
```

配置解析器：

MultpartResolver 接口有以下两个实现类：

    StandardServletMultipartResolver：使用了 Servlet 3.0 标准的上传方式。
    CommonsMultipartResolver：使用了 Apache 的 commons-fileupload 来完成具体的上传操作。

    <bean id="multipartResolver"
          class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="defaultEncoding" value="UTF-8"/>
    </bean>


### 3.1、通过实体 Bean 接收请求参数

```java
    @RequestMapping(value = "/form-data", method = RequestMethod.POST, consumes = "multipart/form-data")
    public String d(LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }
```

### 3.2、通过处理方法的形参接收请求参数

```java
    @RequestMapping(value = "/form-data1", method = RequestMethod.POST)
    public String d1(String city, String person, String target) {
        System.out.println("city  : " + city);
        System.out.println("person: " + person);
        System.out.println("target: " + target);
        return "param";
    }
```

### 3.3、通过 HttpServletRequest 接收请求参数
略

### 3.4、通过 @RequestParam 接收请求参数
```java
    @RequestMapping(value = "/form-data2", method = RequestMethod.POST)
    public String d2(@RequestParam("city") String city,
                     @RequestParam("person")String person,
                     @RequestParam("target")String target) {
        System.out.println("city  : " + city);
        System.out.println("person: " + person);
        System.out.println("target: " + target);
        return "param";
    }
```

### 3.5、通过 @ModelAttribute 接收请求参数
```java
    @RequestMapping(value = "/form-data3", method = RequestMethod.POST)
    public String d3(@ModelAttribute("user") LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }
```

## 4、POST 方式/JSON 参数

略，JSON 解析需要配置，后续需要跟进



