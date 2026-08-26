package com.travel.weather.service.impl;

import com.travel.common.exception.ResourceNotFoundException;
import com.travel.weather.dto.WeatherResponse;
import com.travel.weather.service.WeatherService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final RestClient restClient = RestClient.create();

    @Override
    @SuppressWarnings("unchecked")
    public WeatherResponse getWeather(String city) {
        // convert city name to latitude/longitude
        Map<String, Object> geoResponse = restClient.get()
                .uri("https://geocoding-api.open-meteo.com/v1/search?name={city}&count=1", city)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> results = (List<Map<String, Object>>) geoResponse.get("results");
        if (results == null || results.isEmpty()) {
            throw new ResourceNotFoundException("Could not find weather data for city: " + city);
        }

        Map<String, Object> location = results.get(0);
        double latitude = ((Number) location.get("latitude")).doubleValue();
        double longitude = ((Number) location.get("longitude")).doubleValue();

        //  fetch current weather (temperature, wind, humidity, precipitation)
        Map<String, Object> weatherResponse = restClient.get()
                .uri("https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}" +
                                "&current=temperature_2m,wind_speed_10m,relative_humidity_2m,precipitation" +
                                "&wind_speed_unit=kmh",
                        latitude, longitude)
                .retrieve()
                .body(Map.class);

        Map<String, Object> current = (Map<String, Object>) weatherResponse.get("current");
        double temperature = ((Number) current.get("temperature_2m")).doubleValue();
        double windSpeed = ((Number) current.get("wind_speed_10m")).doubleValue();
        double humidity = ((Number) current.get("relative_humidity_2m")).doubleValue();
        double precipitation = ((Number) current.get("precipitation")).doubleValue();

        // fetch air quality (separate API)
        Map<String, Object> airQualityResponse = restClient.get()
                .uri("https://air-quality-api.open-meteo.com/v1/air-quality?latitude={lat}&longitude={lon}&current=us_aqi",
                        latitude, longitude)
                .retrieve()
                .body(Map.class);

        Map<String, Object> airCurrent = (Map<String, Object>) airQualityResponse.get("current");
        int aqi = ((Number) airCurrent.get("us_aqi")).intValue();

        return WeatherResponse.builder()
                .city(city)
                .temperatureCelsius(temperature)
                .windSpeedKph(windSpeed)
                .humidityPercentage(humidity)
                .precipitationMm(precipitation)
                .airQualityIndex(aqi)
                .airQualityCategory(categorizeAqi(aqi))
                .build();
    }

    private String categorizeAqi(int aqi) {
        if (aqi <= 50) return "Good";
        if (aqi <= 100) return "Moderate";
        if (aqi <= 150) return "Unhealthy for Sensitive Groups";
        if (aqi <= 200) return "Unhealthy";
        if (aqi <= 300) return "Very Unhealthy";
        return "Hazardous";
    }
}