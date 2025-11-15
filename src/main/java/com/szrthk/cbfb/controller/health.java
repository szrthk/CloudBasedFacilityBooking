package com.szrthk.cbfb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class health {
    @GetMapping("/hc")
    public String healthcheck(){
        return "System is working fine";
    }
}
