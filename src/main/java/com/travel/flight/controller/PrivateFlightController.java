package com.travel.flight.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travel.common.dto.ApiResponse;
import com.travel.flight.dto.FlightRequest;
import com.travel.flight.model.Flight;
import com.travel.flight.service.FlightService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/private/api/flights")
@RequiredArgsConstructor
public class PrivateFlightController {
    private final FlightService flightService;
    
    @PostMapping("/add")
    public ApiResponse<Flight> createFlight(@Valid @RequestBody FlightRequest request) {
        Flight flight = flightService.createFlight(request);
        return ApiResponse.ok(flight, "Flight created successfully");
    }
    @DeleteMapping
    public ApiResponse<Flight> deleteFlight(
            @RequestParam String flightNumber,
            @RequestParam String departureTime) {
        return ApiResponse.ok(flightService.deleteFlight(flightNumber, departureTime), "Flight deleted successfully");
    }
    @GetMapping("/all")
    public ApiResponse<List<Flight>> getAllFlights() {
        return ApiResponse.ok(flightService.getAllFlights(), "Flights fetched successfully");
    }

}
