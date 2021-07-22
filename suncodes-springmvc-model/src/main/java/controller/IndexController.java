package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class IndexController {
    @RequestMapping("/index")
    public String f() {
        return "index";
    }

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
}
