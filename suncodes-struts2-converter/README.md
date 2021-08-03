# 类型转换

- 类型转换概述
- 类型转换出错时如何进行处理
- 转到哪个页面
- 显示什么错误消息
- 自定义类型转换器
- 类型转换与复杂对象配合使用

## 概述

从一个 HTML 表单到一个 Action 对象, 类型转换是从字符串到非字符串. 

HTTP 没有 “类型” 的概念. 每一项表单输入只可能是一个字符串或一个字符串数组. 在服务器端, 必须把 String 转换为特定的数据类型

在 struts2 中, 把请求参数映射到 action  属性的工作由 Parameters 拦截器负责, 它是默认的 defaultStack 拦截器中的一员. Parameters 拦截器可以自动完成字符串和基本数据类型之间转换. 

## 类型转换错误

如果类型转换失败:

- 若 Action 类没有实现 ValidationAware 接口： Struts 在遇到类型转换错误时仍会继续调用其 Action 方法, 就好像什么都没发生一样.

- 若 Action 类实现 ValidationAware 接口：Struts 在遇到类型转换错误时将不会继续调用其 Action 方法:  Struts 将检查相关 action 元素的声明是否包含着一个 name=input 的 result.  如果有, Struts 将把控制权转交给那个 result  元素; 若没有 input 结果, Struts 将抛出一个异常

## 定制类型转换错误消息

```text
问题1: 如何覆盖默认的错误消息?
1). 在对应的 Action 类所在的包中新建  
    ActionClassName.properties 文件, ActionClassName 即为包含着输入字段的 Action 类的类名
2). 在属性文件中添加如下键值对: invalid.fieldvalue.fieldName=xxx


问题2: 如果是 simple 主题, 还会自动显示错误消息吗? 如果不会显示, 怎么办 ?
1). 通过 debug 标签, 可知若转换出错, 则在值栈的 Action(实现了 ValidationAware 接口) 对象中有一个  fieldErrors 属性.
该属性的类型为 Map<String, List<String>> 键: 字段(属性名), 值: 错误消息组成的 List. 所以可以使用 LE 或 OGNL 的方式
来显示错误消息: ${fieldErrors.age[0]}

2). 还可以使用 s:fielderror 标签来显示. 可以通过 fieldName 属性显示指定字段的错误.

问题3. 若是 simple 主题, 且使用  <s:fielderror fieldName="age"></s:fielderror> 来显示错误消息, 则该消息在一个 
ul, li, span 中. 如何去除 ul, li, span 呢 ?
在 template.simple 下面的 fielderror.ftl 定义了 simple 主题下, s:fielderror 标签显示错误消息的样式. 所以修改该
配置文件即可. 在 src 下新建  template.simple 包, 新建 fielderror.ftl 文件, 把原生的 fielderror.ftl 中的内容
复制到新建的 fielderror.ftl 中, 然后剔除 ul, li, span 部分即可. 

问题4. 如何自定义类型转换器 ?  
1). 为什么需要自定义的类型转换器 ? 因为 Struts 不能自动完成 字符串 到 引用类型 的 转换.
2). 如何定义类型转换器:
I.  开发类型转换器的类: 扩展 StrutsTypeConverter 类.
II. 配置类型转换器: 
有两种方式
①. 基于字段的配置: 
	> 在字段所在的 Model(可能是 Action, 可能是一个 JavaBean) 的包下, 新建一个 ModelClassName-conversion.properties 文件
	> 在该文件中输入键值对: fieldName=类型转换器的全类名. 
	> 第一次使用该转换器时创建实例. 
	> 类型转换器是单实例的!	

②. 基于类型的配置:
	> 在 src 下新建 xwork-conversion.properties
	> 键入: 待转换的类型=类型转换器的全类名.
	> 在当前 Struts2 应用被加载时创建实例. 

```

# 文件的上传

1). 表单需要注意的 3 点

2). Struts2 的文件上传实际上使用的是 Commons FileUpload 组件, 所以需要导入

```text
commons-fileupload-1.3.jar
commons-io-2.0.1.jar

```
3). Struts2 进行文件上传需要使用 FileUpload 拦截器

4). 基本的文件的上传: 直接在 Action 中定义如下 3 个属性, 并提供对应的 getter 和 setter

```text
//文件对应的 File 对象
private File [fileFieldName];
//文件类型
private String [fileFieldName]ContentType;
//文件名
private String [fileFieldName]FileName;
```

5). 使用 IO 流进行文件的上传即可. 

6). 一次传多个文件怎么办 ?

若传递多个文件, 则上述的 3 个属性, 可以改为 List 类型! 多个文件域的 name 属性值需要一致. 

