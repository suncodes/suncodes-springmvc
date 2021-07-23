## @ModelAttribute注解

在 Spring MVC 中非常重要的注解 @ModelAttribute，用来将请求参数绑定到 Model 对象。

在 Controller 中使用 @ModelAttribute 时，有以下几种应用情况。

- 应用在方法上
- 应用在方法的参数上
- 应用在方法上，并且方法也使用了 @RequestMapping


需要注意的是，因为模型对象要先于 controller 方法之前创建，所以被 @ModelAttribute 注解的方法会在 Controller 每个方法执行之前都执行。因此一个 Controller 映射多个 URL 时，要谨慎使用。 

### 1. 应用在方法上
下面从应用在有无返回值的方法上两个方面进行讲解。

#### 1）应用在无返回值的方法

有关 ModelAttributeController 代码：
```java
@Controller
public class ModelAttributeController {

    @ModelAttribute
    public void f(@RequestParam(required = false) String name, Model model) {
        model.addAttribute("name", name);
    }
    @RequestMapping("/index1")
    public String f() {
        return "index";
    }
}
```

有关 IndexController 代码：
```java
@Controller
public class IndexController {
    @RequestMapping("/index")
    public String f() {
        return "index";
    }
}
```

有关 index.jsp 代码：
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Param</title>
</head>
<body>
lalala
${name}
</body>
</html>

```

请求：http://localhost:8080/model/index?name=111

结果：lalala

请求：http://localhost:8080/model/index1?name=111

结果：lalala 111

说明：对于使用了 ModelAttribute 注解的 Controller，会对同一个 Controller 中的其他请求进行拦截，执行 ModelAttribute 注解的方法。

也就是说 ModelAttribute 注解 只对本 Controller 类进行生效。且每次请求都会执行。

#### 2）应用在有返回值的方法

修改 ModelAttributeController 控制类，代码如下。
```java
@Controller
public class ModelAttributeController {

    @ModelAttribute
    public void f(@RequestParam(required = false) String name, Model model) {
        model.addAttribute("name", name);
    }

    @ModelAttribute
    public String f1(@RequestParam(required = false) String name) {
        name = "222";
        return name;
    }

