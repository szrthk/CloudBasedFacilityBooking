package com.szrthk.cbfb.repositery;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.szrthk.cbfb.model.Booking;

public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findByFacilityIdAndDate(String facilityId, String date);

    List<Booking> findByUserEmail(String userEmail);
}
