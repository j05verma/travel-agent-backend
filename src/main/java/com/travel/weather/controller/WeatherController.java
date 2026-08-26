package com.travel.weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travel.common.dto.ApiResponse;
import com.travel.weather.dto.WeatherResponse;
import com.travel.weather.service.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping
    public ApiResponse<WeatherResponse> getWeather(@RequestParam String city) {
        WeatherResponse weather = weatherService.getWeather(city);
        return ApiResponse.ok(weather, "Weather fetched successfully");
    }

}
