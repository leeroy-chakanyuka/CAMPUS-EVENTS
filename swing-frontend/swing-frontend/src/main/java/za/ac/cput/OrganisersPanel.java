package za.ac.cput;

import javax.swing.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatLightLaf;
import za.ac.cput.DTO.OrganiserResponseDTO;
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

public class OrganisersPanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private final Long adminId;
    private final List<Long> organiserIds = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public OrganisersPanel() {
        this(null);
    }

    public OrganisersPanel(Long adminId) {
        this.adminId = adminId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::loadOrganisers);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Organisers");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        JButton refreshButton = new JButton("↻ Refresh");
        refreshButton.setBackground(CPUT_BLUE);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.addActionListener(e -> loadOrganisers());

        header.add(title, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Name", "Email", "Faculty", "Status", "Action"};

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

        new TableButtonColumn(table, 4, this::handleStatusToggle);
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusBadge());

        return scrollPane;
    }

    private void handleStatusToggle(int row) {
        if (row < 0 || row >= organiserIds.size()) return;
        if (adminId == null) {
            showError("Sign in as an admin to change organiser status.");
            return;
        }
        boolean currentlyActive = tableModel.getValueAt(row, 3).toString().equals("Active");
        StatusUpdateRequestDTO dto = new StatusUpdateRequestDTO();
        dto.setActive(!currentlyActive);
        dto.setRequestingAdminId(adminId);
        try {
            String json = MAPPER.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/organiser/" + organiserIds.get(row) + "/status"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
        } catch (Exception ex) {
            showError("Could not update organiser status: " + ex.getMessage());
            return;
        }
        if (currentlyActive) {
            tableModel.setValueAt("Suspended", row, 3);
            tableModel.setValueAt("Reactivate", row, 4);
        } else {
            tableModel.setValueAt("Active", row, 3);
            tableModel.setValueAt("Suspend", row, 4);
        }
        table.repaint();
    }

    private void loadOrganisers() {
        tableModel.setRowCount(0);
        organiserIds.clear();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/organiser"))
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
            List<OrganiserResponseDTO> organisers = MAPPER.readValue(
                    response.body(), new TypeReference<List<OrganiserResponseDTO>>() {});
            for (OrganiserResponseDTO organiser : organisers) {
                organiserIds.add(organiser.getId());
                String name = (organiser.getFirstName() == null ? "" : organiser.getFirstName())
                        + " " + (organiser.getLastName() == null ? "" : organiser.getLastName());
                tableModel.addRow(new Object[]{
                        name.trim(),
                        organiser.getEmail(),
                        organiser.getFacultyName(),
                        organiser.isActive() ? "Active" : "Suspended",
                        organiser.isActive() ? "Suspend" : "Reactivate"
                });
            }
        } catch (Exception ex) {
            showError("Could not load organisers: " + ex.getMessage());
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
            JFrame frame = new JFrame("OrganisersPanel - standalone test");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new OrganisersPanel());
            frame.setVisible(true);
        });
    }
}
