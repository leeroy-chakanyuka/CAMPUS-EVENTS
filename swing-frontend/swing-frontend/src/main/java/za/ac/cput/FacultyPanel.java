package za.ac.cput;



import javax.swing.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatLightLaf;
import za.ac.cput.DTO.FacultyRequestDTO;
import za.ac.cput.DTO.FacultyResponseDTO;
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

public class FacultyPanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private final Long adminId;
    private final List<Long> facultyIds = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtName;
    private JTextField txtEmail;

    // same palette as MyEvents.java — one visual identity, not five
    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    private static final int LABEL_WIDTH = 140;
    private static final int FIELD_WIDTH = 260;
    private static final int FIELD_HEIGHT = 34;

    public FacultyPanel() {
        this(null);
    }

    public FacultyPanel(Long adminId) {
        this.adminId = adminId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
        add(buildCreateForm(), BorderLayout.SOUTH);

        SwingUtilities.invokeLater(this::loadFaculties);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Faculties");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        JButton refreshButton = new JButton("↻ Refresh");
        refreshButton.setBackground(CPUT_BLUE);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.addActionListener(e -> loadFaculties());

        header.add(title, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Name", "Contact Email", "Status", "Action"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Action column only — needed so the button actually fires
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

        new TableButtonColumn(table, 3, this::handleStatusToggle);
        table.getColumnModel().getColumn(2).setCellRenderer(new StatusBadge());

        return scrollPane;
    }

    private void handleStatusToggle(int row) {
        if (row < 0 || row >= facultyIds.size()) return;
        if (adminId == null) {
            showError("Sign in as an admin to change faculty status.");
            return;
        }
        boolean currentlyActive = tableModel.getValueAt(row, 2).toString().equals("Active");
        StatusUpdateRequestDTO dto = new StatusUpdateRequestDTO();
        dto.setActive(!currentlyActive);
        dto.setRequestingAdminId(adminId);
        try {
            String json = MAPPER.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/faculty/" + facultyIds.get(row) + "/status"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
        } catch (Exception ex) {
            showError("Could not update faculty status: " + ex.getMessage());
            return;
        }
        if (currentlyActive) {
            tableModel.setValueAt("Inactive", row, 2);
            tableModel.setValueAt("Active", row, 3);
        } else {
            tableModel.setValueAt("Active", row, 2);
            tableModel.setValueAt("Deactivate", row, 3);
        }
        table.repaint();
    }

    private JPanel buildCreateForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                new EmptyBorder(16, 0, 0, 0)
        ));

        JLabel formTitle = new JLabel("Create Faculty");
        formTitle.setFont(new Font("Arial", Font.BOLD, 15));
        formTitle.setForeground(CPUT_BLUE);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtName = new JTextField();
        txtEmail = new JTextField();

        JPanel fieldsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        fieldsRow.setBackground(Color.WHITE);
        fieldsRow.add(labeledField("Faculty name", txtName));
        fieldsRow.add(labeledField("Contact email", txtEmail));

        JButton createButton = new JButton("Create Faculty");
        createButton.setBackground(CPUT_RED);
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        createButton.addActionListener(e -> handleCreate());

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonRow.setBackground(Color.WHITE);
        buttonRow.add(createButton);

        form.add(formTitle);
        form.add(Box.createVerticalStrut(10));
        form.add(fieldsRow);
        form.add(buttonRow);
        return form;
    }

    private JPanel labeledField(String labelText, JTextField field) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(new Color(120, 120, 120));

        field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setMaximumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setFont(new Font("Arial", Font.PLAIN, 13));

        wrapper.add(label);
        wrapper.add(Box.createVerticalStrut(4));
        wrapper.add(field);
        return wrapper;
    }

    private void handleCreate() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();

        if (name.isEmpty() || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            JOptionPane.showMessageDialog(this, "Enter a valid faculty name and email.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (adminId == null) {
            showError("Sign in as an admin to create a faculty.");
            return;
        }

        FacultyRequestDTO dto = new FacultyRequestDTO();
        dto.setName(name);
        dto.setContactEmail(email);
        dto.setAdminId(adminId);
        try {
            String json = MAPPER.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/faculty"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
        } catch (Exception ex) {
            showError("Could not create faculty: " + ex.getMessage());
            return;
        }
        txtName.setText("");
        txtEmail.setText("");
        loadFaculties();
    }

    private void loadFaculties() {
        tableModel.setRowCount(0);
        facultyIds.clear();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/faculty"))
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
            List<FacultyResponseDTO> faculties = MAPPER.readValue(
                    response.body(), new TypeReference<List<FacultyResponseDTO>>() {});
            for (FacultyResponseDTO faculty : faculties) {
                facultyIds.add(faculty.getId());
                tableModel.addRow(new Object[]{
                        faculty.getName(),
                        faculty.getContactEmail(),
                        faculty.isActive() ? "Active" : "Inactive",
                        faculty.isActive() ? "Deactivate" : "Active"
                });
            }
        } catch (Exception ex) {
            showError("Could not load faculties: " + ex.getMessage());
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
            JFrame frame = new JFrame("FacultyPanel - standalone test");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new FacultyPanel());
            frame.setVisible(true);
        });
    }
}
