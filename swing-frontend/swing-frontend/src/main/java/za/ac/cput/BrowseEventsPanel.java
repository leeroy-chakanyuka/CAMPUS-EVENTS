package za.ac.cput;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.DTO.EventResponseDTO;
import za.ac.cput.DTO.FacultyResponseDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * Student discovery screen. Rendering and filters stay on the Swing event thread;
 * network I/O runs in a SwingWorker. No fixture events are shipped to the app.
 */
public class BrowseEventsPanel extends JPanel {
    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5)).build();
    private static final String ALL_FACULTIES = "All faculties";
    private static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);

    private final Consumer<EventResponseDTO> onView;
    private final List<EventResponseDTO> allEvents = new ArrayList<>();

    private final ResponsiveGridPanel grid = new ResponsiveGridPanel(248, 18);
    private final JTextField search = new JTextField();
    private final JComboBox<String> faculties = new JComboBox<>(new String[]{ALL_FACULTIES});
    private final JLabel resultCount = new JLabel("Loading events…");
    private final JButton refresh = UiTheme.secondaryButton("Refresh");
    private boolean loading;

    public BrowseEventsPanel(Long studentId, Consumer<EventResponseDTO> onView) {
        this.onView = onView;
        setLayout(new BorderLayout(0, 16));
        setBackground(UiTheme.CANVAS);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(buildHero());
        header.add(Box.createVerticalStrut(18));
        header.add(buildControls());
        add(header, BorderLayout.NORTH);

        grid.setBackground(UiTheme.CANVAS);
        grid.setBorder(new EmptyBorder(2, 0, 12, 0));
        JScrollPane viewport = new JScrollPane(grid);
        viewport.setBorder(null);
        viewport.setBackground(UiTheme.CANVAS);
        viewport.getViewport().setBackground(UiTheme.CANVAS);
        viewport.getVerticalScrollBar().setUnitIncrement(24);
        viewport.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(viewport, BorderLayout.CENTER);

        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void removeUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });
        faculties.addActionListener(e -> applyFilters());
        refresh.addActionListener(e -> loadEvents());

        showMessage("Looking for what's happening?", "Campus events will appear here shortly.", null);
        SwingUtilities.invokeLater(this::loadEvents);
    }

    private JPanel buildHero() {
        JPanel hero = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(0x1B, 0x37, 0x5C));
                g2.fill(new Ellipse2D.Double(w - 235, -145, 340, 340));
                g2.setColor(new Color(0x2E, 0x65, 0xED));
                g2.fill(new Ellipse2D.Double(w - 128, h - 73, 195, 195));
                g2.setColor(new Color(0xEF, 0x6A, 0x65));
                g2.fillRoundRect(w - 112, 29, 40, 8, 8, 8);
                g2.dispose();
            }
        };
        hero.setBackground(UiTheme.NAVY);
        hero.setBorder(new EmptyBorder(18, 24, 19, 24));
        hero.setPreferredSize(new Dimension(0, 151));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 151));
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel kicker = new JLabel("CAMPUS LIFE  /  DISCOVER");
        kicker.setForeground(new Color(0xB4, 0xCC, 0xF5));
        kicker.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        kicker.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel heading = new JLabel("There's more to campus.");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextArea description = UiText.wrapping("Find events worth showing up for.", 1);
        description.setFont(UiTheme.body());
        description.setForeground(new Color(0xDA, 0xE5, 0xF4));
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(kicker);
        text.add(Box.createVerticalStrut(12));
        text.add(heading);
        text.add(Box.createVerticalStrut(7));
        text.add(description);
        hero.add(text, BorderLayout.CENTER);
        return hero;
    }

    private JPanel buildControls() {
        JPanel control = new JPanel();
        control.setOpaque(false);
        control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));
        control.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel searchRow = new JPanel(new BorderLayout(12, 0));
        searchRow.setOpaque(false);
        searchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        JLabel searchLabel = new JLabel("Search");
        searchLabel.setFont(UiTheme.label());
        searchLabel.setForeground(UiTheme.INK);
        search.setToolTipText("Search event titles, faculties, venues and organisers");
        search.getAccessibleContext().setAccessibleName("Search campus events");
        search.putClientProperty("JTextField.placeholderText", "Search events, venues, faculties…");
        search.setFont(UiTheme.body());
        search.setPreferredSize(new Dimension(240, 40));
        refresh.setToolTipText("Reload events from the server");
        searchRow.add(searchLabel, BorderLayout.WEST);
        searchRow.add(search, BorderLayout.CENTER);
        searchRow.add(refresh, BorderLayout.EAST);

        JPanel filterRow = new JPanel(new BorderLayout(8, 0));
        filterRow.setOpaque(false);
        filterRow.setBorder(new EmptyBorder(12, 0, 0, 0));
        filterRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        left.setOpaque(false);
        JLabel facultyLabel = new JLabel("FACULTY");
        facultyLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        facultyLabel.setForeground(UiTheme.MUTED);
        faculties.setFont(UiTheme.body());
        faculties.setPreferredSize(new Dimension(206, 32));
        faculties.setMaximumRowCount(12);
        faculties.getAccessibleContext().setAccessibleName("Filter by faculty");
        left.add(facultyLabel);
        left.add(faculties);
        resultCount.setFont(UiTheme.small());
        resultCount.setForeground(UiTheme.MUTED);
        filterRow.add(left, BorderLayout.WEST);
        filterRow.add(resultCount, BorderLayout.EAST);
        control.add(searchRow);
        control.add(filterRow);
        return control;
    }

    private JPanel eventCard(EventResponseDTO event) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UiTheme.WHITE);
        card.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER));
        card.add(new EventArtwork(event), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(14, 15, 4, 15));

        String titleText = clean(event.getTitle(), "Untitled event");
        JTextArea title = UiText.wrapping(titleText, 3);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        title.setForeground(UiTheme.INK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setToolTipText(titleText);

        String faculty = clean(event.getFacultyName(), "Campus-wide event");
        JTextArea category = UiText.wrapping(faculty.toUpperCase(Locale.ROOT), 2);
        category.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        category.setForeground(UiTheme.BLUE);
        category.setAlignmentX(Component.LEFT_ALIGNMENT);
        category.setToolTipText(faculty);

        String venueName = clean(event.getVenueName(), "Venue to be announced");
        JTextArea venue = UiText.wrapping(venueName, 2);
        venue.setFont(UiTheme.small());
        venue.setForeground(UiTheme.MUTED);
        venue.setToolTipText(venueName);
        venue.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(category);
        body.add(Box.createVerticalStrut(6));
        body.add(title);
        body.add(Box.createVerticalStrut(7));
        body.add(venue);
        card.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(8, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(9, 15, 13, 15));
        String availability = spotsLeft(event);
        JLabel pill = UiComponents.statusPill("Full".equals(availability) ? "Full" : availability);
        pill.setForeground("Full".equals(availability) ? UiTheme.DANGER : UiTheme.SUCCESS);
        pill.setBackground("Full".equals(availability) ? new Color(0xFC, 0xEC, 0xEE) :
                new Color(0xE5, 0xF5, 0xEF));
        footer.add(pill, BorderLayout.WEST);
        JButton view = UiTheme.primaryButton("View event");
        view.setToolTipText("View " + titleText);
        view.addActionListener(e -> { if (onView != null) onView.accept(event); });
        footer.add(view, BorderLayout.EAST);
        card.add(footer, BorderLayout.SOUTH);
        return card;
    }

    private void applyFilters() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::applyFilters);
            return;
        }
        if (loading) return;
        grid.removeAll();
        String query = search.getText().strip().toLowerCase(Locale.ROOT);
        String faculty = (String) faculties.getSelectedItem();
        int count = 0;
        for (EventResponseDTO event : allEvents) {
            if (faculty != null && !ALL_FACULTIES.equals(faculty)
                    && !faculty.equals(event.getFacultyName())) continue;
            String searchable = String.join(" ", clean(event.getTitle(), ""),
                    clean(event.getVenueName(), ""), clean(event.getFacultyName(), ""),
                    clean(event.getOrganiserName(), "")).toLowerCase(Locale.ROOT);
            if (!searchable.contains(query)) continue;
            grid.add(eventCard(event));
            count++;
        }
        resultCount.setText(count + (count == 1 ? " event" : " events") + " to explore");
        if (count == 0) {
            boolean filtered = !query.isBlank() || (faculty != null && !ALL_FACULTIES.equals(faculty));
            JButton clear = filtered ? UiTheme.secondaryButton("Clear filters") : null;
            if (clear != null) clear.addActionListener(e -> {
                search.setText("");
                faculties.setSelectedIndex(0);
            });
            showMessage(filtered ? "No matching events" : "Nothing on the calendar yet",
                    filtered ? "Try another search or clear your faculty filter."
                             : "Check back later for new campus events.", clear);
        } else {
            grid.revalidate();
            grid.repaint();
        }
    }

    private void showMessage(String heading, String detail, JButton action) {
        grid.removeAll();
        grid.add(UiComponents.emptyState(heading, detail, action));
        grid.revalidate();
        grid.repaint();
    }

    private void loadEvents() {
        if (loading) return;
        loading = true;
        refresh.setEnabled(false);
        resultCount.setText("Loading events…");
        new SwingWorker<LoadResult, Void>() {
            @Override protected LoadResult doInBackground() throws Exception {
                List<FacultyResponseDTO> categories = List.of();
                try {
                    HttpResponse<String> facultyResponse = get("/faculty");
                    if (facultyResponse.statusCode() / 100 == 2) {
                        categories = MAPPER.readValue(facultyResponse.body(),
                                new TypeReference<List<FacultyResponseDTO>>() {});
                    }
                } catch (Exception ignored) {
                    // Event browsing remains available even if the faculty service fails.
                }
                HttpResponse<String> eventResponse = get("/event");
                if (eventResponse.statusCode() / 100 != 2)
                    throw new IllegalStateException("HTTP " + eventResponse.statusCode());
                List<EventResponseDTO> events = MAPPER.readValue(eventResponse.body(),
                        new TypeReference<List<EventResponseDTO>>() {});
                return new LoadResult(categories, events);
            }

            @Override protected void done() {
                loading = false;
                refresh.setEnabled(true);
                try {
                    LoadResult response = get();
                    String previous = (String) faculties.getSelectedItem();
                    if (!response.faculties().isEmpty()) {
                        faculties.removeAllItems();
                        faculties.addItem(ALL_FACULTIES);
                        for (FacultyResponseDTO faculty : response.faculties()) {
                            if (faculty.getName() != null && !faculty.getName().isBlank()) {
                                faculties.addItem(faculty.getName());
                            }
                        }
                        if (previous != null) {
                            faculties.setSelectedItem(previous);
                            if (faculties.getSelectedIndex() == -1) faculties.setSelectedIndex(0);
                        }
                    }
                    allEvents.clear();
                    for (EventResponseDTO event : response.events()) {
                        if (event != null && !Boolean.FALSE.equals(event.getOpen())) {
                            allEvents.add(event);
                        }
                    }
                    applyFilters();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    showLoadError();
                } catch (ExecutionException ex) {
                    showLoadError();
                }
            }
        }.execute();
    }

    private void showLoadError() {
        resultCount.setText("Unable to load");
        JButton retry = UiTheme.primaryButton("Try again");
        retry.addActionListener(e -> loadEvents());
        showMessage("Campus events are unavailable",
                "Check your connection to the campus service and try again.", retry);
    }

    private static HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10)).GET().build();
        return HTTP.send(request, HttpResponse.BodyHandlers.ofString());
    }

    static String spotsLeft(EventResponseDTO event) {
        if (event.getCapacity() == null || event.getCapacity() <= 0) return "Open";
        int taken = event.getTicketsSold() == null ? 0 : event.getTicketsSold();
        int left = event.getCapacity() - taken;
        return left <= 0 ? "Full" : left + " left";
    }

    static String dateOnly(String raw) {
        if (raw == null) return "";
        int t = raw.indexOf('T');
        return t < 0 ? raw : raw.substring(0, t);
    }

    private static String clean(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.strip();
    }

    private record LoadResult(List<FacultyResponseDTO> faculties, List<EventResponseDTO> events) { }

    /** Decorative abstract cover: no fake photos, event data or external assets. */
    private static class EventArtwork extends JPanel {
        private static final Color[] PALETTE = {
                new Color(0x23, 0x4D, 0x8A), new Color(0x1D, 0x63, 0x73),
                new Color(0x8B, 0x44, 0x5F), new Color(0x6B, 0x55, 0x96)
        };
        private final EventResponseDTO event;
        EventArtwork(EventResponseDTO event) {
            this.event = event;
            setPreferredSize(new Dimension(0, 108));
            setOpaque(true);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            long key = event.getId() == null ? 0 : event.getId();
            Color tone = PALETTE[(int) Math.floorMod(key, PALETTE.length)];
            int w = getWidth(), h = getHeight();
            g2.setColor(tone);
            g2.fillRect(0, 0, w, h);
            g2.setColor(new Color(255, 255, 255, 28));
            g2.fillOval(w - 130, -87, 240, 240);
            g2.setColor(new Color(255, 255, 255, 36));
            g2.fillOval(w - 70, 20, 150, 150);
            g2.setColor(new Color(255, 255, 255, 190));
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
            g2.drawString("CAMPUS / EVENTS", 16, 23);
            String raw = dateOnly(event.getEventDate());
            try {
                LocalDate date = LocalDate.parse(raw);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 37));
                g2.drawString(String.format(Locale.ENGLISH, "%02d", date.getDayOfMonth()), 16, 77);
                g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
                g2.drawString(MONTH.format(date).toUpperCase(Locale.ENGLISH), 77, 73);
                g2.drawString(Integer.toString(date.getYear()), 77, 89);
            } catch (DateTimeParseException ignored) {
                g2.setColor(Color.WHITE);
                g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 21));
                g2.drawString("WHAT'S ON", 16, 75);
            }
            g2.dispose();
        }
    }
}
