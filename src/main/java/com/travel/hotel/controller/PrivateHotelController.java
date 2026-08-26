package com.travel.hotel.controller;

import com.travel.common.dto.ApiResponse;
import com.travel.hotel.dto.HotelRequest;
import com.travel.hotel.dto.HotelResponse;
import com.travel.hotel.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/private/api/hotels")
@RequiredArgsConstructor
public class PrivateHotelController {
    private final HotelService hotelService;

    @PostMapping("/add")
    public ApiResponse<HotelResponse> createHotel(@Valid @RequestBody HotelRequest request) {
        return ApiResponse.ok(hotelService.createHotel(request), "Hotel created successfully");
    }

    @DeleteMapping("/{name}")
    public ApiResponse<HotelResponse> deleteHotel(@PathVariable String name) {
        return ApiResponse.ok(hotelService.deleteHotel(name), "Hotel deleted successfully");
    }
}
