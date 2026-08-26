package com.travel.flight.tools;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.travel.common.exception.ResourceNotFoundException;
import com.travel.flight.dto.FlightBookingRequest;
import com.travel.flight.dto.PassengerRequest;
import com.travel.flight.model.Flight;
import com.travel.flight.model.FlightBooking;
import com.travel.flight.service.FlightService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlightTools {
    private final FlightService flightService;

    @Tool(description = "Search available flights between a source city and a destination city")
    public String searchFlights(String source, String destination) {
        List<Flight> flights = flightService.search(source, destination);
        if(flights.isEmpty()){
            return "No flights found from " + source + " to " + destination;
        }
        return flights.stream()
                .map(f -> String.format("%s | %s -> %s | Airline: %s | Departs: %s | Price: Rs.%.2f | Seats left: %d",
                        f.getFlightNumber(), f.getSource(), f.getDestination(), f.getAirline(),
                        f.getDepartureTime(), f.getPrice(), f.getSeatsAvailable()))
                .collect(Collectors.joining("\n"));
    }


    @Tool(description = "Check seat availability for a specific flight by its flight number and departure time")
    public String checkFlightAvailability(String flightNumber, String departureTime) {
        try {
            Flight f = flightService.findByFlightNumber(flightNumber, departureTime);
            return f.getSeatsAvailable() > 0
                    ? "Flight " + flightNumber + " has " + f.getSeatsAvailable() + " seats available"
                    : "Flight " + flightNumber + " is fully booked";
        } catch (ResourceNotFoundException e) {
            return e.getMessage();
        }
    }

    @Tool(description = "Book a flight for one or more passengers. Requires the flight number, departure time, and a list of passenger names")
    public String bookFlight(String flightNumber, String departureTime, List<String> passengerNames){
                try {
            List<PassengerRequest> passengers = passengerNames.stream()
                    .map(name -> new PassengerRequest(name))
                    .collect(Collectors.toList());

            FlightBookingRequest request = new FlightBookingRequest(flightNumber, departureTime, passengers);
            FlightBooking booking = flightService.book(request);

            String seatInfo = booking.getPassengers().stream()
                    .map(p -> p.getPassengerName() + " (seat " + p.getSeatNumber() + ")")
                    .collect(Collectors.joining(", "));

            return "Flight booked successfully! Booking ID: " + booking.getId()
                    + ", Passengers: " + seatInfo
                    + ", Total price: Rs." + booking.getTotalPrice();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @Tool(description = "Cancel a flight booking using its booking ID")
    public String cancelFlightBooking(String bookingId) {
        try {
            flightService.cancel(bookingId);
            return "Booking " + bookingId + " has been cancelled successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

       @Tool(description = "Get all flight bookings for a passenger by their name")
    public String getBookingsByCustomer(String passengerName) {
        List<FlightBooking> bookings = flightService.getBookingsByCustomer(passengerName);
        if (bookings.isEmpty()) {
            return "No bookings found for passenger: " + passengerName;
        }
        return bookings.stream()
                .map(b -> String.format("%s | %s | %s | Rs.%.2f",
                        b.getId(), b.getFlightNumber(), b.getStatus(), b.getTotalPrice()))
                .collect(Collectors.joining("\n"));
    }

}
