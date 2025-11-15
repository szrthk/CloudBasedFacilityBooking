package com.szrthk.cbfb.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.szrthk.cbfb.dto.CreateFacilityRequest;
import com.szrthk.cbfb.dto.FacilityResponse;
import com.szrthk.cbfb.dto.UpdateFacilityRequest;
import com.szrthk.cbfb.service.FacilityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/facilities")
public class AdminFacilityController {
    private final FacilityService service;

    public AdminFacilityController(FacilityService service) {
        this.service = service;
    }

    @PostMapping
    public FacilityResponse create(@Valid @RequestBody CreateFacilityRequest request) {
        return FacilityResponse.from(service.createFacility(request));
    }

    @PutMapping("/{id}")
    public FacilityResponse update(@PathVariable String id,
                                   @RequestBody UpdateFacilityRequest request) {
        return FacilityResponse.from(service.updateFacility(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteFacility(id);
    }

    @GetMapping
    public List<FacilityResponse> getAll() {
        return service.getAllFacilities().stream()
                .map(FacilityResponse::from)
                .toList();
    }
}
