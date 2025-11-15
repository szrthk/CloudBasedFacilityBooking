package com.szrthk.cbfb.dto;

public record UpdateFacilityRequest(
        String description,
        String location,
        String openTime,
        String closeTime,
        Integer slotDurationMinutes
) {}
