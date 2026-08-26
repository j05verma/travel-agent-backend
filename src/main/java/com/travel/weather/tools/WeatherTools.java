package com.travel.weather.tools;

import com.travel.common.exception.ResourceNotFoundException;
import com.travel.weather.dto.WeatherResponse;
import com.travel.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeatherTools {

    private final WeatherService weatherService;

    @Tool(description = "Get the current weather for a given city, including temperature, wind speed, humidity, precipitation, and air quality — useful for travel planning")
    public String getWeather(String city) {
        try {
            WeatherResponse w = weatherService.getWeather(city);
            return String.format(
                    "Current weather in %s: %.1f°C, wind %.1f kph, humidity %.0f%%, precipitation %.1f mm, air quality index %d (%s)",
                    w.getCity(), w.getTemperatureCelsius(), w.getWindSpeedKph(),
                    w.getHumidityPercentage(), w.getPrecipitationMm(),
                    w.getAirQualityIndex(), w.getAirQualityCategory());
        } catch (ResourceNotFoundException e) {
            return e.getMessage();
        } catch (RuntimeException e) {
            return "Sorry, I couldn't fetch the weather for " + city + " right now.";
        }
    }
}