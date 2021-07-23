package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pojo.LifeBO;

@Controller
public class JsonController {

    @RequestMapping("/hhh")
    public String f(@RequestBody LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "index";
    }
}
