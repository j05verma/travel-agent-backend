package com.travel.hotel.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelBookingRequest {

    @NotBlank(message = "hotelName is required")
    private String hotelName;

    @NotBlank(message = "checkInDate is required")
    private String checkInDate;

    @NotBlank(message = "checkOutDate is required")
    private String checkOutDate;

    @Positive(message = "nights must be at least 1")
    private int nights;

    @NotEmpty(message = "At least one guest is required")
    @Valid
    private List<GuestRequest> guests;
}