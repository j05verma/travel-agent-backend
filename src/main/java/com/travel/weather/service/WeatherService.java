package com.travel.weather.service;

import com.travel.weather.dto.WeatherResponse;

public interface WeatherService {
    WeatherResponse getWeather(String city);

}
