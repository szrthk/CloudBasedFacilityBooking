package com.szrthk.cbfb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
        name = "facility_date_slot_idx",
        def = "{'facilityId': 1, 'date': 1, 'slot': 1}",
        unique = true
)
public class Booking {

    @Id
    private String id;

    private String facilityId;

    private String userEmail;

    // yyyy-MM-dd
    private String date;

    private String slot;

    private String qrUrl;
    private String qrSignature;
}
