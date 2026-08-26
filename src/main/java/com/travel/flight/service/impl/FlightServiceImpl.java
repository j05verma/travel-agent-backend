package com.travel.flight.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.travel.common.exception.DuplicateResourceException;
import com.travel.common.exception.InvalidOperationException;
import com.travel.common.exception.ResourceNotFoundException;
import com.travel.flight.dto.FlightRequest;
import com.travel.flight.model.BookingStatus;
import com.travel.flight.model.Flight;
import com.travel.flight.model.FlightBooking;
import com.travel.flight.repository.FlightBookingRepository;
import com.travel.flight.repository.FlightRepository;
import com.travel.flight.service.FlightService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightBookingRepository flightBookingRepository;
    private final FlightRepository flightRepository;

    
    @Override
    public Flight createFlight(FlightRequest request) {
        flightRepository.findByFlightNumberIgnoreCaseAndDepartureTimeAndDeletedFalse(
                        request.getFlightNumber(), request.getDepartureTime())
                .ifPresent(f -> {
                    throw new DuplicateResourceException(
                            "Flight " + request.getFlightNumber() + " already exists for departure "
                                    + request.getDepartureTime());
                });
                
           Flight flight = Flight.builder()
                .flightNumber(request.getFlightNumber())
                .airline(request.getAirline())
                .source(request.getSource())
                .destination(request.getDestination())
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .price(request.getPrice())
                .seatsAvailable(request.getSeatsAvailable())
                .build();

        return flightRepository.save(flight);
    }


    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }


    @Override
    public List<Flight> search(String source, String destination) {
        return flightRepository.findBySourceIgnoreCaseAndDestinationIgnoreCaseAndDeletedFalse(source, destination);
    }


    @Override
    public Flight findByFlightNumber(String flightNumber,  String departureTime) {
          return flightRepository.findByFlightNumberIgnoreCaseAndDepartureTimeAndDeletedFalse(flightNumber, departureTime)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Flight " + flightNumber + " not found for departure " + departureTime));
    }


    @Override
    public FlightBooking book(String flightNumber, String departureTime, String customerName, int seats) {
       Flight flight = flightRepository.findByFlightNumberIgnoreCaseAndDeletedFalseAndDepartureTime(flightNumber, departureTime)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Flight " + flightNumber + " not found for departure " + departureTime));


        if(flight.getSeatsAvailable() < seats) {
             throw new InvalidOperationException("Not enough seats available on flight " + flightNumber
                    + ". Only " + flight.getSeatsAvailable() + " left");
        }

        flight.setSeatsAvailable(flight.getSeatsAvailable() - seats);
        flightRepository.save(flight);
        FlightBooking booking = FlightBooking.builder()
                .flightNumber(flight.getFlightNumber())
                .departureTime(flight.getDepartureTime())
                .customerName(customerName)
                .seats(seats)
                .totalPrice(flight.getPrice() * seats)
                .status(BookingStatus.CONFIRMED)
                .bookingDate(LocalDateTime.now())
                .build();
                return flightBookingRepository.save(booking);
    }


    @Override
    public FlightBooking cancel(String bookingId) {
        FlightBooking booking = flightBookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight booking not found for ID: " + bookingId));
         if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidOperationException("Booking " + bookingId + " is already cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        flightBookingRepository.save(booking);
        flightRepository.findByFlightNumberIgnoreCaseAndDeletedFalseAndDepartureTime(booking.getFlightNumber(), booking.getDepartureTime())
        .ifPresent(flight -> {
            flight.setSeatsAvailable(flight.getSeatsAvailable() + booking.getSeats());
            flightRepository.save(flight);
        });

        return booking;
    }


    @Override
    public List<FlightBooking> getBookingsByCustomer(String customerName) {
        return flightBookingRepository.findByCustomerNameIgnoreCase(customerName);
    }


    @Override
    public Flight deleteFlight(String flightNumber, String departureTime) {
        Flight flight = flightRepository.findByFlightNumberIgnoreCaseAndDepartureTimeAndDeletedFalse(flightNumber, departureTime)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Flight " + flightNumber + " not found for departure " + departureTime));
        flight.setDeleted(true);
        return flightRepository.save(flight);
    }


    @Override
    public List<Flight> getAllFlight() {
        return flightRepository.findByDeletedFalse();
    }

}
