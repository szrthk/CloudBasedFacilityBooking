package com.szrthk.cbfb.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateFacilityRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String location,
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "openTime must be HH:mm") String openTime,
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "closeTime must be HH:mm") String closeTime,
        @Min(15) @Max(180) Integer slotDurationMinutes
) {}
