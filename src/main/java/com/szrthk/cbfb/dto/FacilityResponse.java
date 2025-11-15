package com.szrthk.cbfb.dto;

import com.szrthk.cbfb.model.Facility;

public record FacilityResponse(
        String id,
        String name,
        String description,
        String location,
        String openTime,
        String closeTime,
        int slotDurationMinutes
) {
    public static FacilityResponse from(Facility f) {
        return new FacilityResponse(
                f.getId(),
                f.getName(),
                f.getDescription(),
                f.getLocation(),
                f.getOpenTime(),
                f.getCloseTime(),
                f.getSlotDurationMinutes()
        );
    }
}
