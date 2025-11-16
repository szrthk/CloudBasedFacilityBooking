package com.szrthk.cbfb.dto;

import com.szrthk.cbfb.model.Booking;

public record BookingResponse(
        String id,
        String facilityId,
        String userName,
        String userEmail,
        String date,
        String startTime
) {
    public static BookingResponse from(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getFacilityId(),
                b.getUserName(),
                b.getUserEmail(),
                b.getDate(),
                b.getStartTime()
        );
    }   
}
