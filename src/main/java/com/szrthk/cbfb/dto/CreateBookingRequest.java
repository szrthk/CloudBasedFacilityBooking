package com.szrthk.cbfb.dto;

import jakarta.validation.constraints.*;

public record CreateBookingRequest(
        @NotBlank String facilityId,
        @NotBlank String userName,
        @Email String userEmail,
        // yyyy-MM-dd
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                 message = "date must be dd-MM-yyyy")
        String date,
        // HH:mm
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$",
                 message = "startTime must be HH:mm")
        String startTime
) {}
