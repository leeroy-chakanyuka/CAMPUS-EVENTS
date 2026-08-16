package za.ac.cput;
/*
Mologadi Dikgale
Student Number:231016263
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

public class Dashboard extends JFrame {



    private static final String BASE_URL = "http://localhost:8080";
    private static final int ADMIN_ID = 1;

    private final HttpClient httpClient = HttpClient.newHttpClient();



    private final Color SIDEBAR_COLOR = new Color(30, 30, 40);
    private final Color PRIMARY_COLOR = new Color(108, 61, 189);
    private final Color BACKGROUND_COLOR = new Color(244, 243, 246);
    private final Color WHITE = Color.WHITE;


    private JPanel contentPanel;
    private JLabel breadcrumb;

    private CardLayout cardLayout;



    private JTable facultyTable;
    private JTable studentTable;
    private JTable organiserTable;
    private JTable eventTable;
    private JTable adminTable;
    private JTable notificationTable;



    private JTextField facultyNameField;
    private JTextField facultyEmailField;

    private JTextField adminFirstNameField;
    private JTextField adminLastNameField;
    private JTextField adminEmailField;
    private JPasswordField adminPasswordField;

    private JComboBox<String> recipientTypeCombo;
    private JComboBox<String> recipientCombo;
    private JTextArea notificationMessage;



    private final Set<String> facultyEmails = new HashSet<>();
    private final Set<String> adminEmails = new HashSet<>();




    public Dashboard() {

        setTitle("Campus Events — Admin Dashboard");

        setSize(1200, 750);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        createSidebar();

        createContentArea();
    }




    private void createSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setPreferredSize(new Dimension(220, 0));

        sidebar.setBackground(SIDEBAR_COLOR);

        sidebar.setLayout(new BorderLayout());




        JPanel header = new JPanel();

        header.setBackground(SIDEBAR_COLOR);

        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        header.setBorder(new EmptyBorder(20, 20, 20, 20));


        JLabel appName = new JLabel("Campus Events");

        appName.setForeground(Color.WHITE);

        appName.setFont(new Font("Segoe UI", Font.BOLD, 16));


        JLabel role = new JLabel("ADMINISTRATOR");

        role.setForeground(new Color(150, 150, 150));

        role.setFont(new Font("Segoe UI", Font.PLAIN, 11));


        header.add(appName);

        header.add(Box.createVerticalStrut(4));

        header.add(role);


        sidebar.add(header, BorderLayout.NORTH);



        JPanel navigation = new JPanel();

        navigation.setBackground(SIDEBAR_COLOR);

        navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));


        addNavigationButton(navigation, "Faculties", "faculties");

        addNavigationButton(navigation, "Students", "students");

        addNavigationButton(navigation, "Organisers", "organisers");

        addNavigationButton(navigation, "Events", "events");

        addNavigationButton(navigation, "Admins", "admins");

        addNavigationButton(navigation, "Notifications", "notifications");


        sidebar.add(navigation, BorderLayout.CENTER);



        JButton logoutButton = new JButton("Logout");

        styleSidebarButton(logoutButton);

        logoutButton.addActionListener(e -> {

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {

                dispose();

            }

        });


        JPanel footer = new JPanel(new BorderLayout());

        footer.setBackground(SIDEBAR_COLOR);

        footer.setBorder(new EmptyBorder(10, 10, 10, 10));

        footer.add(logoutButton);


        sidebar.add(footer, BorderLayout.SOUTH);


        add(sidebar, BorderLayout.WEST);
    }



    private void addNavigationButton(
            JPanel parent,
            String text,
            String panelName
    ) {

        JButton button = new JButton(text);

        styleSidebarButton(button);

        button.addActionListener(e -> {

            cardLayout.show(contentPanel, panelName);

            breadcrumb.setText(text);

            switch (panelName) {

                case "students":
                    loadStudents();
                    break;

                case "organisers":
                    loadOrganisers();
                    break;

                case "events":
                    loadEvents();
                    break;

                case "admins":
                    loadAdmins();
                    break;

                case "notifications":
                    loadRecipients();
                    break;
            }

        });

        parent.add(button);
    }


    private void styleSidebarButton(JButton button) {

        button.setForeground(new Color(200, 200, 200));

        button.setBackground(SIDEBAR_COLOR);

        button.setBorderPainted(false);

        button.setFocusPainted(false);

        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        button.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                45
        ));
    }




    private void createContentArea() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.setBackground(BACKGROUND_COLOR);



        JPanel topBar = new JPanel(new BorderLayout());

        topBar.setBackground(Color.WHITE);

        topBar.setBorder(new EmptyBorder(15, 25, 15, 25));


        breadcrumb = new JLabel("Dashboard");

        breadcrumb.setForeground(Color.GRAY);

        breadcrumb.setFont(new Font("Segoe UI", Font.PLAIN, 13));


        JLabel adminLabel = new JLabel("Signed in as Admin");

        adminLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));


        topBar.add(breadcrumb, BorderLayout.WEST);

        topBar.add(adminLabel, BorderLayout.EAST);


        mainPanel.add(topBar, BorderLayout.NORTH);



        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);

        contentPanel.setBackground(BACKGROUND_COLOR);


        contentPanel.add(createEmptyPanel(), "empty");

        contentPanel.add(createFacultyPanel(), "faculties");

        contentPanel.add(createStudentPanel(), "students");

        contentPanel.add(createOrganiserPanel(), "organisers");

        contentPanel.add(createEventPanel(), "events");

        contentPanel.add(createAdminPanel(), "admins");

        contentPanel.add(createNotificationPanel(), "notifications");


        JScrollPane scrollPane = new JScrollPane(contentPanel);

        scrollPane.setBorder(null);


        mainPanel.add(scrollPane, BorderLayout.CENTER);


        add(mainPanel, BorderLayout.CENTER);
    }



    private JPanel createEmptyPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(BACKGROUND_COLOR);


        JLabel title = new JLabel("Select an option from the menu");

        title.setFont(new Font("Segoe UI", Font.BOLD, 18));


        JLabel description = new JLabel(
                "Faculty, Students, Organisers, Events, and Admins are managed here."
        );


        JPanel box = new JPanel();

        box.setBackground(Color.WHITE);

        box.setBorder(new EmptyBorder(60, 60, 60, 60));

        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));


        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        description.setAlignmentX(Component.CENTER_ALIGNMENT);


        box.add(title);

        box.add(Box.createVerticalStrut(10));

        box.add(description);


        panel.add(box);


        return panel;
    }




    private JPanel createFacultyPanel() {

        JPanel panel = createBasePanel();


        JLabel title = new JLabel("Faculties");

        title.setFont(new Font("Segoe UI", Font.BOLD, 20));


        JPanel form = new JPanel(new GridLayout(1, 3, 10, 10));

        form.setBackground(Color.WHITE);

        form.setBorder(new EmptyBorder(20, 20, 20, 20));


        facultyNameField = new JTextField();

        facultyEmailField = new JTextField();


        form.add(createField(
                "Faculty Name",
                facultyNameField
        ));

        form.add(createField(
                "Contact Email",
                facultyEmailField
        ));


        JButton createButton = new JButton("Create Faculty");

        stylePrimaryButton(createButton);

        createButton.addActionListener(e -> createFaculty());


        form.add(createButton);


        facultyTable = new JTable();

        setupTable(facultyTable);


        JScrollPane tableScroll = new JScrollPane(facultyTable);


        panel.add(title);

        panel.add(Box.createVerticalStrut(15));

        panel.add(form);

        panel.add(Box.createVerticalStrut(20));

        panel.add(tableScroll);


        return panel;
    }


    private void createFaculty() {

        String name = facultyNameField.getText().trim();

        String email = facultyEmailField.getText().trim();


        if (name.isEmpty()) {

            showError("Faculty name is required.");

            return;
        }


        if (!isValidEmail(email)) {

            showError("Invalid email format.");

            return;
        }


        if (facultyEmails.contains(email.toLowerCase())) {

            showError("Email already used by another faculty.");

            return;
        }


        String json = String.format(
                """
                {
                    "name": "%s",
                    "contactEmail": "%s",
                    "adminId": %d
                }
                """,
                escapeJson(name),
                escapeJson(email),
                ADMIN_ID
        );


        try {

            post("/faculty", json);

            facultyEmails.add(email.toLowerCase());

            facultyNameField.setText("");

            facultyEmailField.setText("");

            showMessage("Faculty created successfully.");

            loadFaculties();

        } catch (Exception e) {

            showError(e.getMessage());
        }
    }



    private void loadFaculties() {

        try {

            String response = get("/faculty");

            // Requires JSON library for production parsing.
            // See Jackson dependency below.

            showMessage("Faculty data loaded.");

        } catch (Exception e) {

            showError("Failed to load faculties: " + e.getMessage());
        }
    }



    private JPanel createStudentPanel() {

        JPanel panel = createBasePanel();


        JLabel title = new JLabel("Students");

        title.setFont(new Font("Segoe UI", Font.BOLD, 20));


        studentTable = new JTable();

        setupTable(studentTable);


        panel.add(title);

        panel.add(Box.createVerticalStrut(15));

        panel.add(new JScrollPane(studentTable));


        return panel;
    }


    private void loadStudents() {

        try {

            String response = get("/student");

            System.out.println(response);

            // Parse JSON here using Jackson.
            // Then populate studentTable.

        } catch (Exception e) {

            showError(
                    "Failed to load students: "
                            + e.getMessage()
            );
        }
    }



    private JPanel createOrganiserPanel() {

        JPanel panel = createBasePanel();


        JLabel title = new JLabel("Organisers");

        title.setFont(new Font("Segoe UI", Font.BOLD, 20));


        organiserTable = new JTable();

        setupTable(organiserTable);


        panel.add(title);

        panel.add(Box.createVerticalStrut(15));

        panel.add(new JScrollPane(organiserTable));


        return panel;
    }


    private void loadOrganisers() {

        try {

            String response = get("/organiser");

            System.out.println(response);

        } catch (Exception e) {

            showError(
                    "Failed to load organisers: "
                            + e.getMessage()
            );
        }
    }



    private JPanel createEventPanel() {

        JPanel panel = createBasePanel();


        JLabel title = new JLabel("Events");

        title.setFont(new Font("Segoe UI", Font.BOLD, 20));


        eventTable = new JTable();

        setupTable(eventTable);


        panel.add(title);

        panel.add(Box.createVerticalStrut(15));

        panel.add(new JScrollPane(eventTable));


        return panel;
    }


    private void loadEvents() {

        try {

            String response = get("/event");

            System.out.println(response);

        } catch (Exception e) {

            showError(
                    "Failed to load events: "
                            + e.getMessage()
            );
        }
    }


    private JPanel createAdminPanel() {

        JPanel panel = createBasePanel();


        JLabel title = new JLabel("Admins");

        title.setFont(new Font("Segoe UI", Font.BOLD, 20));


        JPanel form = new JPanel(
                new GridLayout(1, 5, 10, 10)
        );

        form.setBackground(Color.WHITE);

        form.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );


        adminFirstNameField = new JTextField();

        adminLastNameField = new JTextField();

        adminEmailField = new JTextField();

        adminPasswordField = new JPasswordField();


        form.add(createField(
                "First Name",
                adminFirstNameField
        ));

        form.add(createField(
                "Last Name",
                adminLastNameField
        ));

        form.add(createField(
                "Email",
                adminEmailField
        ));

        form.add(createField(
                "Temporary Password",
                adminPasswordField
        ));


        JButton createButton =
                new JButton("Create Admin");

        stylePrimaryButton(createButton);

        createButton.addActionListener(
                e -> createAdmin()
        );


        form.add(createButton);


        adminTable = new JTable();

        setupTable(adminTable);


        panel.add(title);

        panel.add(Box.createVerticalStrut(15));

        panel.add(form);

        panel.add(Box.createVerticalStrut(20));

        panel.add(new JScrollPane(adminTable));


        return panel;
    }


    private void createAdmin() {

        String firstName =
                adminFirstNameField.getText().trim();

        String lastName =
                adminLastNameField.getText().trim();

        String email =
                adminEmailField.getText().trim();

        String password =
                new String(
                        adminPasswordField.getPassword()
                );


        if (
                firstName.isEmpty()
                        || lastName.isEmpty()
                        || email.isEmpty()
                        || password.isEmpty()
        ) {

            showError("All fields are required.");

            return;
        }


        if (!isValidEmail(email)) {

            showError("Invalid email format.");

            return;
        }


        if (adminEmails.contains(email.toLowerCase())) {

            showError(
                    "Email already used by another admin."
            );

            return;
        }


        String json = String.format(
                """
                {
                    "firstName": "%s",
                    "lastName": "%s",
                    "email": "%s",
                    "temporaryPassword": "%s",
                    "requestingAdminId": %d
                }
                """,
                escapeJson(firstName),
                escapeJson(lastName),
                escapeJson(email),
                escapeJson(password),
                ADMIN_ID
        );


        try {

            post("/admin", json);

            adminEmails.add(email.toLowerCase());

            adminFirstNameField.setText("");

            adminLastNameField.setText("");

            adminEmailField.setText("");

            adminPasswordField.setText("");

            showMessage("Admin created successfully.");

            loadAdmins();

        } catch (Exception e) {

            showError(e.getMessage());
        }
    }


    private void loadAdmins() {

        try {

            String response = get("/admin");

            System.out.println(response);

        } catch (Exception e) {

            showError(
                    "Failed to load admins: "
                            + e.getMessage()
            );
        }
    }




    private JPanel createNotificationPanel() {

        JPanel panel = createBasePanel();


        JLabel title = new JLabel("Notifications");

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 20)
        );


        recipientTypeCombo =
                new JComboBox<>(
                        new String[]{
                                "Student",
                                "Organiser"
                        }
                );


        recipientCombo =
                new JComboBox<>();


        notificationMessage =
                new JTextArea(5, 30);

        notificationMessage.setLineWrap(true);

        notificationMessage.setWrapStyleWord(true);


        JPanel form = new JPanel();

        form.setBackground(Color.WHITE);

        form.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        form.setLayout(
                new BoxLayout(
                        form,
                        BoxLayout.Y_AXIS
                )
        );


        form.add(
                createField(
                        "Recipient Type",
                        recipientTypeCombo
                )
        );


        form.add(Box.createVerticalStrut(10));


        form.add(
                createField(
                        "Recipient",
                        recipientCombo
                )
        );


        form.add(Box.createVerticalStrut(10));


        form.add(
                createField(
                        "Message",
                        new JScrollPane(
                                notificationMessage
                        )
                )
        );


        JButton sendButton =
                new JButton("Send");

        stylePrimaryButton(sendButton);

        sendButton.addActionListener(
                e -> sendNotification()
        );


        form.add(Box.createVerticalStrut(10));

        form.add(sendButton);


        notificationTable = new JTable();

        setupTable(notificationTable);


        recipientTypeCombo.addActionListener(
                e -> loadRecipients()
        );


        panel.add(title);

        panel.add(Box.createVerticalStrut(15));

        panel.add(form);

        panel.add(Box.createVerticalStrut(20));


        JLabel logLabel =
                new JLabel("Sent Notifications");

        logLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );


        panel.add(logLabel);

        panel.add(Box.createVerticalStrut(10));

        panel.add(
                new JScrollPane(notificationTable)
        );


        return panel;
    }


    private void loadRecipients() {

        String type =
                (String) recipientTypeCombo.getSelectedItem();


        String endpoint =
                type.equals("Student")
                        ? "/student"
                        : "/organiser";


        try {

            String response = get(endpoint);

            System.out.println(response);

        } catch (Exception e) {

            showError(
                    "Failed to load recipients: "
                            + e.getMessage()
            );
        }
    }


    private void sendNotification() {

        String type =
                (String)
                        recipientTypeCombo.getSelectedItem();


        String message =
                notificationMessage
                        .getText()
                        .trim();


        if (message.isEmpty()) {

            showError("Message cannot be empty.");

            return;
        }


        if (recipientCombo.getSelectedItem() == null) {

            showError("Please select a recipient.");

            return;
        }


        String recipient =
                recipientCombo
                        .getSelectedItem()
                        .toString();


        String json = String.format(
                """
                {
                    "recipientType": "%s",
                    "recipientId": "%s",
                    "recipient": "%s",
                    "message": "%s"
                }
                """,
                escapeJson(type),
                escapeJson(recipient),
                escapeJson(recipient),
                escapeJson(message)
        );


        try {

            post(
                    "/notification/send",
                    json
            );


            DefaultTableModel model =
                    (DefaultTableModel)
                            notificationTable
                                    .getModel();


            model.addRow(
                    new Object[]{
                            recipient,
                            type,
                            message
                    }
            );


            notificationMessage.setText("");

            showMessage(
                    "Notification sent successfully."
            );


        } catch (Exception e) {

            showError(e.getMessage());
        }
    }


    private String get(String endpoint)
            throws Exception {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        BASE_URL + endpoint
                                )
                        )
                        .GET()
                        .build();


        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        if (
                response.statusCode() < 200
                        || response.statusCode() >= 300
        ) {

            throw new Exception(
                    response.body()
            );
        }


        return response.body();
    }

    private String post(
            String endpoint,
            String json
    ) throws Exception {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        BASE_URL + endpoint
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(json)
                        )
                        .build();


        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        if (
                response.statusCode() < 200
                        || response.statusCode() >= 300
        ) {

            throw new Exception(
                    response.body()
            );
        }


        return response.body();
    }


    // ============================================================
    // HTTP PUT
    // ============================================================

    private String put(
            String endpoint,
            String json
    ) throws Exception {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        BASE_URL + endpoint
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .PUT(
                                HttpRequest.BodyPublishers
                                        .ofString(json)
                        )
                        .build();


        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        if (
                response.statusCode() < 200
                        || response.statusCode() >= 300
        ) {

            throw new Exception(
                    response.body()
            );
        }


        return response.body();
    }


    private JPanel createBasePanel() {

        JPanel panel = new JPanel();

        panel.setBackground(BACKGROUND_COLOR);

        panel.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );


        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );


        return panel;
    }


    private JPanel createField(
            String label,
            JComponent component
    ) {

        JPanel panel = new JPanel(
                new BorderLayout(5, 5)
        );

        panel.setBackground(Color.WHITE);


        JLabel labelComponent =
                new JLabel(label);

        labelComponent.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );


        panel.add(
                labelComponent,
                BorderLayout.NORTH
        );


        panel.add(
                component,
                BorderLayout.CENTER
        );


        return panel;
    }


    private void setupTable(JTable table) {

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );


        table.setRowHeight(35);

        table.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );


        table.setModel(
                new DefaultTableModel()
        );
    }


    private void stylePrimaryButton(
            JButton button
    ) {

        button.setBackground(PRIMARY_COLOR);

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );
    }


    private boolean isValidEmail(
            String email
    ) {

        return email != null
                && email.contains("@")
                && email.contains(".");
    }


    private String escapeJson(
            String value
    ) {

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }



    private void showMessage(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Campus Events",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    private void showError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }



    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(() -> {

            Dashboard dashboard =
                    new Dashboard();

            dashboard.setVisible(true);

        });
    }
}