package com.travel.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {
    private String id;
    private String name;
    private String city;
    private double pricePerNight;
    private int roomsAvailable;
    private double rating;
}