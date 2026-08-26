package com.travel.hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.travel.hotel.model.Hotel;

public interface HotelRepository extends MongoRepository<Hotel, String> {
    Optional<Hotel> findByNameIgnoreCaseAndCityIgnoreCaseAndDeletedFalse(String name, String city);

    List<Hotel> findByCityIgnoreCaseAndDeletedFalse(String city);

    List<Hotel> findByDeletedFalse();

}
