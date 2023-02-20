package com.ll.hogamapp.bounded_context.home.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String showMain() {
        return "usr/home/main";
    }

    @GetMapping("/home/test")
    @PreAuthorize("isAuthenticated()")
    public String showTest() {
        return "usr/home/main";
    }
}
