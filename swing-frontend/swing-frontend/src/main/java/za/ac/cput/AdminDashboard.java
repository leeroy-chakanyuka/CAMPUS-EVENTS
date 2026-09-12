package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private static final Color SIDEBAR_BG = new Color(0, 51, 102); // CPUT_BLUE
    private static final Color SIDEBAR_ACTIVE = new Color(0, 71, 133);

    private JButton btnFaculty;
    private JButton btnStudents;
    private JButton btnOrganisers;
    private JButton btnEvents;
    private JButton btnAdmins;
    private JButton btnNotifications;
    private JButton btnLogout;

    public AdminDashboard() {
        setTitle("Campus Events - Admin Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
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
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setPreferredSize(new Dimension(220, 700));
        nav.setBackground(SIDEBAR_BG);
        nav.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

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

    private JButton navButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(SIDEBAR_BG);
        button.setFocusPainted(false);
        button.setFocusable(false); 
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44)); 
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        return button;
    }

    private void switchTo(String cardName, JButton activeButton) {
        cardLayout.show(contentPanel, cardName);
        setActiveNav(activeButton);
    }

    private void setActiveNav(JButton active) {
        for (JButton b : new JButton[]{btnFaculty, btnStudents, btnOrganisers, btnEvents, btnAdmins, btnNotifications}) {
            b.setBackground(b == active ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        }
    }

    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new FacultyPanel(), "faculty");
        contentPanel.add(new StudentsPanel(), "students");
        contentPanel.add(new OrganisersPanel(), "organisers");
        contentPanel.add(new EventsPanel(), "events");
        contentPanel.add(new AdminsPanel(), "admins");
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
