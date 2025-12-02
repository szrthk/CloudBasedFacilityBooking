package com.szrthk.cbfb.dto;

public record VerifyResponse(
        boolean valid,
        String message
) {}
