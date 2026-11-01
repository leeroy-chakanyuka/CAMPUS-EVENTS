package za.ac.cput;

import za.ac.cput.DTO.EventResponseDTO;

/** Dependency-free regression assertions for Stage 2; run with -ea. */
public final class Stage2BrowseChecks {
    private Stage2BrowseChecks() { }

    public static void main(String[] args) {
        EventResponseDTO event = new EventResponseDTO();
        event.setCapacity(50);
        event.setTicketsSold(17);
        assert "33 left".equals(BrowseEventsPanel.spotsLeft(event));
        event.setTicketsSold(50);
        assert "Full".equals(BrowseEventsPanel.spotsLeft(event));
        event.setCapacity(null);
        assert "Open".equals(BrowseEventsPanel.spotsLeft(event));
        event.setCapacity(0);
        assert "Open".equals(BrowseEventsPanel.spotsLeft(event));
        assert "2026-11-12".equals(BrowseEventsPanel.dateOnly("2026-11-12T09:30:00"));
        assert "2026-11-12".equals(BrowseEventsPanel.dateOnly("2026-11-12"));
        assert "".equals(BrowseEventsPanel.dateOnly(null));
        System.out.println("Stage 2 browse presentation assertions passed.");
    }
}
