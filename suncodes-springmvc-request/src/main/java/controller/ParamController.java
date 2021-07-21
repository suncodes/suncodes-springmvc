package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ParamController {

    @RequestMapping(value = "/params", params = "type")
    public ModelAndView f() {
        return new ModelAndView("/params");
    }

    @RequestMapping(value = "/params1", params = "type=1")
    public ModelAndView f1() {
        return new ModelAndView("/params");
    }

    @RequestMapping(value = "/params2", consumes = "application/json")
    public ModelAndView f2() {
        return new ModelAndView("/params");
    }

    @RequestMapping(value = "/params3", consumes = "application/x-www-form-urlencoded")
    public ModelAndView f3() {
        return new ModelAndView("/params");
    }
}
