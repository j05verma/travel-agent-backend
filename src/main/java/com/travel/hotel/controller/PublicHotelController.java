package com.travel.hotel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.travel.common.dto.ApiResponse;
import com.travel.hotel.dto.HotelBookingRequest;
import com.travel.hotel.dto.HotelBookingResponse;
import com.travel.hotel.dto.HotelResponse;
import com.travel.hotel.service.HotelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public/api/hotels")
@RequiredArgsConstructor
public class PublicHotelController {

    private final HotelService hotelService;

    @GetMapping("/all")
    public ApiResponse<List<HotelResponse>> getAllHotels() {
        return ApiResponse.ok(hotelService.getAllHotels(), "Hotels fetched successfully");
    }

    @GetMapping("/search")
    public ApiResponse<List<HotelResponse>> searchByCity(@RequestParam String city) {
        return ApiResponse.ok(hotelService.search(city), "Hotels fetched successfully");
    }

    @PostMapping("/book")
    public ApiResponse<HotelBookingResponse> book(@Valid @RequestBody HotelBookingRequest request) {
        return ApiResponse.ok(hotelService.book(request), "Hotel booked successfully");
    }

    @PutMapping("/cancel/{bookingId}")
    public ApiResponse<HotelBookingResponse> cancel(@PathVariable String bookingId) {
        return ApiResponse.ok(hotelService.cancel(bookingId), "Booking cancelled successfully");
    }

    @GetMapping("/bookings/guest/{guestName}")
    public ApiResponse<List<HotelBookingResponse>> getBookingsByGuest(@PathVariable String guestName) {
        return ApiResponse.ok(hotelService.getBookingsByGuest(guestName), "Bookings fetched successfully");
    }

    @GetMapping("/{name}")
    public ApiResponse<HotelResponse> findByName(@PathVariable String name) {
        return ApiResponse.ok(hotelService.findByName(name), "Hotel fetched successfully");
    }
}