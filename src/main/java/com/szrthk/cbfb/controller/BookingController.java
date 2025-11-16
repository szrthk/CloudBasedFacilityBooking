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
import com.szrthk.cbfb.service.BookingService;
import com.szrthk.cbfb.dto.CreateBookingRequest;
import com.szrthk.cbfb.dto.AvailabilityResponse;
import com.szrthk.cbfb.dto.BookingResponse;
import com.szrthk.cbfb.model.Booking;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    // 1) Check availability for a facility on a date
    // GET /api/facilities/{id}/availability?date=2025-11-20
    @GetMapping("/facilities/{facilityId}/availability")
    public AvailabilityResponse getAvailability(
            @PathVariable String facilityId,
            @RequestParam String date
    ) {
        return service.getAvailability(facilityId, date);
    }

    // 2) Create booking
    // POST /api/bookings
    @PostMapping("/bookings")
    public BookingResponse createBooking(
            @Valid @RequestBody CreateBookingRequest request
    ) {
        Booking booking = service.createBooking(request);
        return BookingResponse.from(booking);
    }

    // 3) List bookings for a user
    // GET /api/bookings?userEmail=xyz@gmail.com
    @GetMapping("/bookings")
    public List<BookingResponse> getUserBookings(
            @RequestParam String userEmail
    ) {
        return service.getBookingsForUser(userEmail).stream()
                .map(BookingResponse::from)
                .toList();
    }

    // 4) Cancel booking
    // DELETE /api/bookings/{id}
    @DeleteMapping("/bookings/{id}")
    public void cancelBooking(@PathVariable String id) {
        service.cancelBooking(id);
    }

    // 5) Admin: list all bookings (simple monitoring)
    // GET /api/admin/bookings
    @GetMapping("/admin/bookings")
    public List<BookingResponse> getAllBookings() {
        return service.getAllBookings().stream()
                .map(BookingResponse::from)
                .toList();
    }
}
