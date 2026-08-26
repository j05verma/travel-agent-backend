package com.travel.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.travel.flight.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "hotel_bookings")
public class HotelBooking {

    @Id
    private String id;

    private String hotelName;
    private String checkInDate;
    private String checkOutDate;
    private int nights;
    private List<Guest> guests;
    private int rooms;
    private double totalPrice;
    private BookingStatus status;
    private LocalDateTime bookingDate;
}