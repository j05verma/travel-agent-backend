package com.travel.hotel.tool;

import com.travel.common.exception.ResourceNotFoundException;
import com.travel.hotel.dto.GuestRequest;
import com.travel.hotel.dto.HotelBookingRequest;
import com.travel.hotel.dto.HotelBookingResponse;
import com.travel.hotel.dto.HotelResponse;
import com.travel.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HotelTools {

    private final HotelService hotelService;

    @Tool(description = "Search available hotels in a given city")
    public String searchHotels(String city) {
        List<HotelResponse> hotels = hotelService.search(city);
        if (hotels.isEmpty()) {
            return "No hotels found in " + city;
        }
        return hotels.stream()
                .map(h -> String.format("%s | City: %s | Rs.%.2f per night | Rating: %.1f | Rooms left: %d",
                        h.getName(), h.getCity(), h.getPricePerNight(), h.getRating(), h.getRoomsAvailable()))
                .collect(Collectors.joining("\n"));
    }

    @Tool(description = "Check room availability for a specific hotel by its name")
    public String checkHotelAvailability(String hotelName) {
        try {
            HotelResponse h = hotelService.findByName(hotelName);
            return h.getRoomsAvailable() > 0
                    ? "Hotel " + hotelName + " has " + h.getRoomsAvailable() + " rooms available"
                    : "Hotel " + hotelName + " is fully booked";
        } catch (ResourceNotFoundException e) {
            return e.getMessage();
        }
    }

    @Tool(description = "Book a hotel for one or more guests. Requires the hotel name, check-in date, check-out date, number of nights, and a list of guest names")
    public String bookHotel(String hotelName, String checkInDate, String checkOutDate, int nights, List<String> guestNames) {
        try {
            List<GuestRequest> guests = guestNames.stream()
                    .map(GuestRequest::new)
                    .collect(Collectors.toList());
            HotelBookingRequest request = new HotelBookingRequest(hotelName, checkInDate, checkOutDate, nights, guests);
            HotelBookingResponse booking = hotelService.book(request);
            String roomInfo = booking.getGuests().stream()
                    .map(g -> g.getGuestName() + " (room " + g.getRoomNumber() + ")")
                    .collect(Collectors.joining(", "));
            return "Hotel booked successfully! Booking ID: " + booking.getBookingId()
                    + ", Guests: " + roomInfo
                    + ", Total price: Rs." + booking.getTotalPrice();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @Tool(description = "Cancel a hotel booking using its booking ID")
    public String cancelHotelBooking(String bookingId) {
        try {
            hotelService.cancel(bookingId);
            return "Booking " + bookingId + " has been cancelled successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @Tool(description = "Get all hotel bookings for a guest by their name")
    public String getBookingsByGuest(String guestName) {
        List<HotelBookingResponse> bookings = hotelService.getBookingsByGuest(guestName);
        if (bookings.isEmpty()) {
            return "No bookings found for guest: " + guestName;
        }
        return bookings.stream()
                .map(b -> String.format("%s | %s | %s | Rs.%.2f",
                        b.getBookingId(), b.getHotelName(), b.getStatus(), b.getTotalPrice()))
                .collect(Collectors.joining("\n"));
    }
}
