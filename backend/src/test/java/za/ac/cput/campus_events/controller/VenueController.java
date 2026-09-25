package za.ac.cput.campus_events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.domain.Venue;
import za.ac.cput.campus_events.service.IVenueService;

import java.util.List;

@RestController
@RequestMapping("/venue")
public class VenueController {
    private final IVenueService service;

    public VenueController(IVenueService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Venue> create(@RequestBody Venue venue) {
        return ResponseEntity.ok(service.create(venue));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venue> update(@PathVariable Long id, @RequestBody Venue venue) {
        return ResponseEntity.ok(service.update(id, venue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> findById(@PathVariable Long id) {
        Venue venue = service.findById(id);
        return (venue != null) ? ResponseEntity.ok(venue) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Venue>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
