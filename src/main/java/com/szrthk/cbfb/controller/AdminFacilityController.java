package com.szrthk.cbfb.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.szrthk.cbfb.dto.FacilityResponse;
import com.szrthk.cbfb.dto.FacilityRequest;
import com.szrthk.cbfb.service.FacilityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/facilities")
public class AdminFacilityController {
    private final FacilityService service;

    public AdminFacilityController(FacilityService service) {
        this.service = service;
    }

    @PostMapping
    public FacilityResponse create(@Valid @RequestBody FacilityRequest request) {
        return FacilityResponse.from(service.create(request));
    }

    @PutMapping("/{id}")
    public FacilityResponse update(@PathVariable String id,
                                   @RequestBody FacilityRequest request) {
        return FacilityResponse.from(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
