package com.travel.flight.repository;

import com.travel.flight.model.BookingStatus;
import com.travel.flight.model.FlightBooking;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface FlightBookingRepository extends MongoRepository<FlightBooking, String> {
    List<FlightBooking> findByPassengersPassengerNameIgnoreCase(
            String passengerName);

    List<FlightBooking> findByFlightNumberAndDepartureTimeAndStatus(
            String flightNumber,
            String departureTime,
            BookingStatus status);

}