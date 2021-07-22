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




## ModelAndView



## Map 及 Model



## @SessionAttributes


