package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @RequestMapping("/testExceptionHandle")
    public String testExceptionHandle(@RequestParam("i") Integer i) {
        System.out.println(10 / i);
        return "success";
    }

    @ExceptionHandler({ArithmeticException.class})
    public String testArithmeticException(Exception e) {
        System.out.println("打印错误信息 ===> ArithmeticException:" + e);
        // 跳转到指定页面
        return "error";
    }
}
