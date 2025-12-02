package com.szrthk.cbfb.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.szrthk.cbfb.dto.AvailabilityResponse;
import com.szrthk.cbfb.dto.BookingRequest;
import com.szrthk.cbfb.dto.BookingResponse;
import com.szrthk.cbfb.dto.VerifyResponse;
import com.szrthk.cbfb.model.Booking;
import com.szrthk.cbfb.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @GetMapping("/facilities/{id}/availability")
    public AvailabilityResponse getAvailability(@PathVariable("id") String facilityId,
                                                @RequestParam String date) {
        return service.getAvailability(facilityId, date);
    }

    @PostMapping("/bookings/create")
    public BookingResponse create(@Valid @RequestBody BookingRequest request) {
        Booking booking = service.createBooking(request);
        return BookingResponse.from(booking);
    }

    @GetMapping("/bookings/my")
    public List<BookingResponse> myBookings(@RequestParam String userEmail) {
        return service.findByUserEmail(userEmail).stream()
                .map(BookingResponse::from)
                .toList();
    }

    @DeleteMapping("/bookings/{id}")
    public void cancel(@PathVariable String id) {
        service.cancelBooking(id);
    }

    @GetMapping("/admin/bookings")
    public List<BookingResponse> adminBookings() {
        return service.findAll().stream()
                .map(BookingResponse::from)
                .toList();
    }

    @GetMapping("/bookings/verify")
    public VerifyResponse verify(@RequestParam String bookingId,
                                 @RequestParam String signature) {
        return service.verifyBooking(bookingId, signature);
    }
}