    @RequestMapping("/index1")
    public String f() {
        return "index";
    }
}
```

修改 index.jsp，代码如下。 
```xml
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Param</title>
</head>
<body>
lalala <br/>
name: ${name} <br/>
string: ${string}
</body>
</html>
```

输出：
```text
lalala
name: 111
string: 222
```

对于以上情况，返回值对象 name 会被默认放到隐含的 Model 中，在 Model 中 key 为返回值首字母小写，value 为返回的值。等同于 model.addAttribute("string", name);。

但正常情况下，程序中尽量不要出现 key 为 string、int、float 等这样数据类型的返回值。使用 @ModelAttribute 注解 value 属性可以自定义 key，代码如下。

    // 方法有返回值
    @ModelAttribute("name")
    public String myModel(@RequestParam(required = false) String name) {
        return name;
    }

等同于

    model.addAttribute("name", name);


### 2. 应用在方法的参数上

@ModelAttribute 注解在方法的参数上，调用方法时，模型的值会被注入。这在实际使用时非常简单，常用于将表单属性映射到模型对象。

    @RequestMapping("/register")
    public String register(@ModelAttribute("user") UserForm user) {
        if ("zhangsan".equals(uname) && "123456".equals(upass)) {
            logger.info("成功");
            return "login";
        } else {
            logger.info("失败");
            return "register";
    }

上述代码中“@ModelAttribute("user") UserForm user”语句的功能有两个：

    将请求参数的输入封装到 user 对象中
    创建 UserForm 实例


以“user”为键值存储在 Model 对象中，和“model.addAttribute("user",user)”语句的功能一样。如果没有指定键值，即“@ModelAttribute UserForm user”，那么在创建 UserForm 实例时以“userForm”为键值存储在 Model 对象中，和“model.addAtttribute("userForm", user)”语句的功能一样。

### 3. ModelAttribute+RequestMapping

    @RequestMapping(value = "/index3")
    @ModelAttribute("name")
    public String model(@RequestParam(required = false) String name) {
        System.out.println("name: " + name);
        // /model/WEB-INF/jsp/index3.jsp
        return name;
    }

访问地址：http://localhost:8080/model/index3?name=111

结果 404 ，说是 /model/WEB-INF/jsp/index3.jsp 找不到对应资源。

重要：

@ModelAttribute 和 @RequestMapping 注解同时应用在方法上时，有以下作用：

    方法的返回值会存入到 Model 对象中，key 为 ModelAttribute 的 value 属性值。
    方法的返回值不再是方法的访问路径，访问路径会变为 @RequestMapping 的 value 值，例如：@RequestMapping(value = "/index3") 跳转的页面是 index3.jsp 页面。

也就是说，RequestMapping 中的地址，需要和 JSP 资源文件的名称一致，不一致，则会找不到对应的文件。

## ModelAndView

ModelAndView：包含 model 和 view 两部分，使用时需要自己实例化，利用 ModelMap 来传值，也可以设置 view 的名称。

控制器处理方法的返回值如果为 ModelAndView, 则其既包含视图信息，也包含模型数据信息。

添加模型数据:
    
    MoelAndView addObject(String attributeName, Object attributeValue)
    ModelAndView addAllObject(Map<String, ?> modelMap)

设置视图:

    void setView(View view)
    void setViewName(String viewName)


## Map 及 Model

Model：每次请求中都存在的默认参数，利用其 addAttribute() 方法即可将服务器的值传递到客户端页面中。

Spring MVC 在调用方法前会创建一个隐含的模型对象作为模型数据的存储容器。

如果方法的入参为 Map 或 Model 类型，Spring MVC 会将隐含模型的引用传递给这些入参。在方法体内，开发者可以通过这个入参对象访问到模型中的所有数据，也可以向模型中添加新的属性数据

org.springframework.ui.ModelMap

```java
    @RequestMapping("/model")
    public String f1(@RequestParam(required = false) String name,
                     Model model,
                     Map<String, String> map,
                     Map<String, String> map2) {

        map.put("model2", name);
        model.addAttribute("model1", name);
        map2.put("model3", name);
        return "index";
    }
```

结果：

    lalala
    name:
    string:
    user:
    model1: 111
    model2: 111
    model3: 111

如果入参是 Model 或 Map，则会把对应的属性，放入 model 中，不论参数列表在什么位置，有多少。


## @SessionAttributes

- 若希望在多个请求之间共用数据，则可以在控制器类上标注一个 @SessionAttributes,配置需要在session中存放的数据范围，Spring MVC将存放在model中对应的数据暂存到 HttpSession 中。
- @SessionAttributes只能使用在类定义上。
- @SessionAttributes 除了可以通过属性名指定需要放到会 话中的属性外，还可以通过模型属性的对象类型指定哪些模型属性需要放到会话中 例如：
  - @SessionAttributes(types=User.class)会将model中所有类型为 User的属性添加到会话中。
  - @SessionAttributes(value={“user1”, “user2”}) 会将model中属性名为user1和user2的属性添加到会话中。
  - @SessionAttributes(types={User.class, Dept.class}) 会将model中所有类型为 User和Dept的属性添加到会话中。
  - @SessionAttributes(value={“user1”,“user2”},types={Dept.class})会将model中属性名为user1和user2以及类型为Dept的属性添加到会话中。
- value和type之间是并集关系

举个例子说明一下

处理器
```java
@SessionAttributes(value={"user"})
@Controller
public class UserController {

    @RequestMapping("/testSessionAttributes")
    public String testSessionAttributes(Model model){
        User user = new User("jack","123456");
        model.addAttribute("user", user);
        return "success";
    }
}
```

处理方法testSessionAttributes在model中存放了属性名为user的数据，处理结束后,model里的数据会被放入到request中,页面通过request域可以获取到。

而这里使用了@SessionAttributes(value={“user”})将model中属性名为user的数据copy一份进了session域中.


## @SessionAttributes 和 @SessionAttribute的区别

@SessionAttributes 和 @SessionAttribute的区别

Spring MVC中有两个长得非常像的注解：@SessionAttributes 和 @SessionAttribute。

我们先看下@SessionAttributes的定义：

@SessionAttributes用于在请求之间的HTTP Servlet会话中存储model属性。 它是类型级别的注解，用于声明特定控制器使用的会话属性。 这通常列出应透明地存储在会话中以供后续访问请求的模型属性的名称或模型属性的类型。

举个例子：
```java
@SessionAttributes("user")
public class LoginController {

