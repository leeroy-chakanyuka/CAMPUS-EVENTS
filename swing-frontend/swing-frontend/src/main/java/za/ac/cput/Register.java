package za.ac.cput;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatLightLaf;
import za.ac.cput.DTO.RegisterRequestDTO;
import za.ac.cput.DTO.RegisterResponseDTO;
import za.ac.cput.DTO.RegisterResponseDTO;

public class Register extends JFrame {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ButtonGroup roleGroup = new ButtonGroup();
    private JToggleButton btnRoleStudent;
    private JToggleButton btnRoleOrganiser;

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;

    private JTextField txtStudentNumber;
    private JComboBox<String> cmbFacultyStudent;

    private JComboBox<String> cmbFacultyOrganiser;
    private JLabel lblPendingNotice;

    private JPasswordField pwdPassword;
    private JPasswordField pwdConfirm;
    private JButton btnRegister;
    private JButton btnGoLogin;

    private JPanel studentFieldsPanel;
    private JPanel organiserFieldsPanel;
    private JPanel conditionalWrapper;
    private CardLayout conditionalLayout;

    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 15);
    private static final Font HEADING_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font NOTICE_FONT = new Font("SansSerif", Font.ITALIC, 11);

    private static final int LABEL_WIDTH = 170;
    private static final int FIELD_WIDTH = 300;
    private static final int FIELD_HEIGHT = 38;
    private static final Dimension FIELD_SIZE = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
    private static final Dimension ROW_SIZE = new Dimension(LABEL_WIDTH + 10 + FIELD_WIDTH, FIELD_HEIGHT);
    private static final String BASE_URL = "http://localhost:8080";

    public Register() {
        // set up the window — same size as Login
        setTitle("Campus Events - Register");
        setSize(960, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // create the components
        btnRoleStudent = new JToggleButton("Student", true);
        btnRoleOrganiser = new JToggleButton("Organiser");
        roleGroup.add(btnRoleStudent);
        roleGroup.add(btnRoleOrganiser);

        Dimension roleBtnSize = new Dimension(150, 44);
        for (JToggleButton b : new JToggleButton[]{btnRoleStudent, btnRoleOrganiser}) {
            b.setPreferredSize(roleBtnSize);
            b.setFont(BUTTON_FONT);
            b.setFocusPainted(false);
        }

        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtEmail = new JTextField();
        txtStudentNumber = new JTextField();
        cmbFacultyStudent = new JComboBox<>(new String[]{"ICT"});
        cmbFacultyOrganiser = new JComboBox<>(new String[]{"ICT"});
        pwdPassword = new JPasswordField();
        pwdConfirm = new JPasswordField();

        for (JTextField f : new JTextField[]{txtFirstName, txtLastName, txtEmail, txtStudentNumber, pwdPassword, pwdConfirm}) {
            f.setFont(FIELD_FONT);
            f.setPreferredSize(FIELD_SIZE);
            f.setMaximumSize(FIELD_SIZE);
        }
        for (JComboBox<String> c : new JComboBox[]{cmbFacultyStudent, cmbFacultyOrganiser}) {
            c.setFont(FIELD_FONT);
            c.setPreferredSize(FIELD_SIZE);
            c.setMaximumSize(FIELD_SIZE);
        }

        lblPendingNotice = new JLabel("<html>Needs admin approval before you can create events.</html>");
        lblPendingNotice.setFont(NOTICE_FONT);
        lblPendingNotice.setForeground(Color.GRAY);

        btnRegister = new JButton("Create account");
        btnGoLogin = new JButton("Already have an account? Sign in");
        btnRegister.setFont(BUTTON_FONT);
        btnGoLogin.setFont(LABEL_FONT);

        studentFieldsPanel = buildStudentFieldsPanel();
        organiserFieldsPanel = buildOrganiserFieldsPanel();

        conditionalLayout = new CardLayout();
        conditionalWrapper = new JPanel(conditionalLayout);
        conditionalWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        conditionalWrapper.setMaximumSize(new Dimension(ROW_SIZE.width, 100));
        conditionalWrapper.add(studentFieldsPanel, "STUDENT");
        conditionalWrapper.add(organiserFieldsPanel, "ORGANISER");

        btnRoleStudent.addItemListener(e -> {
            if (btnRoleStudent.isSelected()) conditionalLayout.show(conditionalWrapper, "STUDENT");
        });
        btnRoleOrganiser.addItemListener(e -> {
            if (btnRoleOrganiser.isSelected()) conditionalLayout.show(conditionalWrapper, "ORGANISER");
        });

        JPanel formSide = new JPanel();
        formSide.setLayout(new BoxLayout(formSide, BoxLayout.Y_AXIS));
        formSide.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        JLabel heading = new JLabel("Create an account");
        heading.setFont(HEADING_FONT);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        rolePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rolePanel.setMaximumSize(new Dimension(ROW_SIZE.width, 54));
        rolePanel.add(btnRoleStudent);
        rolePanel.add(btnRoleOrganiser);

        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setMaximumSize(new Dimension(ROW_SIZE.width, 44));
        btnGoLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGoLogin.setMaximumSize(new Dimension(ROW_SIZE.width, 28));

        formSide.add(heading);
        formSide.add(Box.createVerticalStrut(16));
        formSide.add(rolePanel);
        formSide.add(Box.createVerticalStrut(16));
        formSide.add(labeledRow("First name", txtFirstName));
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(labeledRow("Last name", txtLastName));
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(labeledRow("Email", txtEmail));
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(conditionalWrapper);
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(labeledRow("Password", pwdPassword));
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(labeledRow("Confirm password", pwdConfirm));
        formSide.add(Box.createVerticalStrut(16));
        formSide.add(btnRegister);
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(btnGoLogin);

        JPanel purplePanel = new JPanel();
        purplePanel.setBackground(new Color(108, 61, 189));
        purplePanel.setPreferredSize(new Dimension(400, 720));

        JPanel root = new JPanel(new BorderLayout());
        root.add(purplePanel, BorderLayout.WEST);
        root.add(formSide, BorderLayout.CENTER);
        setContentPane(root);

        btnGoLogin.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });

        btnRegister.addActionListener(e -> handleRegister());
    }

    private JPanel buildStudentFieldsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labeledRow("Student number", txtStudentNumber));
        panel.add(Box.createVerticalStrut(8));
        panel.add(labeledRow("Faculty", cmbFacultyStudent));
        return panel;
    }

    private JPanel buildOrganiserFieldsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPendingNotice.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labeledRow("Faculty", cmbFacultyOrganiser));
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblPendingNotice);
        return panel;
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

    private String getSelectedRole() {
        return btnRoleOrganiser.isSelected() ? "ORGANISER" : "STUDENT";
    }

    private void handleRegister() {
        String password = new String(pwdPassword.getPassword());
        String confirm = new String(pwdConfirm.getPassword());

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            RegisterRequestDTO register = new RegisterRequestDTO();
            register.setRole(getSelectedRole());
            register.setEmail(txtEmail.getText());
            register.setPassword(password);
            register.setFacultyId(1L);

            if (getSelectedRole().equals("STUDENT")) {
                register.setStudentNumber(txtStudentNumber.getText());
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(MAPPER.writeValueAsString(register)))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            RegisterResponseDTO result = MAPPER.readValue(response.body(), RegisterResponseDTO.class);

            if (result.isSuccess()) {
                new Verify(result.getUuid()).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, result.getMessage(),
                        "Registration failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not reach the backend: " + ex.getMessage(),
                    "Connection error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new Register().setVisible(true));
    }
}
