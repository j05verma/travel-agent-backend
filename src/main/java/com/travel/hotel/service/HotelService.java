package com.travel.hotel.service;

import java.util.List;

import com.travel.hotel.dto.HotelBookingRequest;
import com.travel.hotel.dto.HotelBookingResponse;
import com.travel.hotel.dto.HotelRequest;
import com.travel.hotel.dto.HotelResponse;

public interface HotelService {
    HotelResponse createHotel(HotelRequest request);
    List<HotelResponse> getAllHotels();
    List<HotelResponse> search(String city);
    HotelResponse findByName(String name);
    HotelBookingResponse book(HotelBookingRequest request);
    HotelBookingResponse cancel(String bookingId);
    List<HotelBookingResponse> getBookingsByGuest(String guestName);
    HotelResponse deleteHotel(String name);
}