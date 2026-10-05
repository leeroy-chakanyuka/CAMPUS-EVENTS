package za.ac.cput;

import javax.swing.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.DTO.EventResponseDTO;
import za.ac.cput.DTO.FacultyResponseDTO;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class BrowseEventsPanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private final Long studentId;
    private final Consumer<EventResponseDTO> onView;

    private final List<EventResponseDTO> allEvents = new ArrayList<>();
    private final List<EventResponseDTO> shownEvents = new ArrayList<>();

    private JPanel grid;
    private JTextField txtSearch;
    private JComboBox<String> cmbFaculty;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public BrowseEventsPanel(Long studentId, Consumer<EventResponseDTO> onView) {
        this.studentId = studentId;
        this.onView = onView;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(Color.WHITE);
        north.add(buildHeader(), BorderLayout.NORTH);
        north.add(buildFilters(), BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        grid = new JPanel(new GridLayout(0, 3, 12, 12));
        grid.setBackground(Color.WHITE);
        grid.setBorder(new EmptyBorder(12, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(24);
        add(scrollPane, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::loadEvents);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Browse Events");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        JButton refreshButton = new JButton("↻ Refresh");
        refreshButton.setBackground(CPUT_BLUE);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.addActionListener(e -> loadEvents());

        header.add(title, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);
        return header;
    }

    private JPanel buildFilters() {
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        filters.setBackground(Color.WHITE);

        txtSearch = new JTextField(22);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 13));
        txtSearch.addActionListener(e -> applyFilter());

        cmbFaculty = new JComboBox<>(new String[]{"All faculties"});
        cmbFaculty.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbFaculty.addActionListener(e -> applyFilter());

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(CPUT_BLUE);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> applyFilter());

        filters.add(new JLabel("Search:"));
        filters.add(txtSearch);
        filters.add(new JLabel("Faculty:"));
        filters.add(cmbFaculty);
        filters.add(searchButton);
        return filters;
    }

    private JPanel eventCard(EventResponseDTO event) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(0, 0, 10, 0)));

        // Placeholder thumbnail: flat random color per event, stable across
        // refreshes (seeded by event id). Swapped for real images later.
        long seed = event.getId() == null ? 0 : event.getId();
        Random rand = new Random(seed * 31 + 7);
        Color thumb = Color.getHSBColor(rand.nextFloat(), 0.45f, 0.92f);
        JPanel thumbnail = new JPanel(new GridBagLayout());
        thumbnail.setBackground(thumb);
        thumbnail.setPreferredSize(new Dimension(0, 90));
        String initial = event.getTitle() == null || event.getTitle().isBlank()
                ? "?" : event.getTitle().substring(0, 1).toUpperCase();
        JLabel letter = new JLabel(initial);
        letter.setFont(new Font("Arial", Font.BOLD, 40));
        letter.setForeground(new Color(255, 255, 255, 230));
        thumbnail.add(letter);
        card.add(thumbnail, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);
        info.setBorder(new EmptyBorder(10, 12, 0, 12));

        JLabel title = new JLabel(event.getTitle() == null ? "" : event.getTitle());
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(DARK_TEXT);

        JLabel sub = new JLabel((event.getFacultyName() == null ? "" : event.getFacultyName())
                + " · " + (event.getVenueName() == null ? "" : event.getVenueName()));
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(new Color(120, 120, 120));

        JLabel meta = new JLabel(dateOnly(event.getEventDate()) + " · " + spotsLeft(event));
        meta.setFont(new Font("Arial", Font.BOLD, 12));
        meta.setForeground(CPUT_BLUE);

        info.add(title);
        info.add(Box.createVerticalStrut(4));
        info.add(sub);
        info.add(Box.createVerticalStrut(4));
        info.add(meta);
        card.add(info, BorderLayout.CENTER);

        JButton viewButton = new JButton("View");
        viewButton.setBackground(CPUT_BLUE);
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.addActionListener(e -> { if (onView != null) onView.accept(event); });
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.setBackground(Color.WHITE);
        buttonRow.add(viewButton);
        card.add(buttonRow, BorderLayout.SOUTH);

        return card;
    }

    private void applyFilter() {
        String query = txtSearch.getText().trim().toLowerCase();
        String faculty = (String) cmbFaculty.getSelectedItem();
        shownEvents.clear();
        grid.removeAll();
        for (EventResponseDTO event : allEvents) {
            if (faculty != null && !"All faculties".equals(faculty)
                    && (event.getFacultyName() == null || !faculty.equals(event.getFacultyName()))) {
                continue;
            }
            if (!query.isBlank() && (event.getTitle() == null
                    || !event.getTitle().toLowerCase().contains(query))) {
                continue;
            }
            shownEvents.add(event);
            grid.add(eventCard(event));
        }
        if (shownEvents.isEmpty()) {
            JLabel empty = new JLabel("No open events match.", SwingConstants.CENTER);
            empty.setFont(new Font("Arial", Font.PLAIN, 14));
            empty.setForeground(new Color(120, 120, 120));
            grid.add(empty);
        }
        grid.revalidate();
        grid.repaint();
    }

    private void loadEvents() {
        allEvents.clear();
        try {
            HttpRequest reqFaculties = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/faculty"))
                    .GET()
                    .build();
            HttpResponse<String> resFaculties = HTTP.send(reqFaculties, HttpResponse.BodyHandlers.ofString());
            if (resFaculties.statusCode() / 100 == 2) {
                List<FacultyResponseDTO> faculties = MAPPER.readValue(
                        resFaculties.body(), new TypeReference<List<FacultyResponseDTO>>() {});
                String selected = (String) cmbFaculty.getSelectedItem();
                cmbFaculty.removeAllItems();
                cmbFaculty.addItem("All faculties");
                for (FacultyResponseDTO f : faculties) {
                    cmbFaculty.addItem(f.getName());
                }
                if (selected != null) cmbFaculty.setSelectedItem(selected);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/event"))
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
            List<EventResponseDTO> events = MAPPER.readValue(
                    response.body(), new TypeReference<List<EventResponseDTO>>() {});
            for (EventResponseDTO event : events) {
                if (Boolean.FALSE.equals(event.getOpen())) continue;
                allEvents.add(event);
            }
            applyFilter();
        } catch (Exception ex) {
            showError("Could not load events: " + ex.getMessage());
        }
    }

    static String spotsLeft(EventResponseDTO event) {
        if (event.getCapacity() == null) return "—";
        int sold = event.getTicketsSold() == null ? 0 : event.getTicketsSold();
        int left = event.getCapacity() - sold;
        return left <= 0 ? "Full" : left + " left";
    }

    static String dateOnly(String raw) {
        if (raw == null) return "";
        int t = raw.indexOf('T');
        return t < 0 ? raw : raw.substring(0, t);
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                this, message == null || message.isBlank() ? "Request failed" : message,
                "Student request failed", JOptionPane.ERROR_MESSAGE));
    }
}
