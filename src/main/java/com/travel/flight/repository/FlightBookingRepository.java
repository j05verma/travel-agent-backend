package com.travel.flight.repository;

import com.travel.flight.model.FlightBooking;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface FlightBookingRepository extends MongoRepository<FlightBooking, String> {
    List<FlightBooking> findByCustomerNameIgnoreCase(String customerName);

}