package com.travel.flight.service;

import com.travel.flight.dto.FlightRequest;
import com.travel.flight.model.Flight;
import com.travel.flight.model.FlightBooking;

import java.util.List;

public interface FlightService {

    Flight createFlight(FlightRequest request);
    List<Flight> getAllFlights();
    List<Flight> search(String source, String destination);
    Flight findByFlightNumber(String flightNumber,  String departureTime);
    FlightBooking book(String flightNumber, String departureTime, String customerName, int seats);
    FlightBooking cancel(String bookingId);
    List<FlightBooking> getBookingsByCustomer(String customerName);
    Flight deleteFlight(String flightNumber, String departureTime); 
    // public
    List<Flight> getAllFlight();
}