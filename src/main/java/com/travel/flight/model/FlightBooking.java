package com.travel.flight.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flight_bookings")
public class FlightBooking {

    @Id
    private String id;

    private String flightNumber;
    private String departureTime;
    private String customerName;
    private int seats;
    private double totalPrice;
    private BookingStatus status;
    private LocalDateTime bookingDate;
}