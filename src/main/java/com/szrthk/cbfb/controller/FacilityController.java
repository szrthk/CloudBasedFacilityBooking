package com.szrthk.cbfb.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.szrthk.cbfb.dto.FacilityResponse;
import com.szrthk.cbfb.service.FacilityService;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {
    private final FacilityService service;

    public FacilityController(FacilityService service) {
        this.service = service;
    }

    @GetMapping
    public List<FacilityResponse> getAll() {
        return service.getAllFacilities()
                .stream()
                .map(FacilityResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public FacilityResponse getById(@PathVariable String id) {
        return FacilityResponse.from(service.getFacilityById(id));
    }
}
