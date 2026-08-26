package com.travel.hotel.repository;

import com.travel.flight.model.BookingStatus;
import com.travel.hotel.model.HotelBooking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HotelBookingRepository extends MongoRepository<HotelBooking, String> {

    List<HotelBooking> findByGuestsGuestNameIgnoreCase(String guestName);

    List<HotelBooking> findByHotelNameIgnoreCaseAndCheckInDateAndStatus(
            String hotelName, String checkInDate, BookingStatus status);
}