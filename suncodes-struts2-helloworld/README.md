# 工程搭建

首先说明，struts2 工程有两部分，（1）web部分，（2）strut2部分（配置文件）

web 部分和之前的 web 项目是一致的。

struts2 部分，主要就是配置文件放在那儿。

如果是传统的struts2，则需要放在 src/struts.xml，编译之后的目录是classes/struts.xml

如果是maven，则可以放在 resources/struts.xml，只要保证编译之后的目录是classes/struts.xml即可。

- 创建 maven 项目
- 配置 pom 文件
  - 添加 servlet api 依赖
  - 添加 struts2 依赖
  - 打包方式为 war
- 新建 webapp 文件夹
- 在 resources 文件夹新建 struts.xml 文件

# 工程分析

主要还是 struts.xml 文件的配置。

struts.xml 文件的作用：

- URL 和 class 映射（控制器）
- 结果返回（视图）

具体配置详见 struts.xml 

说明下几点内容：

package：

- package: 包. struts2 使用 package 来组织模块. 
- name 属性: 必须. 用于其它的包应用当前包. （用于继承）
- extends: 当前包继承哪个包, 继承的, 即可以继承其中的所有的配置. 通常情况下继承 struts-default，struts-default 这个包在 struts-default.xml 文件中定义.
- namespace 可选, 如果它没有给出, 则以 / 为默认值. 
- 若 namespace 有一个非默认值, 则要想调用这个包里的Action, 就必须把这个属性所定义的命名空间添加到有关的 URI 字符串里

action：

- 配置一个 action: 一个 struts2 的请求就是一个 action 
- name: 对应一个 struts2 的请求的名字(或对一个 servletPath, 但去除 / 和扩展名), 不包含扩展名
- class 的默认值为: com.opensymphony.xwork2.ActionSupport
- method 的默认值为: execute
- result: 结果. 
  - result: 结果. 表示 action 方法执行后可能返回的一个结果. 所以一个 action 节点可能会有多个 result 子节点.
  - 多个 result 子节点使用 name 来区分
  - name: 标识一个 result. 和 action 方法的返回值对应. 默认值为 success
  - type: 表示结果的类型. 默认值为 dispatcher(转发到结果.)


