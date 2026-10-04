package za.ac.cput;

import javax.swing.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.DTO.TicketResponseDTO;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class StudentTicketsPanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private final Long studentId;
    private final List<Long> ticketIds = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public StudentTicketsPanel(Long studentId) {
        this.studentId = studentId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::loadTickets);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("My Tickets");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        JButton refreshButton = new JButton("↻ Refresh");
        refreshButton.setBackground(CPUT_BLUE);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.addActionListener(e -> loadTickets());

        header.add(title, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Event", "Date", "Venue", "Status", "Action"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Action column only
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

        new TableButtonColumn(table, 4, this::handleCancel);
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusBadge());

        return scrollPane;
    }

    private void handleCancel(int row) {
        if (row < 0 || row >= ticketIds.size()) return;
        if (studentId == null) {
            showError("Sign in as a student to cancel a ticket.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel your ticket for '" + tableModel.getValueAt(row, 0) + "'?",
                "Confirm cancel", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ticket/" + ticketIds.get(row) + "?studentId=" + studentId))
                    .DELETE()
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
        } catch (Exception ex) {
            showError("Could not cancel ticket: " + ex.getMessage());
            return;
        }
        ticketIds.remove(row);
        tableModel.removeRow(row);
        table.repaint();
    }

    private void loadTickets() {
        tableModel.setRowCount(0);
        ticketIds.clear();
        if (studentId == null) return;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ticket/student/" + studentId))
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
            List<TicketResponseDTO> tickets = MAPPER.readValue(
                    response.body(), new TypeReference<List<TicketResponseDTO>>() {});
            for (TicketResponseDTO ticket : tickets) {
                ticketIds.add(ticket.getId());
                tableModel.addRow(new Object[]{
                        ticket.getEventTitle(),
                        BrowseEventsPanel.dateOnly(ticket.getEventDate()),
                        ticket.getVenueName(),
                        "Issued",
                        "Cancel"
                });
            }
        } catch (Exception ex) {
            showError("Could not load tickets: " + ex.getMessage());
        }
        table.repaint();
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                this, message == null || message.isBlank() ? "Request failed" : message,
                "Student request failed", JOptionPane.ERROR_MESSAGE));
    }
}
