package com.szrthk.cbfb.util;

import java.util.List;

public final class SlotConstants {
    private SlotConstants() {}

    // Default hourly slots; adjust as needed
    public static final List<String> SLOTS = List.of(
            "09:00-10:00",
            "10:00-11:00",
            "11:00-12:00",
            "12:00-13:00",
            "13:00-14:00",
            "14:00-15:00",
            "15:00-16:00",
            "16:00-17:00"
    );
}
