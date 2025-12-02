package com.szrthk.cbfb.dto;

import com.szrthk.cbfb.model.Booking;

public record BookingResponse(
        String id,
        String facilityId,
        String userEmail,
        String date,
        String slot,
        String qrUrl
) {
    public static BookingResponse from(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getFacilityId(),
                b.getUserEmail(),
                b.getDate(),
                b.getSlot(),
                b.getQrUrl()
        );
    }   
}
