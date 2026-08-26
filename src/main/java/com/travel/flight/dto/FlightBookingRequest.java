package com.travel.flight.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightBookingRequest {

    @NotBlank(message = "flightNumber is required")
    private String flightNumber;

    @NotBlank(message = "departureTime is required")
    private String departureTime;
 
    @NotBlank(message = "customerName is required")
    private String customerName;

    @Min(value = 1, message = "seats must be at least 1")
    private int seats;
}