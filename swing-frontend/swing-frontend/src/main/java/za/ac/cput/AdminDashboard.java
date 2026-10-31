package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private final Long adminId;

    private JPanel navigation;
    private CardLayout cardLayout;
    private JPanel contentPanel;



    private JButton btnFaculty;
    private JButton btnStudents;
    private JButton btnOrganisers;
    private JButton btnEvents;
    private JButton btnAdmins;
    private JButton btnNotifications;
    private JButton btnLogout;

    public AdminDashboard() {
        this(null);
    }

    public AdminDashboard(Long adminId) {
        this.adminId = adminId;
        setTitle("Campus Events - Admin Dashboard");
        AppShell.configureWindow(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setWindowIcon();

        add(buildNavigation(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);

        // land on Faculties by default
        cardLayout.show(contentPanel, "faculty");
        setActiveNav(btnFaculty);
    }

    private void setWindowIcon() {
        try {
            java.net.URL iconUrl = getClass().getResource("/za/ac/cput/images/image.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception ignored) {
        }
    }

    private JPanel buildNavigation() {
        JPanel nav = AppShell.navigation("Admin");
        navigation = nav;

        btnFaculty = navButton("Faculties");
        btnStudents = navButton("Students");
        btnOrganisers = navButton("Organisers");
        btnEvents = navButton("Events");
        btnAdmins = navButton("Admins");
        btnNotifications = navButton("Notifications");
        btnLogout = navButton("Logout");

        btnFaculty.addActionListener(e -> switchTo("faculty", btnFaculty));
        btnStudents.addActionListener(e -> switchTo("students", btnStudents));
        btnOrganisers.addActionListener(e -> switchTo("organisers", btnOrganisers));
        btnEvents.addActionListener(e -> switchTo("events", btnEvents));
        btnAdmins.addActionListener(e -> switchTo("admins", btnAdmins));
        btnNotifications.addActionListener(e -> switchTo("notifications", btnNotifications));
        btnLogout.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });

        nav.add(btnFaculty);
        nav.add(Box.createVerticalStrut(4));
        nav.add(btnStudents);
        nav.add(Box.createVerticalStrut(4));
        nav.add(btnOrganisers);
        nav.add(Box.createVerticalStrut(4));
        nav.add(btnEvents);
        nav.add(Box.createVerticalStrut(4));
        nav.add(btnAdmins);
        nav.add(Box.createVerticalStrut(4));
        nav.add(btnNotifications);
        nav.add(Box.createVerticalGlue());
        nav.add(btnLogout);

        return nav;
    }

    private JButton navButton(String text) {
        String icon = switch (text) {
            case "Faculties" -> "grid";
            case "Students", "Organisers" -> "users";
            case "Events" -> "calendar";
            case "Admins" -> "shield";
            case "Notifications" -> "bell";
            case "Logout" -> "logout";
            default -> "grid";
        };
        return AppShell.navButton(text, icon);
    }

    private void switchTo(String cardName, JButton activeButton) {
        cardLayout.show(contentPanel, cardName);
        setActiveNav(activeButton);
    }

    private void setActiveNav(JButton active) {
        AppShell.setActive(navigation, active);
    }

    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentPanel = AppShell.content(cardLayout);

        contentPanel.add(new FacultyPanel(adminId), "faculty");
        contentPanel.add(new StudentsPanel(adminId), "students");
        contentPanel.add(new OrganisersPanel(adminId), "organisers");
        contentPanel.add(new EventsPanel(adminId), "events");
        contentPanel.add(new AdminsPanel(adminId), "admins");
        contentPanel.add(new NotificationsPanel(), "notifications");

        return contentPanel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
