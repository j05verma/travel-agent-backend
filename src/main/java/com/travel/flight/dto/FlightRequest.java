package com.travel.flight.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequest {

    @NotBlank(message = "flightNumber is required")
    private String flightNumber;

    @NotBlank(message = "airline is required")
    private String airline;

    @NotBlank(message = "source is required")
    private String source;

    @NotBlank(message = "destination is required")
    private String destination;

    @NotBlank(message = "departureTime is required")
    private String departureTime;

    @NotBlank(message = "arrivalTime is required")
    private String arrivalTime;

    @Positive(message = "price must be positive")
    private double price;

    @Positive(message = "seatsAvailable must be positive")
    private int seatsAvailable;
}