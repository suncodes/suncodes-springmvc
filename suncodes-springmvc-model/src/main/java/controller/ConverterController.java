package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pojo.LifeBO;

@Controller
public class ConverterController {

    @RequestMapping("/converter")
    public void f(@RequestParam("goods") LifeBO lifeBO) {
        System.out.println(lifeBO);
    }
}
