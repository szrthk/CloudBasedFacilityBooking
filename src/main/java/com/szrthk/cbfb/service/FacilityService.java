package com.szrthk.cbfb.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.szrthk.cbfb.dto.CreateFacilityRequest;
import com.szrthk.cbfb.dto.UpdateFacilityRequest;
import com.szrthk.cbfb.model.Facility;
import com.szrthk.cbfb.repositery.FacilityRepository;

@Service
public class FacilityService {
    private final FacilityRepository repository;

    public FacilityService(FacilityRepository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("null")
    public Facility createFacility(CreateFacilityRequest req) {
        if (repository.existsByName(req.name())) {
            throw new IllegalArgumentException("Facility name already exists");
        }

        int slotDuration = (req.slotDurationMinutes() == null) ? 60 : req.slotDurationMinutes();

        Facility facility = Facility.builder()
                .name(req.name().trim())
                .description(req.description().trim())
                .location(req.location().trim())
                .openTime(req.openTime())
                .closeTime(req.closeTime())
                .slotDurationMinutes(slotDuration)
                .build();

        return repository.save(facility);
    }

    @SuppressWarnings("null")
    @NonNull
    public Facility updateFacility(String id, UpdateFacilityRequest req) {
        Facility facility = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));

        if (StringUtils.hasText(req.description())) {
            facility.setDescription(req.description().trim());
        }
        if (StringUtils.hasText(req.location())) {
            facility.setLocation(req.location().trim());
        }
        if (StringUtils.hasText(req.openTime())) {
            facility.setOpenTime(req.openTime());
        }
        if (StringUtils.hasText(req.closeTime())) {
            facility.setCloseTime(req.closeTime());
        }
        if (req.slotDurationMinutes() != null) {
            facility.setSlotDurationMinutes(req.slotDurationMinutes());
        }

        return repository.save(facility);
    }

    @SuppressWarnings("null")
    public void deleteFacility(String id) {
        repository.deleteById(id);
    }

    public List<Facility> getAllFacilities() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Facility getFacilityById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));
    }
}
