package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
