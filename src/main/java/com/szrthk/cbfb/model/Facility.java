package com.szrthk.cbfb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("facilities")
public class Facility {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;
    private String location;

    // time in "HH:mm" 24h format like "09:00"
    private String openTime;
    private String closeTime;

    // e.g. 60 minutes per slot
    private int slotDurationMinutes;
}