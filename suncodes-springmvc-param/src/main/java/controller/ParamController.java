package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pojo.LifeBO;

@Controller
public class ParamController {

    @RequestMapping(value = "/url", method = RequestMethod.GET)
    public String f(LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }

    @RequestMapping(value = "/url1", method = RequestMethod.GET)
    public String f1(String city, String person, String target) {
        System.out.println("city  : " + city);
        System.out.println("person: " + person);
        System.out.println("target: " + target);
        return "param";
    }

    @RequestMapping(value = "/url2/{city}/{person}/{target}", method = RequestMethod.GET)
    public String f2(@PathVariable String city, @PathVariable String person, @PathVariable String target) {
        System.out.println("city  : " + city);
        System.out.println("person: " + person);
        System.out.println("target: " + target);
        return "param";
    }

    @RequestMapping(value = "/url3", method = RequestMethod.GET)
    public String f3(@ModelAttribute("user") LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }

    // =======================================================================
    // =======================================================================

    @RequestMapping(value = "/urlencoded", method = RequestMethod.POST)
    public String u(LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }

    @RequestMapping(value = "/urlencoded1", method = RequestMethod.POST)
    public String u1(String city, String person, String target) {
        System.out.println("city  : " + city);
        System.out.println("person: " + person);
        System.out.println("target: " + target);
        return "param";
    }

    @RequestMapping(value = "/urlencoded2", method = RequestMethod.POST)
    public String u2(@ModelAttribute("user") LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }

    // =======================================================================
    // =======================================================================

    @RequestMapping(value = "/form-data", method = RequestMethod.POST, consumes = "multipart/form-data")
    public String d(LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }

    @RequestMapping(value = "/form-data1", method = RequestMethod.POST)
    public String d1(String city, String person, String target) {
        System.out.println("city  : " + city);
        System.out.println("person: " + person);
        System.out.println("target: " + target);
        return "param";
    }

    @RequestMapping(value = "/form-data2", method = RequestMethod.POST)
    public String d2(@RequestParam("city") String city,
                     @RequestParam("person")String person,
                     @RequestParam("target")String target) {
        System.out.println("city  : " + city);
        System.out.println("person: " + person);
        System.out.println("target: " + target);
        return "param";
    }

    @RequestMapping(value = "/form-data3", method = RequestMethod.POST)
    public String d3(@ModelAttribute("user") LifeBO lifeBO) {
        System.out.println(lifeBO);
        return "param";
    }

}
