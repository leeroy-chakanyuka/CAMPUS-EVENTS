package za.ac.cput;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.DTO.VerifyRequestDTO;
import za.ac.cput.DTO.VerifyResponseDTO;
import za.ac.cput.DTO.ResendRequestDTO;
import com.formdev.flatlaf.FlatLightLaf;

public class Verify extends JFrame {

    private JPasswordField pinField;
    private JButton submitButton;
    private JButton resendButton;
    private JLabel messageLabel;

    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, 18);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 15);
    private static final Font HEADING_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final int LABEL_WIDTH = 170;
    private static final int FIELD_WIDTH = 300;
    private static final int FIELD_HEIGHT = 38;
    private static final Dimension FIELD_SIZE = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
    private static final Dimension ROW_SIZE = new Dimension(LABEL_WIDTH + 10 + FIELD_WIDTH, FIELD_HEIGHT);
    private static final String BASE_URL = "http://localhost:8080";

    // passed in from Register when it opens this screen
    private final String uuid;

    public Verify(String uuid) {
        this.uuid = uuid;
        initComponents();
    }

    private void initComponents() {
        // set up the window — same size as Login and Register
        setTitle("Campus Events - Verify your account");
        setSize(960, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // create the components
        pinField = new JPasswordField();
        pinField.setFont(FIELD_FONT);
        pinField.setPreferredSize(FIELD_SIZE);
        pinField.setMaximumSize(FIELD_SIZE);
        pinField.setEchoChar('\u2022');

        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        messageLabel.setForeground(Color.GRAY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton = new JButton("Verify OTP");
        submitButton.setFont(BUTTON_FONT);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setMaximumSize(new Dimension(ROW_SIZE.width, 44));
        submitButton.setFocusPainted(false);

        resendButton = new JButton("Resend OTP");
        resendButton.setFont(LABEL_FONT);
        resendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resendButton.setMaximumSize(new Dimension(ROW_SIZE.width, 36));
        resendButton.setFocusPainted(false);

        // create image panel
        JPanel purplePanel = new JPanel();
        purplePanel.setBackground(new Color(108, 61, 189));
        purplePanel.setPreferredSize(new Dimension(400, 720));

        // create form panel
        JPanel formSide = new JPanel();
        formSide.setLayout(new BoxLayout(formSide, BoxLayout.Y_AXIS));
        formSide.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        JLabel heading = new JLabel("Verify your account");
        heading.setFont(HEADING_FONT);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subheading = new JLabel("Enter the OTP sent to your email.");
        subheading.setFont(LABEL_FONT);
        subheading.setForeground(Color.GRAY);
        subheading.setAlignmentX(Component.CENTER_ALIGNMENT);

        formSide.add(Box.createVerticalGlue());
        formSide.add(heading);
        formSide.add(Box.createVerticalStrut(6));
        formSide.add(subheading);
        formSide.add(Box.createVerticalStrut(32));
        formSide.add(labeledRow("OTP code", pinField));
        formSide.add(Box.createVerticalStrut(8));
        formSide.add(messageLabel);
        formSide.add(Box.createVerticalStrut(24));
        formSide.add(submitButton);
        formSide.add(Box.createVerticalStrut(12));
        formSide.add(resendButton);
        formSide.add(Box.createVerticalGlue());

        // assemble
        JPanel root = new JPanel(new BorderLayout());
        root.add(purplePanel, BorderLayout.WEST);
        root.add(formSide, BorderLayout.CENTER);
        setContentPane(root);

        submitButton.addActionListener(e -> handleVerify());
        resendButton.addActionListener(e -> handleResend());
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

    private void handleVerify() {
        String pin = new String(pinField.getPassword()).trim();

        if (pin.isEmpty()) {
            messageLabel.setText("Please enter your OTP code.");
            return;
        }

        try {
            VerifyRequestDTO dto = new VerifyRequestDTO();
            dto.setUuid(uuid);
            dto.setPin(pin);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/verify"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            mapper.writeValueAsString(dto)))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            VerifyResponseDTO verifyResponse =
                    mapper.readValue(response.body(), VerifyResponseDTO.class);

            if (verifyResponse.isSuccess()) {
                JOptionPane.showMessageDialog(this,
                        "Account verified! Please sign in.");

                new Login().setVisible(true);
                dispose();
            } else {
                String msg = verifyResponse.getMessage();

                if ("expired".equalsIgnoreCase(msg)) {
                    messageLabel.setText(
                            "Your OTP expired. Click Resend to get a new one.");
                } else {
                    messageLabel.setText(msg);
                }
            }

        } catch (Exception ex) {
            messageLabel.setText("Could not reach the backend.");
        }
    }

    private void handleResend() {
        try {
            ResendRequestDTO dto = new ResendRequestDTO();
            dto.setUuid(uuid);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/resend"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            mapper.writeValueAsString(dto)))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            VerifyResponseDTO resendResponse =
                    mapper.readValue(response.body(), VerifyResponseDTO.class);

            if (resendResponse.isSuccess()) {

                pinField.setText("");
                messageLabel.setText("New OTP sent! Check your email.");
                resendButton.setEnabled(false);

                Timer timer = new Timer(30000, e -> {
                    resendButton.setEnabled(true);
                    resendButton.setText("Resend OTP");
                });

                timer.setRepeats(false);
                resendButton.setText("Resend OTP (wait 30s)");
                timer.start();

            } else {
                messageLabel.setText(resendResponse.getMessage());
            }

        } catch (Exception ex) {
            messageLabel.setText("Could not reach the backend.");
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Verify("test-uuid").setVisible(true));
    }
}