	@ModelAttribute("user")
	public User setUpUserForm() {
		return new User();
	}
}
```

我们可以看到@SessionAttributes是类注解，他用来在session中存储model。 如上面的例子，我们定义了一个名为“User”的model并把它存储在Session中。

我们再看一下@SessionAttribute的定义：

如果您需要访问全局存在（例如，在控制器外部（例如，通过过滤器）管理）并且可能存在或可能不存在的预先存在的会话属性，则可以在方法参数上使用@SessionAttribute注释，例如 以下示例显示：

```java
@Controller
@RequestMapping("/user")
public class UserController {

   /*
    * Get user from session attribute
    */
   @GetMapping("/info")
   public String userInfo(@SessionAttribute("user") User user) {

      System.out.println("Email: " + user.getEmail());
      System.out.println("First Name: " + user.getFname());

      return "user";
   }
}
```

@SessionAttribute只是获取存储在session中的属性。如果要设置（添加删除）session的属性，则要考虑将org.springframework.web.context.request.WebRequest或javax.servlet.http.HttpSession注入到控制器方法中。

@SessionAttributes中绑定的model可以通过如下几个途径获取：

- 在视图中通过request.getAttribute或session.getAttribute获取

- 在后面请求返回的视图中通过session.getAttribute或者从model中获取

- 自动将参数设置到后面请求所对应处理器的Model类型参数或者有@ModelAttribute注释的参数里面。

@SessionAttributes用户后可以调用SessionStatus.setComplete来清除，这个方法只是清除SessionAttribute里的参数，而不会应用Session中的参数。

```java
@Controller
@SessionAttributes("pet") 
public class EditPetForm {

    // ...

