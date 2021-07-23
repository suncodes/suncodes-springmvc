package handler;

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