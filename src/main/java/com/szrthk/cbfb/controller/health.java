package com.szrthk.cbfb.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class health {
    @GetMapping("/hc")
    public Map<String, String> healthcheck(){
        return Map.of("status", "System is working fine");
    }
}
