package com.szrthk.cbfb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.szrthk.cbfb.dto.FacilityRequest;
import com.szrthk.cbfb.model.Facility;
import com.szrthk.cbfb.repository.FacilityRepository;

@SuppressWarnings("unused")
@Service
public class FacilityService {
    private final FacilityRepository repository;

    public FacilityService(FacilityRepository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("unused")
    public Facility create(FacilityRequest request) {
        if (request == null || request.name() == null || request.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Facility name is required");
        }
        
        if (repository.existsByName(request.name().trim())) {
            throw new IllegalArgumentException("Facility name already exists");
        }
        
        Facility facility = Facility.builder()
                .name(request.name().trim())
                .description(request.description() != null ? request.description().trim() : "")
                .location(request.location() != null ? request.location().trim() : "")
                .slots(request.slots() != null ? request.slots() : List.of())
                .build();
                
        @SuppressWarnings("null")
        Facility savedFacility = repository.save(facility);
        if (savedFacility == null) {
            throw new IllegalStateException("Failed to create facility");
        }
        return savedFacility;
    }

    @SuppressWarnings("unused")
    public Facility update(String id, FacilityRequest request) {
        if (id == null || request == null) {
            throw new IllegalArgumentException("Facility ID and request are required");
        }
        
        Facility facility = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));

        if (request.description() != null) {
            facility.setDescription(request.description().trim());
        }
        if (request.location() != null) {
            facility.setLocation(request.location().trim());
        }
        if (request.slots() != null) {
            facility.setSlots(request.slots());
        }
        if (request.name() != null && !request.name().trim().isEmpty()) {
            // Check if the new name is already taken by another facility
            if (!facility.getName().equals(request.name().trim()) && 
                repository.existsByName(request.name().trim())) {
                throw new IllegalArgumentException("Facility name already exists");
            }
            facility.setName(request.name().trim());
        }
        
        @SuppressWarnings("null")
        Facility updatedFacility = repository.save(facility);
        if (updatedFacility == null) {
            throw new IllegalStateException("Failed to update facility");
        }
        return updatedFacility;
    }

    public void delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Facility ID is required");
        }
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Facility not found");
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to delete facility", e);
        }
    }

    public List<Facility> findAll() {
        return repository.findAll();
    }

    public Facility findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Facility ID is required");
        }
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));
    }
}
