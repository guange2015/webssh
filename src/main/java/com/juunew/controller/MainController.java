package com.juunew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @RequestMapping(value="/",method= RequestMethod.GET)
    public ModelAndView index() {
        //index就是视图的名称（index.ftl）
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }

}
