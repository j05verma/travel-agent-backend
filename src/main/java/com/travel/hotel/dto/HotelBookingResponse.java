package com.travel.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.travel.flight.model.BookingStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelBookingResponse {
    private String bookingId;
    private String hotelName;
    private String checkInDate;
    private String checkOutDate;
    private int nights;
    private List<GuestResponse> guests;
    private int rooms;
    private double totalPrice;
    private BookingStatus status;
}