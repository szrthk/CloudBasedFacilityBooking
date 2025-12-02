package com.szrthk.cbfb.dto;

import java.util.List;

public record AvailabilityResponse(
        List<String> availableSlots,
        List<String> bookedSlots
) {}
