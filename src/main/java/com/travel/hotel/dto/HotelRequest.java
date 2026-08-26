package com.travel.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "city is required")
    private String city;

    @Positive(message = "pricePerNight must be positive")
    private double pricePerNight;

    @Positive(message = "roomsAvailable must be positive")
    private int roomsAvailable;

    private double rating;
}