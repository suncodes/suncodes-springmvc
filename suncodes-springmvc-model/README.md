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

