package com.szrthk.cbfb.repositery;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.szrthk.cbfb.model.Facility;

public interface FacilityRepository extends MongoRepository<Facility, String> {

    Optional<Facility> findByName(String name);

    boolean existsByName(String name);
}
