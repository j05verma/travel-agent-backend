package com.travel.hotel.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    @Id
    private String id;
    private String name;
    private String city;
    private double pricePerNight;
    private int roomsAvailable;
    private double rating;
    private boolean deleted = false;

}
