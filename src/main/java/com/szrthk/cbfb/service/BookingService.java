package com.szrthk.cbfb.service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.szrthk.cbfb.model.Booking;
import com.szrthk.cbfb.model.Facility;
import com.szrthk.cbfb.repositery.BookingRepository;
import com.szrthk.cbfb.repositery.FacilityRepository;
import com.szrthk.cbfb.dto.CreateBookingRequest;
import com.szrthk.cbfb.dto.AvailabilityResponse;
import com.szrthk.cbfb.dto.SlotResponse;

@Service
public class BookingService {

    private final FacilityRepository facilityRepository;
    private final BookingRepository bookingRepository;

    public BookingService(FacilityRepository facilityRepository,
                          BookingRepository bookingRepository) {
        this.facilityRepository = facilityRepository;
        this.bookingRepository = bookingRepository;
    }

    public AvailabilityResponse getAvailability(String facilityId, String date) {
        @SuppressWarnings("null")
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));

        int slotMinutes = facility.getSlotDurationMinutes();
        LocalTime open = LocalTime.parse(facility.getOpenTime());
        LocalTime close = LocalTime.parse(facility.getCloseTime());

        // find all bookings for that day
        List<Booking> bookings = bookingRepository
                .findByFacilityIdAndDate(facilityId, date);

        // map of booked startTime -> true
        Set<String> bookedStarts = bookings.stream()
                .map(Booking::getStartTime)
                .collect(Collectors.toSet());

        List<SlotResponse> slots = new ArrayList<>();

        LocalTime current = open;
        while (current.plusMinutes(slotMinutes).compareTo(close) <= 0) {
            LocalTime end = current.plusMinutes(slotMinutes);
            String startStr = current.toString(); // default format HH:mm
            String endStr = end.toString();

            boolean booked = bookedStarts.contains(startStr);
            slots.add(new SlotResponse(startStr, endStr, booked));

            current = end;
        }

        return new AvailabilityResponse(
                facilityId,
                date,
                slotMinutes,
                slots
        );
    }

    @SuppressWarnings("null")
    public Booking createBooking(CreateBookingRequest req) {
        @SuppressWarnings("null")
        Facility facility = facilityRepository.findById(req.facilityId())
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));

        int slotMinutes = facility.getSlotDurationMinutes();
        LocalTime open = LocalTime.parse(facility.getOpenTime());
        LocalTime close = LocalTime.parse(facility.getCloseTime());
        LocalTime start = LocalTime.parse(req.startTime());
        LocalTime end = start.plusMinutes(slotMinutes);

        // 1) check within working hours
        if (start.isBefore(open) || end.isAfter(close)) {
            throw new IllegalArgumentException("Requested time is outside facility hours");
        }

        // 2) check aligns to slot size (multiple of slotMinutes from open)
        long minutesFromOpen = java.time.Duration.between(open, start).toMinutes();
        if (minutesFromOpen % slotMinutes != 0) {
            throw new IllegalArgumentException("Requested time is not aligned to slot duration");
        }

        // 3) check not already booked
        List<Booking> existing = bookingRepository
                .findByFacilityIdAndDate(req.facilityId(), req.date());
        boolean conflict = existing.stream()
                .anyMatch(b -> b.getStartTime().equals(req.startTime()));
        if (conflict) {
            throw new IllegalArgumentException("Slot already booked");
        }

        Booking booking = Booking.builder()
                .facilityId(req.facilityId())
                .userName(req.userName())
                .userEmail(req.userEmail())
                .date(req.date())
                .startTime(req.startTime())
                .build();

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsForUser(String userEmail) {
        return bookingRepository.findByUserEmail(userEmail);
    }

    @SuppressWarnings("null")
    public void cancelBooking(String id) {
        bookingRepository.deleteById(id);
    }

    // admin utility
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
