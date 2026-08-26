package com.travel.flight.tools;

import org.springframework.stereotype.Component;

import com.travel.flight.service.FlightService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlightTools {
    private final FlightService flightService;

}
