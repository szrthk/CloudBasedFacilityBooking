package com.szrthk.cbfb.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("bookings")
@CompoundIndex(
        name = "facility_date_start_idx",
        def = "{'facilityId': 1, 'date': 1, 'startTime': 1}",
        unique = true
)
public class Booking {

    @Id
    private String id;

    private String facilityId;

    private String userName;
    private String userEmail;

    // yyyy-MM-dd
    private String date;

    // HH:mm
    private String startTime;
}