package za.ac.cput;

import javax.swing.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.DTO.EventResponseDTO;
import za.ac.cput.DTO.StudentResponseDTO;
import za.ac.cput.DTO.TicketResponseDTO;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

public class StudentHomePanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private final Long studentId;

    private JLabel lblWelcome;
    private JLabel valUpcoming;
    private JLabel valTickets;
    private JLabel valFaculty;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CARD_BG = new Color(235, 242, 250);

    public StudentHomePanel(Long studentId) {
        this.studentId = studentId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        lblWelcome = new JLabel("Welcome");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 28));
        lblWelcome.setForeground(CPUT_BLUE);

        JButton refreshButton = new JButton("↻ Refresh");
        refreshButton.setBackground(CPUT_BLUE);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.addActionListener(e -> loadStats());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.add(lblWelcome, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setBackground(Color.WHITE);
        cards.setBorder(new EmptyBorder(24, 0, 0, 0));
        valUpcoming = new JLabel("—", SwingConstants.CENTER);
        valTickets = new JLabel("—", SwingConstants.CENTER);
        valFaculty = new JLabel("—", SwingConstants.CENTER);
        cards.add(statCard("Upcoming events", valUpcoming));
        cards.add(statCard("My tickets", valTickets));
        cards.add(statCard("My faculty", valFaculty));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.WHITE);
        center.add(cards, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::loadStats);
    }

    private JPanel statCard(String label, JLabel value) {
        value.setFont(new Font("Arial", Font.BOLD, 26));
        value.setForeground(CPUT_BLUE);
        JLabel name = new JLabel(label, SwingConstants.CENTER);
        name.setFont(new Font("Arial", Font.PLAIN, 13));
        name.setForeground(new Color(120, 120, 120));
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(24, 12, 24, 12));
        card.add(value, BorderLayout.CENTER);
        card.add(name, BorderLayout.SOUTH);
        return card;
    }

    private void loadStats() {
        String name = "Student";
        String faculty = "—";
        int upcoming = 0;
        int tickets = 0;
        try {
            HttpRequest reqStudents = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/student"))
                    .GET()
                    .build();
            HttpResponse<String> resStudents = HTTP.send(reqStudents, HttpResponse.BodyHandlers.ofString());
            if (resStudents.statusCode() / 100 == 2) {
                List<StudentResponseDTO> students = MAPPER.readValue(
                        resStudents.body(), new TypeReference<List<StudentResponseDTO>>() {});
                for (StudentResponseDTO s : students) {
                    if (studentId != null && studentId.equals(s.getId())) {
                        String full = ((s.getFirstName() == null ? "" : s.getFirstName())
                                + " " + (s.getLastName() == null ? "" : s.getLastName())).trim();
                        if (!full.isBlank()) name = full;
                        if (s.getFacultyName() != null) faculty = s.getFacultyName();
                        break;
                    }
                }
            }

            HttpRequest reqEvents = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/event"))
                    .GET()
                    .build();
            HttpResponse<String> resEvents = HTTP.send(reqEvents, HttpResponse.BodyHandlers.ofString());
            if (resEvents.statusCode() / 100 == 2) {
                List<EventResponseDTO> events = MAPPER.readValue(
                        resEvents.body(), new TypeReference<List<EventResponseDTO>>() {});
                LocalDateTime now = LocalDateTime.now();
                for (EventResponseDTO e : events) {
                    if (Boolean.FALSE.equals(e.getOpen())) continue;
                    try {
                        if (e.getEventDate() != null && LocalDateTime.parse(e.getEventDate()).isBefore(now)) continue;
                    } catch (Exception ignored) {
                    }
                    upcoming++;
                }
            }

            if (studentId != null) {
                HttpRequest reqTickets = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/ticket/student/" + studentId))
                        .GET()
                        .build();
                HttpResponse<String> resTickets = HTTP.send(reqTickets, HttpResponse.BodyHandlers.ofString());
                if (resTickets.statusCode() / 100 == 2) {
                    List<TicketResponseDTO> mine = MAPPER.readValue(
                            resTickets.body(), new TypeReference<List<TicketResponseDTO>>() {});
                    tickets = mine.size();
                }
            }
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this, "Could not load dashboard: " + ex.getMessage(),
                    "Student request failed", JOptionPane.ERROR_MESSAGE));
            return;
        }
        final String fName = name;
        final String fFaculty = faculty;
        final int fUpcoming = upcoming;
        final int fTickets = tickets;
        SwingUtilities.invokeLater(() -> {
            lblWelcome.setText("Welcome, " + fName);
            valUpcoming.setText(String.valueOf(fUpcoming));
            valTickets.setText(String.valueOf(fTickets));
            valFaculty.setText(fFaculty);
        });
    }
}
