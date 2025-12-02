package com.szrthk.cbfb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BookingRequest(
        @NotBlank String facilityId,
        @Email @NotBlank String userEmail,
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "date must be yyyy-MM-dd")
        String date,
        @NotBlank String slot
) {}
