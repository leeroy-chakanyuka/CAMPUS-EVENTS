package za.ac.cput;

import javax.swing.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatLightLaf;
import za.ac.cput.DTO.AdminResponseDTO;
import za.ac.cput.DTO.CreateAdminRequestDTO;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AdminsPanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private final Long adminId;
    private final List<Long> adminIds = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JPasswordField pwdTemp;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    private static final int FIELD_WIDTH = 200;
    private static final int FIELD_HEIGHT = 34;

    public AdminsPanel() {
        this(null);
    }

    public AdminsPanel(Long adminId) {
        this.adminId = adminId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
        add(buildCreateForm(), BorderLayout.SOUTH);

        SwingUtilities.invokeLater(this::loadAdmins);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Admins");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        JButton refreshButton = new JButton("↻ Refresh");
        refreshButton.setBackground(CPUT_BLUE);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.addActionListener(e -> loadAdmins());

        header.add(title, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Name", "Email", "Action"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Action column only
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

        new TableButtonColumn(table, 2, this::handleChangePassword);

        return scrollPane;
    }

    private void handleChangePassword(int row) {
        if (row < 0 || row >= adminIds.size()) return;
        if (adminId == null) {
            showError("Sign in as an admin to change a password.");
            return;
        }
        if (!adminId.equals(adminIds.get(row))) {
            JOptionPane.showMessageDialog(this, "You can only change your own password.",
                    "Not allowed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPasswordField currentField = new JPasswordField();
        JPasswordField newField = new JPasswordField();
        Object[] fields = {"Current password:", currentField, "New password:", newField};
        int choice = JOptionPane.showConfirmDialog(this, fields,
                "Change your password", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }
        String current = new String(currentField.getPassword());
        String fresh = new String(newField.getPassword());
        if (current.isBlank() || fresh.isBlank()) {
            showError("Both passwords are required.");
            return;
        }
        try {
            String query = "adminId=" + adminId
                    + "&currentPassword=" + URLEncoder.encode(current, StandardCharsets.UTF_8)
                    + "&newPassword=" + URLEncoder.encode(fresh, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/admin/change-password?" + query))
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
            JOptionPane.showMessageDialog(this, "Password changed.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Could not change password: " + ex.getMessage());
        }
    }

    private JPanel buildCreateForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                new EmptyBorder(16, 0, 0, 0)
        ));

        JLabel formTitle = new JLabel("Create Admin");
        formTitle.setFont(new Font("Arial", Font.BOLD, 15));
        formTitle.setForeground(CPUT_BLUE);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtEmail = new JTextField();
        pwdTemp = new JPasswordField();

        JPanel fieldsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        fieldsRow.setBackground(Color.WHITE);
        fieldsRow.add(labeledField("First name", txtFirstName));
        fieldsRow.add(labeledField("Last name", txtLastName));
        fieldsRow.add(labeledField("Email", txtEmail));
        fieldsRow.add(labeledField("Temporary password", pwdTemp));

        JButton createButton = new JButton("Create Admin");
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
        String first = txtFirstName.getText().trim();
        String last = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String temp = new String(pwdTemp.getPassword());

        if (first.isEmpty() || last.isEmpty() || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            JOptionPane.showMessageDialog(this, "Enter a valid name and email.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (temp.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter a temporary password.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (adminId == null) {
            showError("Sign in as an admin to create an admin.");
            return;
        }

        CreateAdminRequestDTO dto = new CreateAdminRequestDTO();
        dto.setFirstName(first);
        dto.setLastName(last);
        dto.setEmail(email);
        dto.setPassword(temp);
        try {
            String json = MAPPER.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/admin?requestingAdminId=" + adminId))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode body = MAPPER.readTree(response.body());
            if (response.statusCode() / 100 != 2 || !body.path("success").asBoolean(false)) {
                showError(body.path("message").asText(response.body()));
                return;
            }
            JOptionPane.showMessageDialog(this,
                    body.path("message").asText("Admin created.")
                            + " They appear below once they verify.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Could not create admin: " + ex.getMessage());
            return;
        }
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        pwdTemp.setText("");
        loadAdmins();
    }

    private void loadAdmins() {
        tableModel.setRowCount(0);
        adminIds.clear();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/admin"))
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
            List<AdminResponseDTO> admins = MAPPER.readValue(
                    response.body(), new TypeReference<List<AdminResponseDTO>>() {});
            for (AdminResponseDTO admin : admins) {
                adminIds.add(admin.getId());
                String name = (admin.getFirstName() == null ? "" : admin.getFirstName())
                        + " " + (admin.getLastName() == null ? "" : admin.getLastName());
                tableModel.addRow(new Object[]{name.trim(), admin.getEmail(), "Change password"});
            }
        } catch (Exception ex) {
            showError("Could not load admins: " + ex.getMessage());
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
            JFrame frame = new JFrame("AdminsPanel - standalone test");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new AdminsPanel());
            frame.setVisible(true);
        });
    }
}
