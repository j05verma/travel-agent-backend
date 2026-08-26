package com.travel.hotel.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.travel.common.exception.DuplicateResourceException;
import com.travel.common.exception.InvalidOperationException;
import com.travel.common.exception.ResourceNotFoundException;
import com.travel.flight.model.BookingStatus;
import com.travel.hotel.dto.GuestResponse;
import com.travel.hotel.dto.HotelBookingRequest;
import com.travel.hotel.dto.HotelBookingResponse;
import com.travel.hotel.dto.HotelRequest;
import com.travel.hotel.dto.HotelResponse;
import com.travel.hotel.model.Guest;
import com.travel.hotel.model.Hotel;
import com.travel.hotel.model.HotelBooking;
import com.travel.hotel.repository.HotelBookingRepository;
import com.travel.hotel.repository.HotelRepository;
import com.travel.hotel.service.HotelService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl  implements HotelService{
    private final HotelRepository hotelRepository;
    private final HotelBookingRepository bookingRepository;

    @Override
    public HotelResponse createHotel(HotelRequest request) {
        hotelRepository.findByNameIgnoreCaseAndCityIgnoreCaseAndDeletedFalse(request.getName(), request.getCity())
                .ifPresent(h -> {
                    throw new DuplicateResourceException(
                            "Hotel " + request.getName() + " already exists in " + request.getCity());
                });

        Hotel hotel = Hotel.builder()
                .name(request.getName())
                .city(request.getCity())
                .pricePerNight(request.getPricePerNight())
                .roomsAvailable(request.getRoomsAvailable())
                .rating(request.getRating())
                .deleted(false)
                .build();

        Hotel saved = hotelRepository.save(hotel);
        return toHotelResponse(saved);
    }

    @Override
    public List<HotelResponse> getAllHotels() {
         return hotelRepository.findByDeletedFalse()
            .stream()
            .map(this::toHotelResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<HotelResponse> search(String city) {
        return hotelRepository.findByCityIgnoreCaseAndDeletedFalse(city)
            .stream()
            .map(this::toHotelResponse)
            .collect(Collectors.toList());
    }

    @Override
    public HotelResponse findByName(String name) {
        Hotel hotel = hotelRepository.findByDeletedFalse()
            .stream()
            .filter(h -> h.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found: " + name));

           return toHotelResponse(hotel);
    }

    @Override
    public HotelBookingResponse book(HotelBookingRequest request) {
        Hotel hotel = hotelRepository.findByDeletedFalse()
            .stream()
            .filter(h -> h.getName().equalsIgnoreCase(request.getHotelName()))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found: " + request.getHotelName()));

            int roomsNeeded = request.getGuests().size();
            if (hotel.getRoomsAvailable() < roomsNeeded) {
                 throw new InvalidOperationException(
                "Not enough rooms available at " + hotel.getName() + ". Available: " + hotel.getRoomsAvailable());
            }
            List<Guest> guests = new java.util.ArrayList<>();
            int roomCounter = 101;
            for (var guestReq : request.getGuests()) {
                 guests.add(Guest.builder()
                 .guestName(guestReq.getGuestName())
                 .roomNumber(roomCounter++)
                 .build());
            }
            double totalPrice = hotel.getPricePerNight() * request.getNights() * roomsNeeded;
            HotelBooking booking = HotelBooking.builder()
            .hotelName(hotel.getName())
            .checkInDate(request.getCheckInDate())
            .checkOutDate(request.getCheckOutDate())
            .nights(request.getNights())
            .guests(guests)
            .rooms(roomsNeeded)
            .totalPrice(totalPrice)
            .status(BookingStatus.CONFIRMED)
            .bookingDate(LocalDateTime.now())
            .build();
        
             hotel.setRoomsAvailable(hotel.getRoomsAvailable() - roomsNeeded);
             hotelRepository.save(hotel);

              HotelBooking saved = bookingRepository.save(booking);
              return toBookingResponse(saved);
    }


       @Override
      public HotelBookingResponse cancel(String bookingId) {
       HotelBooking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

       if (booking.getStatus() == BookingStatus.CANCELLED) {
        throw new IllegalStateException("Booking " + bookingId + " is already cancelled");
       }

          booking.setStatus(BookingStatus.CANCELLED);
           HotelBooking saved = bookingRepository.save(booking);

             hotelRepository.findByDeletedFalse().stream()
            .filter(h -> h.getName().equalsIgnoreCase(booking.getHotelName()))
            .findFirst()
            .ifPresent(h -> {
                h.setRoomsAvailable(h.getRoomsAvailable() + booking.getRooms());
                hotelRepository.save(h);
            });

    return toBookingResponse(saved);
    }

    @Override
    public List<HotelBookingResponse> getBookingsByGuest(String guestName) {
        return bookingRepository.findByGuestsGuestNameIgnoreCase(guestName)
            .stream()
            .map(this::toBookingResponse)
            .collect(Collectors.toList());
    }

    @Override
    public HotelResponse deleteHotel(String name) {
        Hotel hotel = hotelRepository.findByDeletedFalse()
            .stream()
            .filter(h -> h.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found: " + name));

         hotel.setDeleted(true);
         Hotel saved = hotelRepository.save(hotel);
         return toHotelResponse(saved);
    }

        // ---------- private helper ----------

    private HotelResponse toHotelResponse(Hotel hotel) {
        return HotelResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .city(hotel.getCity())
                .pricePerNight(hotel.getPricePerNight())
                .roomsAvailable(hotel.getRoomsAvailable())
                .rating(hotel.getRating())
                .build();
    }

    private HotelBookingResponse toBookingResponse(HotelBooking booking) {
    List<GuestResponse> guestResponses = booking.getGuests().stream()
            .map(g -> GuestResponse.builder()
                    .roomNumber(g.getRoomNumber())
                    .guestName(g.getGuestName())
                    .build())
            .collect(Collectors.toList());

    return HotelBookingResponse.builder()
            .bookingId(booking.getId())
            .hotelName(booking.getHotelName())
            .checkInDate(booking.getCheckInDate())
            .checkOutDate(booking.getCheckOutDate())
            .nights(booking.getNights())
            .guests(guestResponses)
            .rooms(booking.getRooms())
            .totalPrice(booking.getTotalPrice())
            .status(booking.getStatus())
            .build();
        } 
 
}
