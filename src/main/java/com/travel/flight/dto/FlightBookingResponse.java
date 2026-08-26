package com.travel.flight.dto;

import com.travel.flight.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightBookingResponse {
    private String bookingId;
    private String flightNumber;
    private String departureTime;
    private String customerName;
    private int seats;
    private double totalPrice;
    private BookingStatus status;
}