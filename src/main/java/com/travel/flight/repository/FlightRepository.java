package com.travel.flight.repository;

import com.travel.flight.model.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends MongoRepository<Flight, String> {

    Optional<Flight> findByFlightNumberIgnoreCaseAndDepartureTimeAndDeletedFalse(String flightNumber, String departureTime);

    Optional<Flight> findByFlightNumberIgnoreCaseAndDeletedFalseAndDepartureTime(String flightNumber, String departureTime);

    List<Flight> findBySourceIgnoreCaseAndDestinationIgnoreCaseAndDeletedFalse(String source, String destination);

    List<Flight> findByDeletedFalse();

    Optional<Flight> findByFlightNumberIgnoreCase(String flightNumber);
}