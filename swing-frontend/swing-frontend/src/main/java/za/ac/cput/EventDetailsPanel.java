package za.ac.cput;

import javax.swing.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.DTO.EventResponseDTO;
import za.ac.cput.DTO.TicketRequestDTO;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EventDetailsPanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private final Long studentId;
    private final Runnable onBack;

    private EventResponseDTO event;

    private JLabel lblTitle;
    private JLabel lblFacultyBadge;
    private JTextArea txtDescription;
    private JLabel lblVenue;
    private JLabel lblDate;
    private JProgressBar capacityBar;
    private JLabel lblSpots;
    private JButton btnTicket;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);
    private static final Color BADGE_BG = new Color(235, 242, 250);

    public EventDetailsPanel(Long studentId, Runnable onBack) {
        this.studentId = studentId;
        this.onBack = onBack;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> { if (onBack != null) onBack.run(); });

        lblTitle = new JLabel("Event");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(CPUT_BLUE);

        lblFacultyBadge = new JLabel();
        lblFacultyBadge.setFont(new Font("Arial", Font.BOLD, 12));
        lblFacultyBadge.setForeground(CPUT_BLUE);
        lblFacultyBadge.setOpaque(true);
        lblFacultyBadge.setBackground(BADGE_BG);
        lblFacultyBadge.setBorder(new EmptyBorder(4, 10, 4, 10));

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titleRow.setBackground(Color.WHITE);
        titleRow.add(lblTitle);
        titleRow.add(lblFacultyBadge);

        top.add(backButton, BorderLayout.WEST);
        top.add(titleRow, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(16, 0, 0, 0));

        txtDescription = new JTextArea(4, 60);
        txtDescription.setEditable(false);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDescription.setBackground(Color.WHITE);

        lblVenue = new JLabel();
        lblDate = new JLabel();
        lblVenue.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDate.setFont(new Font("Arial", Font.PLAIN, 14));

        capacityBar = new JProgressBar(0, 100);
        capacityBar.setStringPainted(true);
        capacityBar.setMaximumSize(new Dimension(400, 24));

        lblSpots = new JLabel();
        lblSpots.setFont(new Font("Arial", Font.BOLD, 14));
        lblSpots.setForeground(CPUT_BLUE);

        btnTicket = new JButton("Get Ticket");
        btnTicket.setBackground(CPUT_RED);
        btnTicket.setForeground(Color.WHITE);
        btnTicket.setFocusPainted(false);
        btnTicket.setFont(new Font("Arial", Font.BOLD, 14));
        btnTicket.addActionListener(e -> handleGetTicket());

        body.add(new JScrollPane(txtDescription));
        body.add(Box.createVerticalStrut(12));
        body.add(lblVenue);
        body.add(Box.createVerticalStrut(6));
        body.add(lblDate);
        body.add(Box.createVerticalStrut(12));
        body.add(capacityBar);
        body.add(Box.createVerticalStrut(6));
        body.add(lblSpots);
        body.add(Box.createVerticalStrut(16));
        body.add(btnTicket);
        add(body, BorderLayout.CENTER);
    }

    public void setEvent(EventResponseDTO event) {
        this.event = event;
        if (event == null) return;
        lblTitle.setText(event.getTitle() == null ? "Event" : event.getTitle());
        lblFacultyBadge.setText(event.getFacultyName() == null ? "" : event.getFacultyName());
        txtDescription.setText(event.getDescription() == null ? "" : event.getDescription());
        txtDescription.setCaretPosition(0);
        lblVenue.setText("Venue: " + (event.getVenueName() == null ? "—" : event.getVenueName()));
        lblDate.setText("Date: " + BrowseEventsPanel.dateOnly(event.getEventDate()));
        int sold = event.getTicketsSold() == null ? 0 : event.getTicketsSold();
        if (event.getCapacity() == null || event.getCapacity() <= 0) {
            capacityBar.setIndeterminate(true);
            capacityBar.setString("Open capacity");
            lblSpots.setText(BrowseEventsPanel.spotsLeft(event));
            btnTicket.setEnabled(true);
        } else {
            capacityBar.setIndeterminate(false);
            capacityBar.setMaximum(event.getCapacity());
            capacityBar.setValue(Math.min(sold, event.getCapacity()));
            capacityBar.setString(sold + " / " + event.getCapacity() + " taken");
            lblSpots.setText(BrowseEventsPanel.spotsLeft(event));
            btnTicket.setEnabled(sold < event.getCapacity());
        }
    }

    private void handleGetTicket() {
        if (event == null || event.getId() == null) return;
        if (studentId == null) {
            showError("Sign in as a student to get a ticket.");
            return;
        }
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setEventId(event.getId());
        dto.setPrice(0.0);
        try {
            String json = MAPPER.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ticket?studentId=" + studentId))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
            JOptionPane.showMessageDialog(this,
                    "Ticket issued for '" + event.getTitle() + "'.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Could not get ticket: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                this, message == null || message.isBlank() ? "Request failed" : message,
                "Student request failed", JOptionPane.ERROR_MESSAGE));
    }
}
