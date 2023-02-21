package com.ll.hogamapp.bounded_context.pop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/pop")
@Controller
public class PopController {
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public String showIndex() {
        return "usr/pop/index";
    }
}
