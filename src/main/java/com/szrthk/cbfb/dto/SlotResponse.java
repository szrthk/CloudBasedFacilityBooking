package com.szrthk.cbfb.dto;

public record SlotResponse(
        String startTime,
        String endTime,
        boolean booked
) {}
