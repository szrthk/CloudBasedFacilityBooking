package com.szrthk.cbfb.dto;

import java.util.List;

public record AvailabilityResponse(
        String facilityId,
        String date,
        int slotDurationMinutes,
        List<SlotResponse> slots
) {}
