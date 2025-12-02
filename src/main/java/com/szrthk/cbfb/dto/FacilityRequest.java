package com.szrthk.cbfb.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record FacilityRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String location,
        @NotEmpty List<String> slots
) {}