7). 可以对上传的文件进行限制吗 ? 例如扩展名, 内容类型, 上传文件的大小 ? 若可以, 则若出错, 显示什么错误消息呢 ? 消息可以定制吗 ? 

可以的!

可以通过配置 FileUploadInterceptor 拦截器的参数的方式来进行限制

maximumSize (optional) - 默认的最大值为 2M. 上传的单个文件的最大值

allowedTypes (optional) - 允许的上传文件的类型. 多个使用 , 分割

allowedExtensions (optional) - 允许的上传文件的扩展名. 多个使用 , 分割.

注意: 在 org.apache.struts2 下的 default.properties 中有对上传的文件总的大小的限制. 可以使用常量的方式来修改该限制

struts.multipart.maxSize=2097152

定制错误消息. 可以在国际化资源文件中定义如下的消息:

struts.messages.error.uploading - 文件上传出错的消息

struts.messages.error.file.too.large - 文件超过最大值的消息

struts.messages.error.content.type.not.allowed - 文件内容类型不合法的消息

struts.messages.error.file.extension.not.allowed - 文件扩展名不合法的消息

问题: 此种方式定制的消息并不完善. 可以参考 org.apache.struts2 下的 struts-messages.properties, 可以提供更多的定制信息.

# 文件的下载

1). Struts2 中使用 type="stream" 的 result 进行下载即可

2). 具体使用细节参看 struts-2.3.15.3-all/struts-2.3.15.3/docs/WW/docs/stream-result.html

3). 可以为 stream 的 result 设定如下参数

```text
contentType: 结果类型
contentLength: 下载的文件的长度
contentDisposition: 设定 Content-Dispositoin 响应头. 该响应头指定接应是一个文件下载类型, 一般取值为  attachment;filename="document.pdf".

inputName: 指定文件输入流的 getter 定义的那个属性的名字. 默认为 inputStream

bufferSize: 缓存的大小. 默认为 1024
allowCaching: 是否允许使用缓存 
contentCharSet: 指定下载的字符集 
```

4). 以上参数可以在 Action 中以 getter 方法的方式提供!

# 表单的重复提交问题

1). 什么是表单的重复提交

> 在不刷新表单页面的前提下: 
  >> 多次点击提交按钮
>
  > 已经提交成功, 按 "回退" 之后, 再点击 "提交按钮".
>
  >> 在控制器响应页面的形式为转发情况下，若已经提交成功, 然后点击 "刷新(F5)"
		
> 注意:
  >> 若刷新表单页面, 再提交表单不算重复提交
>
  >> 若使用的是 redirect 的响应类型, 已经提交成功后, 再点击 "刷新", 不是表单的重复提交
		
2). 表单重复提交的危害:  			

3). Struts2 解决表单的重复提交问题:

I. 在 s:form 中添加 s:token 子标签

> 生成一个隐藏域

> 在 session 添加一个属性值

> 隐藏域的值和 session 的属性值是一致的. 
	
II. 使用 Token 或 TokenSession 拦截器. 

> 这两个拦截器均不在默认的拦截器栈中, 所以需要手工配置一下
>
> 若使用 Token 拦截器, 则需要配置一个 token.valid 的 result
>
> 若使用 TokenSession 拦截器, 则不需要配置任何其它的 result
	
III. Token VS TokenSession

> 都是解决表单重复提交问题的
>
> 使用 token 拦截器会转到 token.valid 这个 result
>
> 使用 tokenSession 拦截器则还会响应那个目标页面, 但不会执行 tokenSession 的后续拦截器. 就像什么都没发生过一样!
	
IV. 可以使用 s:actionerror 标签来显示重复提交的错误消息. 

该错误消息可以在国际化资源文件中覆盖. 该消息可以在 struts-messages.properties 文件中找到

struts.messages.invalid.token=^^The form has already been processed or no token was supplied, please try again.

# 自定义拦截器

1). 具体步骤

I. 定义一个拦截器的类

> 可以实现 Interceptor 接口
>
> 继承 AbstractInterceptor 抽象类

II. 在 struts.xml 文件配置.	

```text
<interceptors>
		
	<interceptor name="hello" class="com.atguigu.struts2.interceptors.MyInterceptor"></interceptor>
	
</interceptors>

<action name="testToken" class="com.atguigu.struts2.token.app.TokenAction">
	<interceptor-ref name="hello"></interceptor-ref>
	<interceptor-ref name="defaultStack"></interceptor-ref>
	<result>/success.jsp</result>
	<result name="invalid.token">/token-error.jsp</result>
</action>
```
	
III. 注意: 在自定义的拦截器中可以选择不调用 ActionInvocation 的 invoke() 方法. 那么后续的拦截器和 Action 方法将不会被调用.

Struts 会渲染自定义拦截器 intercept 方法返回值对应的 result


