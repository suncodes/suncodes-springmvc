package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pojo.LifeBO;

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

//    @ModelAttribute("name")
//    public String f2(@RequestParam(required = false) String name) {
//        System.out.println("name: " + name);
//        return name;
//    }

    @RequestMapping("/index1")
    public String f() {
        return "index";
    }

    @RequestMapping("/index2")
    public String register(@ModelAttribute("user") @RequestParam(required = false) LifeBO lifeBO) {
        return "index";
    }

    @RequestMapping(value = "/index3")
    @ModelAttribute("name")
    public String model(@RequestParam(required = false) String name) {
        System.out.println("name: " + name);
        // /model/WEB-INF/jsp/index3.jsp
        return name;
    }

}
