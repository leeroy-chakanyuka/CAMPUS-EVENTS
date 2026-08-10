package za.ac.cput;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.formdev.flatlaf.FlatLightLaf;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.DTO.LoginRequestDTO;
import za.ac.cput.DTO.LoginResponseDTO;

public class Login extends JFrame {

    private final ButtonGroup roleGroup = new ButtonGroup();
    private JToggleButton btnRoleStudent;
    private JToggleButton btnRoleOrganiser;
    private JToggleButton btnRoleAdmin;

    private JTextField txtIdentifier;
    private JPasswordField pwdPassword;
    private JButton btnLogin;
    private JButton btnGoRegister;

    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 15);
    private static final Font HEADING_FONT = new Font("SansSerif", Font.BOLD, 26);
    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Login() {
        // set up the window
        setTitle("Campus Events - Sign in");
        setSize(960, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // create the components
        btnRoleStudent = new JToggleButton("Student", true);
        btnRoleOrganiser = new JToggleButton("Organiser");
        btnRoleAdmin = new JToggleButton("Admin");
        roleGroup.add(btnRoleStudent);
        roleGroup.add(btnRoleOrganiser);
        roleGroup.add(btnRoleAdmin);

        Dimension roleBtnSize = new Dimension(130, 50);
        for (JToggleButton b : new JToggleButton[]{btnRoleStudent, btnRoleOrganiser, btnRoleAdmin}) {
            b.setPreferredSize(roleBtnSize);
            b.setFont(BUTTON_FONT);
            b.setFocusPainted(false);
        }

        txtIdentifier = new JTextField(20);
        pwdPassword = new JPasswordField(20);
        txtIdentifier.setFont(FIELD_FONT);
        pwdPassword.setFont(FIELD_FONT);
        txtIdentifier.setPreferredSize(new Dimension(320, 42));
        pwdPassword.setPreferredSize(new Dimension(320, 42));
        txtIdentifier.setMaximumSize(new Dimension(320, 42));
        pwdPassword.setMaximumSize(new Dimension(320, 42));

        btnLogin = new JButton("Sign in");
        btnGoRegister = new JButton("Create an account");
        btnLogin.setFont(BUTTON_FONT);
        btnGoRegister.setFont(LABEL_FONT);
        btnLogin.setPreferredSize(new Dimension(320, 48));
        btnGoRegister.setPreferredSize(new Dimension(320, 36));

        // create image panel
        JPanel purplePanel = new JPanel();
        purplePanel.setBackground(new Color(108, 61, 189));
        purplePanel.setPreferredSize(new Dimension(400, 720));

        // create form panel
        JPanel formSide = new JPanel();
        formSide.setLayout(new BoxLayout(formSide, BoxLayout.Y_AXIS));
        formSide.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        JLabel heading = new JLabel("Sign in");
        heading.setFont(HEADING_FONT);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        // FlowLayout.CENTER keeps the three role buttons on one line, centered as a group
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        rolePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rolePanel.setMaximumSize(new Dimension(500, 54));
        rolePanel.add(btnRoleStudent);
        rolePanel.add(btnRoleOrganiser);
        rolePanel.add(btnRoleAdmin);

        JLabel lblId = new JLabel("Email");
        lblId.setFont(LABEL_FONT);
        lblId.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPwd = new JLabel("Password");
        lblPwd.setFont(LABEL_FONT);
        lblPwd.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtIdentifier.setAlignmentX(Component.CENTER_ALIGNMENT);
        pwdPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(320, 48));
        btnGoRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGoRegister.setMaximumSize(new Dimension(320, 36));

        formSide.add(Box.createVerticalGlue());
        formSide.add(heading);
        formSide.add(Box.createVerticalStrut(30));
        formSide.add(rolePanel);
        formSide.add(Box.createVerticalStrut(30));
        formSide.add(lblId);
        formSide.add(Box.createVerticalStrut(6));
        formSide.add(txtIdentifier);
        formSide.add(Box.createVerticalStrut(20));
        formSide.add(lblPwd);
        formSide.add(Box.createVerticalStrut(6));
        formSide.add(pwdPassword);
        formSide.add(Box.createVerticalStrut(30));
        formSide.add(btnLogin);
        formSide.add(Box.createVerticalStrut(12));
        formSide.add(btnGoRegister);
        formSide.add(Box.createVerticalGlue());

        // assemble
        JPanel root = new JPanel(new BorderLayout());
        root.add(purplePanel, BorderLayout.WEST);
        root.add(formSide, BorderLayout.CENTER);
        setContentPane(root);

        // link to register screen
        btnGoRegister.addActionListener(e -> {
            new Register().setVisible(true);
            this.dispose();
        });

        // real login call
        btnLogin.addActionListener(e -> handleLogin());
    }

    private String getSelectedRole() {
        if (btnRoleOrganiser.isSelected()) return "ORGANISER";
        if (btnRoleAdmin.isSelected()) return "ADMIN";
        return "STUDENT";
    }

    private void handleLogin() {
        try {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setRole(getSelectedRole());
            dto.setEmail(txtIdentifier.getText());
            dto.setPassword(new String(pwdPassword.getPassword()));

            String jsonBody = MAPPER.writeValueAsString(dto);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            LoginResponseDTO loginResponse =
                    MAPPER.readValue(response.body(), LoginResponseDTO.class);

            if (loginResponse.isSuccess()) {
                // TODO: Open dashboard based on loginResponse.getRole()
                JOptionPane.showMessageDialog(
                        this,
                        "Logged in as " + loginResponse.getRole()
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        loginResponse.getMessage(),
                        "Login failed",
                        JOptionPane.ERROR_MESSAGE
                );
                pwdPassword.setText("");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not reach the backend: " + ex.getMessage(),
                    "Connection error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