    @PostMapping("/pets/{id}")
    public String handle(Pet pet, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors) {
            // ...
        }
            status.setComplete(); 
            // ...
        }
    }
}
```
总结一下：

@SessionAttributes 是将model设置到session中去。

@SessionAttribute 是从session获取之前设置到session中的数据。


## Spring MVC类型转换器（Converter）

写在前面的话：

类型转换器是为了在`请求`的时候，把一种类型，映射为另外一种类型。

Spring MVC 框架的 Converter<S，T> 是一个可以将一种数据类型转换成另一种数据类型的接口，这里 S 表示源类型，T 表示目标类型。开发者在实际应用中使用框架内置的类型转换器基本上就够了，但有时需要编写具有特定功能的类型转换器。

例如，用户输入的日期可能有许多种形式，如“December 25,2014”“12/25/2014”和“2014-12-25”，这些都表示同一个日期。默认情况下，Spring 会期待用户输入的日期样式与当前语言区域的日期样式相同。例如，对于美国的用户而言，就是月/日/年的格式。如果希望 Spring 在将输入的日期字符串绑定到 LocalDate 时，使用不同的日期样式，则需要编写一个 Converter，才能将字符串转换成日期。

java.time.LocalDate 类是 Java 8 的一个新类型，用来替代 java.util.Date。还需使用新的 Date/Time API 来替换旧有的 Date 和 Calendar 类。

### 内置的类型转换器

在 Spring MVC 框架中，对于常用的数据类型，开发者无须创建自己的类型转换器，因为 Spring MVC 框架有许多内置的类型转换器用于完成常用的类型转换。Spring MVC 框架提供的内置类型转换包括以下几种类型。

#### 1）标量转换器

| 名称                           | 作用                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| StringToBooleanConverter       | String 到 boolean 类型转换                                   |
| ObjectToStringConverter        | Object 到 String 转换，调用 toString 方法转换                |
| StringToNumberConverterFactory | String 到数字转换（例如 Integer、Long 等）                   |
| NumberToNumberConverterFactory | 数字子类型（基本类型）到数字类型（包装类型）转换             |
| StringToCharacterConverter     | String 到 Character 转换，取字符串中的第一个字符             |
| NumberToCharacterConverter     | 数字子类型到 Character 转换                                  |
| CharacterToNumberFactory       | Character 到数字子类型转换                                   |
| StringToEnumConverterFactory   | String 到枚举类型转换，通过 Enum.valueOf 将字符串转换为需要的枚举类型 |
| EnumToStringConverter          | 枚举类型到 String 转换，返回枚举对象的 name 值               |
| StringToLocaleConverter        | String 到 java.util.Locale 转换                              |
| PropertiesToStringConverter    | java.util.Properties 到 String 转换，默认通过 ISO-8859-1 解码 |
| StringToPropertiesConverter    | String 到 java.util.Properties 转换，默认使用 ISO-8859-1 编码 |

#### 2）集合、数组相关转换器

| 名称                            | 作用                                                         |
| ------------------------------- | ------------------------------------------------------------ |
| ArrayToCollectionConverter      | 任意数组到任意集合（List、Set）转换                          |
| CollectionToArrayConverter      | 任意集合到任意数组转换                                       |
| ArrayToArrayConverter           | 任意数组到任意数组转换                                       |
| CollectionToCollectionConverter | 集合之间的类型转换                                           |
| MapToMapConverter               | Map之间的类型转换                                            |
| ArrayToStringConverter          | 任意数组到 String 转换                                       |
| StringToArrayConverter          | 字符串到数组的转换，默认通过“，”分割，且去除字符串两边的空格（trim） |
| ArrayToObjectConverter          | 任意数组到 Object 的转换，如果目标类型和源类型兼容，直接返回源对象；否则返回数组的第一个元素并进行类型转换 |
| ObjectToArrayConverter          | Object 到单元素数组转换                                      |
| CollectionToStringConverter     | 任意集合（List、Set）到 String 转换                          |
| StringToCollectionConverter     | String 到集合（List、Set）转换，默认通过“，”分割，且去除字符串两边的空格（trim） |
| CollectionToObjectConverter     | 任意集合到任意 Object 的转换，如果目标类型和源类型兼容，直接返回源对象；否则返回集合的第一个元素并进行类型转换 |
| ObjectToCollectionConverter     | Object 到单元素集合的类型转换                                |

类型转换是在视图与控制器相互传递数据时发生的。Spring MVC 框架对于基本类型（例如 int、long、float、double、boolean 以及 char 等）已经做好了基本类型转换。

    注意：在使用内置类型转换器时，请求参数输入值与接收参数类型要兼容，否则会报 400 错误。请求参数类型与接收参数类型不兼容问题需要学习输入校验后才可解决。

### 自定义类型转换器

当 Spring MVC 框架内置的类型转换器不能满足需求时，开发者可以开发自己的类型转换器。

#### 1）自定义类型转换器

```java
package converter;

import org.springframework.core.convert.converter.Converter;
import pojo.LifeBO;

public class LifeConverter implements Converter<String, LifeBO> {
    public LifeBO convert(String s) {

        LifeBO lifeBO = new LifeBO();
        // 以“，”分隔
        String stringvalues[] = s.split(",");
        if (stringvalues.length == 3) {
            // 为user实例赋值
            lifeBO.setCity(stringvalues[0]);
            lifeBO.setPerson(stringvalues[1]);
            lifeBO.setTarget(stringvalues[2]);
            return lifeBO;
        } else {
            throw new IllegalArgumentException(String.format("类型转换失败，但格式是[% s ] ", s));
        }
    }
}

```

#### 2）注册类型转换器

```xml
    <!--注册类型转换器UserConverter -->
    <mvc:annotation-driven conversion-service="conversionService" />
    <bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
        <property name="converters">
            <list>
                <bean class="converter.LifeConverter" />
            </list>
        </property>
    </bean>
```

#### 3）定义 Controller

```java
@Controller
public class ConverterController {

    @RequestMapping("/converter")
    public void f(@RequestParam("goods") LifeBO lifeBO) {
        System.out.println(lifeBO);
    }
}

```

#### 4）发出请求

```jsp
<form method="post" action="${pageContext.request.contextPath }/converter">
    输入：<input type="text" name="goods">
</form>
```

特别有一点要注意：

请求中的参数名 goods 和 接收的参数名 goods 要一致，需要使用 @RequestParam 注解。














