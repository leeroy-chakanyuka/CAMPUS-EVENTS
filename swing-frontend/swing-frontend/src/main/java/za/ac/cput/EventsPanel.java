package za.ac.cput;

import javax.swing.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatLightLaf;
import za.ac.cput.DTO.EventResponseDTO;
import za.ac.cput.DTO.StatusUpdateRequestDTO;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class EventsPanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private final Long adminId;
    private final List<Long> eventIds = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public EventsPanel() {
        this(null);
    }

    public EventsPanel(Long adminId) {
        this.adminId = adminId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::loadEvents);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Events (cross-faculty)");
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

    private JScrollPane buildTable() {
        String[] columns = {"Title", "Faculty", "Organiser", "Date", "Status", "Action"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Action column only
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

        // Force cancel is terminal: after committing, the action becomes
        // static non-clickable "Cancelled" text
        new TableButtonColumn(table, 5, this::handleForceCancel, new Color(190, 30, 45), "Cancelled");
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusBadge());

        return scrollPane;
    }

    private void handleForceCancel(int row) {
        if (row < 0 || row >= eventIds.size()) return;
        String currentStatus = tableModel.getValueAt(row, 4).toString();
        if (currentStatus.equals("Cancelled")) {
            return;
        }
        if (adminId == null) {
            showError("Sign in as an admin to cancel events.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Force-cancel '" + tableModel.getValueAt(row, 0) + "'? This cannot be undone.",
                "Confirm force cancel", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        StatusUpdateRequestDTO dto = new StatusUpdateRequestDTO();
        dto.setActive(false);
        dto.setRequestingAdminId(adminId);
        try {
            String json = MAPPER.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/event/" + eventIds.get(row) + "/force-cancel"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
        } catch (Exception ex) {
            showError("Could not cancel event: " + ex.getMessage());
            return;
        }
        tableModel.setValueAt("Cancelled", row, 4);
        tableModel.setValueAt("Cancelled", row, 5);
        table.repaint();
    }

    private void loadEvents() {
        tableModel.setRowCount(0);
        eventIds.clear();
        try {
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
                eventIds.add(event.getId());
                boolean open = !Boolean.FALSE.equals(event.getOpen());
                tableModel.addRow(new Object[]{
                        event.getTitle(),
                        event.getFacultyName(),
                        event.getOrganiserName(),
                        event.getEventDate(),
                        open ? "Open" : "Cancelled",
                        open ? "Force cancel" : "Cancelled"
                });
            }
        } catch (Exception ex) {
            showError("Could not load events: " + ex.getMessage());
        }
        table.repaint();
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                this, message == null || message.isBlank() ? "Request failed" : message,
                "Admin request failed", JOptionPane.ERROR_MESSAGE));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("EventsPanel - standalone test");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new EventsPanel());
            frame.setVisible(true);
        });
    }
}
