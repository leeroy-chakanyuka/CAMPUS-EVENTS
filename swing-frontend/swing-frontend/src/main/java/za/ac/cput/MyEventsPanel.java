package za.ac.cput;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.DTO.EventRequestDTO;
import za.ac.cput.DTO.EventResponseDTO;
import za.ac.cput.DTO.VenueResponseDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MyEventsPanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private final Long organiserId;
    private final boolean readOnly;
    private final List<EventData> events = new ArrayList<>();
    private final List<VenueResponseDTO> venues = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;
    private CardLayout cardLayout;
    private JPanel cardHolder;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<VenueResponseDTO> venueComboBox;
    private JTextField dateField;
    private JTextField capacityField;
    private int editingRow = -1;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public MyEventsPanel() {
        this(null, false);
    }

    public MyEventsPanel(Long organiserId) {
        this(organiserId, false);
    }

    public MyEventsPanel(Long organiserId, boolean readOnly) {
        this.organiserId = organiserId;
        this.readOnly = readOnly;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        cardLayout = new CardLayout();
        cardHolder = new JPanel(cardLayout);
        cardHolder.setBackground(Color.WHITE);
        cardHolder.add(buildListCard(), "list");
        cardHolder.add(buildFormCard(), "form");
        add(cardHolder, BorderLayout.CENTER);
        cardLayout.show(cardHolder, "list");

        if (organiserId != null) {
            loadVenuesAndEvents();
        }
    }

    private JPanel buildListCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        JLabel title = new JLabel("My Events");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        JButton createButton = new JButton("+ Create Event");
        createButton.setBackground(CPUT_RED);
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        createButton.addActionListener(e -> openForm(-1));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        if (!readOnly) {
            header.add(createButton, BorderLayout.EAST);
        }
        card.add(header, BorderLayout.NORTH);
        card.add(buildTable(), BorderLayout.CENTER);
        return card;
    }

    private JScrollPane buildTable() {
        String[] columns = readOnly
                ? new String[]{"Title", "Venue", "Date", "Capacity", "Status"}
                : new String[]{"Title", "Venue", "Date", "Capacity", "Status", "Edit", "Close"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (readOnly) return false;
                return column == 5 || column == 6;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setForeground(DARK_TEXT);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(LIGHT_BLUE);
        table.setSelectionForeground(DARK_TEXT);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(CPUT_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        if (!readOnly) {
            new TableButtonColumn(table, 5, row -> openForm(row));
            new TableButtonColumn(table, 6, this::handleClose, CPUT_RED, "Closed");
        }
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusBadge());
        return scrollPane;
    }

    private JPanel buildFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Create Event");
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(CPUT_BLUE);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        heading.setName("formHeading");

        titleField = new JTextField();
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        venueComboBox = new JComboBox<>();
        dateField = new JTextField();
        dateField.setToolTipText("Example: 2026-10-15");
        capacityField = new JTextField();

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBackground(Color.WHITE);
        fields.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        addField(fields, gbc, 0, "Title:", titleField);
        addField(fields, gbc, 1, "Description:", new JScrollPane(descriptionArea));
        addField(fields, gbc, 2, "Venue:", venueComboBox);
        addField(fields, gbc, 3, "Date:", dateField);
        addField(fields, gbc, 4, "Capacity:", capacityField);

        JButton saveButton = new JButton("Create Event");
        saveButton.setName("saveButton");
        saveButton.setBackground(CPUT_RED);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(e -> submitEvent());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> cardLayout.show(cardHolder, "list"));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBackground(Color.WHITE);
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttons.add(saveButton);
        buttons.add(cancelButton);

        card.add(heading);
        card.add(Box.createVerticalStrut(16));
        card.add(fields);
        card.add(buttons);
        return card;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, Component component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.2;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(component, gbc);
    }

    private void openForm(int row) {
        if (readOnly) return;
        editingRow = row;
        JPanel form = (JPanel) cardHolder.getComponent(1);
        JLabel heading = null;
        JButton saveButton = null;
        for (Component c : form.getComponents()) {
            if ("formHeading".equals(c.getName())) heading = (JLabel) c;
            if (c instanceof JPanel) {
                for (Component nested : ((JPanel) c).getComponents()) {
                    if ("saveButton".equals(nested.getName())) saveButton = (JButton) nested;
                }
            }
        }

        if (row == -1) {
            titleField.setText("");
            descriptionArea.setText("");
            if (venueComboBox.getItemCount() > 0) venueComboBox.setSelectedIndex(0);
            dateField.setText("");
            capacityField.setText("");
            if (heading != null) heading.setText("Create Event");
            if (saveButton != null) saveButton.setText("Create Event");
        } else {
            EventData event = events.get(row);
            titleField.setText(event.title);
            descriptionArea.setText(event.description == null ? "" : event.description);
            selectVenue(event.venueId, event.venueName);
            dateField.setText(displayDate(event.eventDate));
            capacityField.setText(String.valueOf(event.capacity));
            if (heading != null) heading.setText("Edit Event");
            if (saveButton != null) saveButton.setText("Save Changes");
        }
        cardLayout.show(cardHolder, "form");
    }

    private void submitEvent() {
        if (readOnly) return;
        if (organiserId == null) {
            showError("No organiser account is available.");
            return;
        }

        String title = titleField.getText().trim();
        String date = dateField.getText().trim();
        String capacityText = capacityField.getText().trim();
        if (title.isEmpty() || date.isEmpty() || capacityText.isEmpty()) return;

        VenueResponseDTO venue = (VenueResponseDTO) venueComboBox.getSelectedItem();
        if (venue == null) {
            showError("Please select a venue.");
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException ex) {
            showError("Capacity must be a number.");
            return;
        }

        EventRequestDTO dto = new EventRequestDTO();
        dto.setTitle(title);
        dto.setDescription(descriptionArea.getText().trim());
        dto.setEventDate(toDateTime(date));
        dto.setCapacity(capacity);
        dto.setVenueId(venue.getId());
        dto.setOrganiserId(organiserId);

        try {
            String json = MAPPER.writeValueAsString(dto);
            HttpResponse<String> response;
            if (editingRow == -1) {
                response = send("POST", BASE_URL + "/event", json);
            } else {
                response = send("PUT", BASE_URL + "/event/" + events.get(editingRow).id, json);
            }

            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }

            EventResponseDTO saved = MAPPER.readValue(response.body(), EventResponseDTO.class);
            if (editingRow == -1) {
                events.add(fromResponse(saved));
            } else {
                events.set(editingRow, fromResponse(saved));
            }
            refreshTable();
            cardLayout.show(cardHolder, "list");
        } catch (Exception ex) {
            showError("Could not save event: " + ex.getMessage());
        }
    }

    private void handleClose(int row) {
        if (readOnly) return;
        if (organiserId == null || row < 0 || row >= events.size()) return;
        EventData event = events.get(row);
        if (!event.open) return;

        try {
            HttpResponse<String> response = send(
                    "PUT",
                    BASE_URL + "/event/" + event.id + "/close?organiserId=" + organiserId,
                    ""
            );
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
            event.open = false;
            refreshTable();
        } catch (Exception ex) {
            showError("Could not close event: " + ex.getMessage());
        }
    }

    private void loadVenuesAndEvents() {
        SwingUtilities.invokeLater(() -> {
            try {
                HttpResponse<String> venueResponse = send("GET", BASE_URL + "/venue", "");
                if (venueResponse.statusCode() / 100 != 2) {
                    showError(venueResponse.body());
                    return;
                }
                venues.clear();
                venues.addAll(MAPPER.readValue(venueResponse.body(), new TypeReference<List<VenueResponseDTO>>() {}));
                venueComboBox.removeAllItems();
                for (VenueResponseDTO venue : venues) venueComboBox.addItem(venue);

                HttpResponse<String> eventResponse = send("GET", BASE_URL + "/event/organiser/" + organiserId, "");
                if (eventResponse.statusCode() / 100 != 2) {
                    showError(eventResponse.body());
                    return;
                }
                events.clear();
                List<EventResponseDTO> responseEvents = MAPPER.readValue(
                        eventResponse.body(), new TypeReference<List<EventResponseDTO>>() {});
                for (EventResponseDTO event : responseEvents) events.add(fromResponse(event));
                refreshTable();
            } catch (Exception ex) {
                showError("Could not load organiser data: " + ex.getMessage());
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (EventData event : events) {
            if (readOnly) {
                tableModel.addRow(new Object[]{
                        event.title,
                        event.venueName,
                        displayDate(event.eventDate),
                        event.capacity,
                        event.open ? "Open" : "Closed"
                });
            } else {
                tableModel.addRow(new Object[]{
                        event.title,
                        event.venueName,
                        displayDate(event.eventDate),
                        event.capacity,
                        event.open ? "Open" : "Closed",
                        "Edit",
                        event.open ? "Close registration" : "Closed"
                });
            }
        }
    }

    private EventData fromResponse(EventResponseDTO dto) {
        EventData event = new EventData();
        event.id = dto.getId();
        event.title = dto.getTitle();
        event.description = dto.getDescription();
        event.eventDate = dto.getEventDate();
        event.capacity = dto.getCapacity();
        event.open = Boolean.TRUE.equals(dto.getOpen());
        event.venueId = dto.getVenueId();
        event.venueName = dto.getVenueName();
        return event;
    }

    private void selectVenue(Long id, String name) {
        for (int i = 0; i < venueComboBox.getItemCount(); i++) {
            VenueResponseDTO venue = venueComboBox.getItemAt(i);
            if ((id != null && id.equals(venue.getId())) || (id == null && name != null && name.equals(venue.getName()))) {
                venueComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private static String toDateTime(String date) {
        return date.length() == 10 ? date + "T00:00:00" : date;
    }

    private static String displayDate(String dateTime) {
        if (dateTime == null) return "";
        return dateTime.length() >= 10 ? dateTime.substring(0, 10) : dateTime;
    }

    private HttpResponse<String> send(String method, String url, String body) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url));
        if ("GET".equals(method)) {
            builder.GET();
        } else if ("POST".equals(method)) {
            builder.header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body));
        } else if ("PUT".equals(method)) {
            builder.header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body));
        }
        return HTTP.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                this, message == null || message.isBlank() ? "Request failed" : message,
                "Organiser request failed", JOptionPane.ERROR_MESSAGE));
    }

    private static class EventData {
        Long id;
        String title;
        String description;
        String eventDate;
        Integer capacity;
        boolean open;
        Long venueId;
        String venueName;
    }
}