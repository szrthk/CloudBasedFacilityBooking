package com.szrthk.cbfb.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.szrthk.cbfb.dto.AvailabilityResponse;
import com.szrthk.cbfb.dto.BookingRequest;
import com.szrthk.cbfb.dto.VerifyResponse;
import com.szrthk.cbfb.model.Booking;
import com.szrthk.cbfb.model.Facility;
import com.szrthk.cbfb.repository.BookingRepository;
import com.szrthk.cbfb.repository.FacilityRepository;
import com.szrthk.cbfb.util.SlotConstants;

@Service
public class BookingService {

    private final FacilityRepository facilityRepository;
    private final BookingRepository bookingRepository;
    private final QrService qrService;
    private final S3Service s3Service;
    private final String qrSecret;

    public BookingService(FacilityRepository facilityRepository,
                          BookingRepository bookingRepository,
                          QrService qrService,
                          S3Service s3Service,
                          @Value("${qr.secret:qr-secret}") String qrSecret) {
        this.facilityRepository = facilityRepository;
        this.bookingRepository = bookingRepository;
        this.qrService = qrService;
        this.s3Service = s3Service;
        this.qrSecret = qrSecret;
    }

    public AvailabilityResponse getAvailability(String facilityId, String date) {
        if (facilityId == null || date == null) {
            throw new IllegalArgumentException("Facility ID and date are required");
        }
        
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));

        List<String> definedSlots = facility.getSlots() != null && !facility.getSlots().isEmpty()
                ? facility.getSlots()
                : SlotConstants.SLOTS;

        List<Booking> bookings = bookingRepository.findByFacilityIdAndDate(facilityId, date);
        Set<String> booked = bookings.stream()
                .map(Booking::getSlot)
                .filter(slot -> slot != null)
                .collect(Collectors.toSet());

        List<String> available = definedSlots.stream()
                .filter(slot -> slot != null && !booked.contains(slot))
                .toList();

        return new AvailabilityResponse(available, new ArrayList<>(booked));
    }

    @SuppressWarnings("null")
    public Booking createBooking(BookingRequest request) {
        if (request == null || request.facilityId() == null || request.date() == null || 
            request.slot() == null || request.userEmail() == null) {
            throw new IllegalArgumentException("All booking details are required");
        }

        @SuppressWarnings("null")
        Facility facility = facilityRepository.findById(request.facilityId())
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));

        List<String> allowedSlots = facility.getSlots() != null && !facility.getSlots().isEmpty()
                ? facility.getSlots()
                : SlotConstants.SLOTS;

        if (request.slot() == null || !allowedSlots.contains(request.slot())) {
            throw new IllegalArgumentException("Invalid or missing slot selection");
        }

        boolean conflict = bookingRepository
                .existsByFacilityIdAndDateAndSlot(request.facilityId(), request.date(), request.slot());
        if (conflict) {
            throw new IllegalArgumentException("Slot already booked");
        }

        String signature = generateSignature(request.facilityId(), request.date(), request.slot(), request.userEmail());
        String payload = buildPayload(request, signature);
        byte[] qrBytes = qrService.generateQrBytes(payload);
        String fileName = String.format("%s_%s_%s.png", 
            request.facilityId(), 
            request.date(), 
            request.slot().replace(':', '-'));
        String qrUrl = s3Service.upload(qrBytes, fileName);

        Booking booking = Booking.builder()
                .facilityId(request.facilityId())
                .userEmail(request.userEmail())
                .date(request.date())
                .slot(request.slot())
                .qrSignature(signature)
                .qrUrl(qrUrl)
                .build();

        return bookingRepository.save(booking);
    }

    public VerifyResponse verifyBooking(String bookingId, String signature) {
        if (bookingId == null || signature == null) {
            return new VerifyResponse(false, "Booking ID and signature are required");
        }
        
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
                
        if (booking.getFacilityId() == null || booking.getDate() == null || 
            booking.getSlot() == null || booking.getUserEmail() == null) {
            return new VerifyResponse(false, "Invalid booking data");
        }
        
        String expected = generateSignature(
            booking.getFacilityId(), 
            booking.getDate(), 
            booking.getSlot(), 
            booking.getUserEmail());
            
        boolean valid = expected != null && expected.equals(signature);
        return new VerifyResponse(valid, valid ? "Booking is valid" : "Invalid booking signature");
    }

    public List<Booking> findByUserEmail(String userEmail) {
        if (userEmail == null) {
            throw new IllegalArgumentException("User email is required");
        }
        return bookingRepository.findByUserEmail(userEmail);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public void cancelBooking(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Booking ID is required");
        }
        bookingRepository.deleteById(id);
    }

    private String buildPayload(BookingRequest request, String signature) {
        return "facilityId=" + request.facilityId() +
                ";date=" + request.date() +
                ";slot=" + request.slot() +
                ";userEmail=" + request.userEmail() +
                ";signature=" + signature;
    }

    private String generateSignature(String facilityId, String date, String slot, String userEmail) {
        try {
            String payload = facilityId + "|" + date + "|" + slot + "|" + userEmail;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(qrSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to generate QR signature", e);
        }
    }
}
