package com.travel.flight.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travel.common.dto.ApiResponse;
import com.travel.flight.model.Flight;
import com.travel.flight.model.FlightBooking;
import com.travel.flight.service.FlightService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public/api/flights")
@RequiredArgsConstructor
public class PublicFlightController {
    private final FlightService flightService;

    @GetMapping("/search")
    public ApiResponse<List<Flight>> search(
            @RequestParam String source,
            @RequestParam String destination) {
        return ApiResponse.ok(flightService.search(source, destination), "Flights fetched successfully");
    }

    

    @GetMapping("/{flightNumber}")
    public ApiResponse<Flight> getFlightDetails(
            @PathVariable String flightNumber,
            @RequestParam String departureTime) {
        return ApiResponse.ok(flightService.findByFlightNumber(flightNumber, departureTime), "Flight details fetched successfully");
    }

    @PostMapping("/book")
    public ApiResponse<FlightBooking> book(
            @RequestParam String flightNumber,
            @RequestParam String departureTime,
            @RequestParam String customerName,
            @RequestParam int seats) {
        return ApiResponse.ok(flightService.book(flightNumber, departureTime, customerName, seats), "Flight booked successfully");
    }

    @PostMapping("/cancel/{bookingId}")
    public ApiResponse<FlightBooking> cancel(@PathVariable String bookingId) {
        return ApiResponse.ok(flightService.cancel(bookingId), "Booking cancelled successfully");
    }

    @GetMapping("/bookings/{customerName}")
    public ApiResponse<List<FlightBooking>> getBookingsByCustomer(@PathVariable String customerName) {
        return ApiResponse.ok(flightService.getBookingsByCustomer(customerName), "Bookings fetched successfully");
    }

    @GetMapping("/all")
     public ApiResponse<List<Flight>> getAllFlights() {
        return ApiResponse.ok(flightService.getAllFlight(), "Flights fetched successfully"); 
    }
}