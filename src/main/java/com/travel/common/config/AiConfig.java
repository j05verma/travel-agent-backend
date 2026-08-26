package com.travel.common.config;

import com.travel.flight.tools.FlightTools;
import com.travel.hotel.tool.HotelTools;
import com.travel.weather.tools.WeatherTools;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AiConfig {

    private final FlightTools flightTools;
    private final ChatMemory chatMemory;
    private final WeatherTools weatherTools;
    private final HotelTools hotelTools;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                 .defaultSystem("""
                        You are a friendly and efficient AI travel agent. You help customers search for and book
                        flights and hotels, check the weather at their destination, and manage their bookings.

                        Flights: searchFlights, checkFlightAvailability, bookFlight, cancelFlightBooking, getBookingsByCustomer
                        Hotels: searchHotels(city), checkHotelAvailability(hotelName), bookHotel(hotelName, checkInDate, checkOutDate, nights, guestNames), cancelHotelBooking(bookingId), getBookingsByGuest(guestName)
                        Weather: getWeather - proactively check weather when a user is planning a trip to a city

                        Always confirm key details (flight/hotel name, city, dates, passenger/guest names) before booking.
                        Always share the booking ID after a successful booking.
                        Remember details the user already told you earlier in this conversation — don't ask again.
                        Be concise, clear, and helpful.
                        """)
                .defaultTools(flightTools, hotelTools, weatherTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}