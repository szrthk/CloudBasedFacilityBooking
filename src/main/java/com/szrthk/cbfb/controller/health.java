package com.szrthk.cbfb.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class health {
    @GetMapping({"/health", "/hc"})
    public Map<String, String> healthcheck(){
        return Map.of("status", "ok");
    }
}
