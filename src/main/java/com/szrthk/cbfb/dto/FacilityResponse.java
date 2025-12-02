package com.szrthk.cbfb.dto;

import com.szrthk.cbfb.model.Facility;

public record FacilityResponse(
        String id,
        String name,
        String description,
        String location,
        java.util.List<String> slots
) {
    public static FacilityResponse from(Facility f) {
        return new FacilityResponse(
                f.getId(),
                f.getName(),
                f.getDescription(),
                f.getLocation(),
                f.getSlots()
        );
    }
}
