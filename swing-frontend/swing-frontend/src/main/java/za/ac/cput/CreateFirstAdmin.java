package za.ac.cput;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.DTO.CreateAdminRequestDTO;
import za.ac.cput.DTO.CreateAdminResponseDTO;
public class CreateFirstAdmin extends JFrame {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JPasswordField pwdPassword;
    private JPasswordField pwdConfirm;
    private JButton btnCreate;

    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 15);
    private static final Font HEADING_FONT = new Font("SansSerif", Font.BOLD, 24);

    private static final int LABEL_WIDTH = 170;
    private static final int FIELD_WIDTH = 300;
    private static final int FIELD_HEIGHT = 38;
    private static final Dimension FIELD_SIZE = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
    private static final Dimension ROW_SIZE = new Dimension(LABEL_WIDTH + 10 + FIELD_WIDTH, FIELD_HEIGHT);
    private static final String BASE_URL = "http://localhost:8080";

    public CreateFirstAdmin() {
        setTitle("Campus Events : Set up your Admin account");
        setSize(960, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // create the components
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtEmail = new JTextField();
        pwdPassword = new JPasswordField();
        pwdConfirm = new JPasswordField();

        for (JTextField f : new JTextField[]{txtFirstName, txtLastName, txtEmail, pwdPassword, pwdConfirm}) {
            f.setFont(FIELD_FONT);
            f.setPreferredSize(FIELD_SIZE);
            f.setMaximumSize(FIELD_SIZE);
        }

        btnCreate = new JButton("Create Admin & continue");
        btnCreate.setFont(BUTTON_FONT);
        btnCreate.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCreate.setMaximumSize(new Dimension(ROW_SIZE.width, 44));

        // create image panel
        JPanel purplePanel = new JPanel();
        purplePanel.setBackground(new Color(108, 61, 189));
        purplePanel.setPreferredSize(new Dimension(400, 720));

        // create form panel
        JPanel formSide = new JPanel();
        formSide.setLayout(new BoxLayout(formSide, BoxLayout.Y_AXIS));
        formSide.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        JLabel heading = new JLabel("Welcome, set up your Admin account");
        heading.setFont(HEADING_FONT);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subheading = new JLabel("This only happens once, the first time the app runs.");
        subheading.setFont(LABEL_FONT);
        subheading.setForeground(Color.GRAY);
        subheading.setAlignmentX(Component.CENTER_ALIGNMENT);

        formSide.add(Box.createVerticalGlue());
        formSide.add(heading);
        formSide.add(Box.createVerticalStrut(6));
        formSide.add(subheading);
        formSide.add(Box.createVerticalStrut(24));
        formSide.add(labeledRow("First name", txtFirstName));
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(labeledRow("Last name", txtLastName));
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(labeledRow("Email", txtEmail));
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(labeledRow("Password", pwdPassword));
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(labeledRow("Confirm password", pwdConfirm));
        formSide.add(Box.createVerticalStrut(24));
        formSide.add(btnCreate);
        formSide.add(Box.createVerticalGlue());

        // assemble
        JPanel root = new JPanel(new BorderLayout());
        root.add(purplePanel, BorderLayout.WEST);
        root.add(formSide, BorderLayout.CENTER);
        setContentPane(root);

        btnCreate.addActionListener(e -> handleCreate());
    }

    private JPanel labeledRow(String labelText, JComponent field) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(ROW_SIZE);
        row.setPreferredSize(ROW_SIZE);

        JLabel label = new JLabel(labelText + " :");
        label.setFont(LABEL_FONT);
        label.setPreferredSize(new Dimension(LABEL_WIDTH, FIELD_HEIGHT));
        label.setMaximumSize(new Dimension(LABEL_WIDTH, FIELD_HEIGHT));
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(label);
        row.add(Box.createHorizontalStrut(10));
        row.add(field);
        return row;
    }

    private void handleCreate() {
        String password = new String(pwdPassword.getPassword());
        String confirm  = new String(pwdConfirm.getPassword());

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            CreateAdminRequestDTO adminRequest = new CreateAdminRequestDTO();
            adminRequest.setFirstName(txtFirstName.getText());
            adminRequest.setLastName(txtLastName.getText());
            adminRequest.setEmail(txtEmail.getText());   // ← was setPassword(txtEmail.getText())
            adminRequest.setPassword(password);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/admin/seed"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(MAPPER.writeValueAsString(adminRequest)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // ← actually check the response instead of blindly continuing
            CreateAdminResponseDTO dto = MAPPER.readValue(response.body(), CreateAdminResponseDTO.class);
            if (!dto.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Failed: " + dto.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Admin account created. Please sign in.");
            new Login().setVisible(true);
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Something went wrong: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CreateFirstAdmin().setVisible(true));
    }
}