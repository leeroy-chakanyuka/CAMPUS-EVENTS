package za.ac.cput;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MyEventsPanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";

    private JTable table;
    private DefaultTableModel tableModel;

    private CardLayout cardLayout;
    private JPanel cardHolder;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<VenueItem> venueComboBox;
    private JTextField dateField;
    private JTextField capacityField;

    private int editingRow = -1;
    private Long editingEventId = null;

    private Long organiserId;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public MyEventsPanel(Long organiserId) {
        this.organiserId = organiserId;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        cardLayout = new CardLayout();
        cardHolder = new JPanel(cardLayout);
        cardHolder.setBackground(Color.WHITE);

        cardHolder.add(buildListCard(), "list");
        cardHolder.add(buildFormCard(), "form");

        add(cardHolder, BorderLayout.CENTER);

        loadVenues();
        loadEvents();

        cardLayout.show(cardHolder, "list");
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
        header.add(createButton, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);
        card.add(buildTable(), BorderLayout.CENTER);

        return card;
    }

    private JScrollPane buildTable() {

        String[] columns = {
                "ID",
                "Title",
                "Description",
                "Venue",
                "Date",
                "Capacity",
                "Status",
                "Edit",
                "Close"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7 || column == 8;
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

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        table.getTableHeader().setBackground(CPUT_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        new TableButtonColumn(
                table,
                7,
                row -> openForm(row)
        );

        new TableButtonColumn(
                table,
                8,
                this::handleClose,
                CPUT_RED,
                "Closed"
        );

        table.getColumnModel()
                .getColumn(6)
                .setCellRenderer(new StatusBadge());

        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);

        // Hide description column
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(0);

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

        gbc.gridx = 0;
        gbc.gridy = 0;

        fields.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        fields.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        fields.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;

        fields.add(
                new JScrollPane(descriptionArea),
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy++;

        fields.add(new JLabel("Venue:"), gbc);

        gbc.gridx = 1;

        fields.add(venueComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        fields.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;

        fields.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        fields.add(new JLabel("Capacity:"), gbc);

        gbc.gridx = 1;

        fields.add(capacityField, gbc);

        JButton saveButton = new JButton("Create Event");

        saveButton.setName("saveButton");
        saveButton.setBackground(CPUT_RED);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));

        saveButton.addActionListener(e -> submitEvent());

        JButton cancelButton = new JButton("Cancel");

        cancelButton.setFocusPainted(false);

        cancelButton.addActionListener(
                e -> cardLayout.show(cardHolder, "list")
        );

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

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

    private void openForm(int row) {

        editingRow = row;

        Component[] comps =
                ((JPanel) cardHolder.getComponent(1))
                        .getComponents();

        JLabel heading = null;
        JButton saveButton = null;

        for (Component c : comps) {

            if ("formHeading".equals(c.getName())) {
                heading = (JLabel) c;
            }

            if ("saveButton".equals(c.getName())) {
                saveButton = (JButton) c;
            }
        }

        if (row == -1) {

            editingEventId = null;

            titleField.setText("");
            descriptionArea.setText("");
            venueComboBox.setSelectedIndex(
                    venueComboBox.getItemCount() > 0 ? 0 : -1
            );
            dateField.setText("");
            capacityField.setText("");

            if (heading != null) {
                heading.setText("Create Event");
            }

            if (saveButton != null) {
                saveButton.setText("Create Event");
            }

        } else {

            editingEventId =
                    Long.valueOf(
                            tableModel.getValueAt(row, 0).toString()
                    );

            titleField.setText(
                    tableModel.getValueAt(row, 1).toString()
            );

            descriptionArea.setText(
                    tableModel.getValueAt(row, 2).toString()
            );

            String venueName =
                    tableModel.getValueAt(row, 3).toString();

            selectVenue(venueName);

            dateField.setText(
                    tableModel.getValueAt(row, 4).toString()
            );

            capacityField.setText(
                    tableModel.getValueAt(row, 5).toString()
            );

            if (heading != null) {
                heading.setText("Edit Event");
            }

            if (saveButton != null) {
                saveButton.setText("Save Changes");
            }
        }

        cardLayout.show(cardHolder, "form");
    }

    private void submitEvent() {

        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String date = dateField.getText().trim();
        String capacityText = capacityField.getText().trim();

        if (title.isEmpty()
                || date.isEmpty()
                || capacityText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete the required fields."
            );

            return;
        }

        if (venueComboBox.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a venue."
            );

            return;
        }

        int capacity;

        try {
            capacity = Integer.parseInt(capacityText);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Capacity must be a number."
            );

            return;
        }

        LocalDate eventDate;

        try {

            eventDate = LocalDate.parse(date);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Date must use yyyy-MM-dd."
            );

            return;
        }

        VenueItem selectedVenue =
                (VenueItem) venueComboBox.getSelectedItem();

        String json =
                "{"
                        + "\"title\":\"" + escapeJson(title) + "\","
                        + "\"description\":\"" + escapeJson(description) + "\","
                        + "\"eventDate\":\""
                        + eventDate
                        + "T00:00:00\","
                        + "\"capacity\":"
                        + capacity
                        + ","
                        + "\"venueId\":"
                        + selectedVenue.getId()
                        + ","
                        + "\"organiserId\":"
                        + organiserId
                        + "}";

        try {

            if (editingEventId == null) {

                sendRequest(
                        "POST",
                        "/event",
                        json
                );

            } else {

                sendRequest(
                        "PUT",
                        "/event/" + editingEventId,
                        json
                );
            }

            JOptionPane.showMessageDialog(
                    this,
                    editingEventId == null
                            ? "Event created successfully."
                            : "Event updated successfully."
            );

            editingEventId = null;
            editingRow = -1;

            cardLayout.show(cardHolder, "list");

            loadEvents();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not save event:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleClose(int row) {

        String status =
                tableModel.getValueAt(row, 6).toString();

        if ("Closed".equalsIgnoreCase(status)) {
            return;
        }

        Long eventId =
                Long.valueOf(
                        tableModel.getValueAt(row, 0).toString()
                );

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Close registration for this event?",
                "Close Event",
                JOptionPane.YES_NO_OPTION
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            sendRequest(
                    "PUT",
                    "/event/" + eventId
                            + "/close?organiserId="
                            + organiserId,
                    null
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Event registration closed."
            );

            loadEvents();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not close event:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadEvents() {

        if (organiserId == null) {
            return;
        }

        try {

            String response =
                    sendRequest(
                            "GET",
                            "/event/organiser/"
                                    + organiserId,
                            null
                    );

            List<EventResponse> events =
                    objectMapper.readValue(
                            response,
                            new TypeReference<List<EventResponse>>() {
                            }
                    );

            tableModel.setRowCount(0);

            for (EventResponse event : events) {

                String venueName =
                        event.venue != null
                                ? event.venue.name
                                : "";

                String date = "";

                if (event.eventDate != null) {

                    date =
                            event.eventDate.length() >= 10
                                    ? event.eventDate.substring(0, 10)
                                    : event.eventDate;
                }

                String status =
                        Boolean.TRUE.equals(event.open)
                                ? "Open"
                                : "Closed";

                String closeText =
                        Boolean.TRUE.equals(event.open)
                                ? "Close registration"
                                : "Closed";

                tableModel.addRow(
                        new Object[]{
                                event.id,
                                event.title,
                                event.description == null
                                        ? ""
                                        : event.description,
                                venueName,
                                date,
                                event.capacity,
                                status,
                                "Edit",
                                closeText
                        }
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load events:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadVenues() {

        try {

            String response =
                    sendRequest(
                            "GET",
                            "/venue",
                            null
                    );

            List<VenueResponse> venues =
                    objectMapper.readValue(
                            response,
                            new TypeReference<List<VenueResponse>>() {
                            }
                    );

            venueComboBox.removeAllItems();

            for (VenueResponse venue : venues) {

                venueComboBox.addItem(
                        new VenueItem(
                                venue.id,
                                venue.name
                        )
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load venues:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void selectVenue(String venueName) {

        for (int i = 0;
             i < venueComboBox.getItemCount();
             i++) {

            VenueItem item =
                    venueComboBox.getItemAt(i);

            if (item.getName().equals(venueName)) {

                venueComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private String sendRequest(
            String method,
            String path,
            String body) throws Exception {

        HttpRequest.Builder builder =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        BASE_URL + path
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        );

        if ("GET".equalsIgnoreCase(method)) {

            builder.GET();

        } else if ("POST".equalsIgnoreCase(method)) {

            builder.POST(
                    HttpRequest.BodyPublishers.ofString(
                            body == null ? "" : body
                    )
            );

        } else if ("PUT".equalsIgnoreCase(method)) {

            builder.PUT(
                    HttpRequest.BodyPublishers.ofString(
                            body == null ? "" : body
                    )
            );
        }

        HttpResponse<String> response =
                httpClient.send(
                        builder.build(),
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Server returned HTTP "
                            + response.statusCode()
                            + ": "
                            + response.body()
            );
        }

        return response.body();
    }

    private String escapeJson(String value) {

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    public void refreshEvents() {
        loadVenues();
        loadEvents();
    }

    private static class VenueResponse {

        public Long id;
        public String name;
    }

    private static class EventResponse {

        public Long id;
        public String title;
        public String description;
        public String eventDate;
        public Integer capacity;
        public Boolean open;
        public VenueResponse venue;
    }
}
