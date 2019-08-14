package com.actimel.celibrary.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "index";
    }
 
    @GetMapping("/")
    public String root() {
        return "index";
    }
 

}
