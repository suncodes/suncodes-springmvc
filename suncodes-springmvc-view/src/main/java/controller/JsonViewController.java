package controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import pojo.JsonBO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonViewController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        /*
         * {
         *   "jsonBO" : {
         *     "address" : "郑州",
         *     "weather" : "暴雨"
         *   }
         * }
         */
        JsonBO jsonBO = new JsonBO();
        jsonBO.setAddress("郑州");
        jsonBO.setWeather("暴雨");
        modelAndView.addObject(jsonBO);
        return modelAndView;
    }
}
