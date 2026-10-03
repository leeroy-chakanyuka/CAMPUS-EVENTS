package za.ac.cput.campus_events.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.DTO.CreateVenueRequestDTO;
import za.ac.cput.campus_events.DTO.VenueResponseDTO;
import za.ac.cput.campus_events.domain.Address;
import za.ac.cput.campus_events.domain.Venue;
import za.ac.cput.campus_events.service.IVenueService;

import java.util.List;

/**
 * Venue CRUD. GET /venue feeds the organiser event form's venue dropdown —
 * the one read every organiser client needs. Writes exist for the future
 * venue admin screen; no business rules beyond required fields yet.
 */
@RestController
@RequestMapping("/venue")
public class VenueController {

    private final IVenueService venueService;

    public VenueController(IVenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public ResponseEntity<List<VenueResponseDTO>> allVenues() {
        return ResponseEntity.ok(venueService.findAll().stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> venueById(@PathVariable Long id) {
        return venueService.findById(id)
                .<ResponseEntity<?>>map(v -> ResponseEntity.ok(toResponse(v)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venue not found: " + id));
    }

    @PostMapping
    public ResponseEntity<?> createVenue(@RequestBody CreateVenueRequestDTO dto) {
        try {
            validate(dto);
            Venue saved = venueService.save(toVenue(new Venue.Builder(), dto));
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVenue(@PathVariable Long id,
                                         @RequestBody CreateVenueRequestDTO dto) {
        try {
            validate(dto);
            Venue existing = venueService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Venue not found: " + id));
            Venue.Builder builder = new Venue.Builder()
                    .setId(existing.getId());
            Venue saved = venueService.save(toVenue(builder, dto));
            return ResponseEntity.ok(toResponse(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVenue(@PathVariable Long id) {
        try {
            venueService.deleteById(id);
            return ResponseEntity.ok("Venue deleted");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Cannot delete venue with events");
        }
    }

    private void validate(CreateVenueRequestDTO dto) {
        if (dto == null) throw new RuntimeException("Venue details are required");
        if (dto.getName() == null || dto.getName().isBlank()) throw new RuntimeException("Name is required");
        if (dto.getCapacity() == null || dto.getCapacity() <= 0) {
            throw new RuntimeException("Capacity must be greater than zero");
        }
    }

    private Venue toVenue(Venue.Builder builder, CreateVenueRequestDTO dto) {
        Address address = null;
        if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
            address = new Address.Builder().setStreet(dto.getAddress().trim()).build();
        }
        return builder
                .setName(dto.getName().trim())
                .setCapacity(dto.getCapacity())
                .setAddress(address)
                .build();
    }

    private VenueResponseDTO toResponse(Venue venue) {
        VenueResponseDTO dto = new VenueResponseDTO();
        dto.setId(venue.getId());
        dto.setName(venue.getName());
        dto.setCapacity(venue.getCapacity());
        dto.setCity(venue.getAddress() == null ? null : venue.getAddress().getCity());
        return dto;
    }
}
