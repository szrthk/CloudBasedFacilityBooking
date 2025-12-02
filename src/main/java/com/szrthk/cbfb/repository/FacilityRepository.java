package com.szrthk.cbfb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.szrthk.cbfb.model.Facility;

public interface FacilityRepository extends MongoRepository<Facility, String> {
    boolean existsByName(String name);
}
