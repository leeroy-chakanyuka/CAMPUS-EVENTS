package za.ac.cput.campus_events.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.campus_events.domain.Venue;
import za.ac.cput.campus_events.service.IVenueService;

import java.util.List;

@RestController
@RequestMapping("/venue")
public class VenueController {

    private final IVenueService venueService;

    public VenueController(IVenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public List<Venue> getAllVenues() {
        return venueService.findAll();
    }
}