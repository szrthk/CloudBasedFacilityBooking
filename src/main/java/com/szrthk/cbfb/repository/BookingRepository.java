package com.szrthk.cbfb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.szrthk.cbfb.model.Booking;

public interface BookingRepository extends MongoRepository<Booking, String> {
    boolean existsByFacilityIdAndDateAndSlot(String facilityId, String date, String slot);
    List<Booking> findByUserEmail(String userEmail);
    List<Booking> findByFacilityIdAndDate(String facilityId, String date);
}
