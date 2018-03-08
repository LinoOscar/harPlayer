package com.harPlayer.core.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class MainController {
	
	private static final Logger logger = Logger.getLogger(MainController.class);

    @RequestMapping("/test")
    public String test() {
        return "test :: test";
    }
 
    /*ERRORS*/
    @RequestMapping("/*")
    public String error() {
        return "404";
    }
    
    /*UTILS*/
    @RequestMapping("properties")
    @ResponseBody
    java.util.Properties properties() {
        return System.getProperties();
    }
}
