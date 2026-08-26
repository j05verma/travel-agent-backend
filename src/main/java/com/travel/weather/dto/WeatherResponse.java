package com.travel.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {
    private String city;
    private double temperatureCelsius;
    private double windSpeedKph;
    private double humidityPercentage;
    private double precipitationMm;
    private int airQualityIndex;
    private String airQualityCategory;

}
