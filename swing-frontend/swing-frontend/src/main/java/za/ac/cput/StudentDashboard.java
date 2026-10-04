package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;
import za.ac.cput.DTO.EventResponseDTO;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    private final Long studentId;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private static final Color SIDEBAR_BG = new Color(0, 51, 102);
    private static final Color SIDEBAR_ACTIVE = new Color(0, 71, 133);

    private JButton btnDashboard;
    private JButton btnBrowse;
    private JButton btnTickets;
    private JButton btnNotifications;

    private StudentHomePanel homePanel;
    private BrowseEventsPanel browsePanel;
    private EventDetailsPanel detailsPanel;
    private StudentTicketsPanel ticketsPanel;
    private StudentNotificationsPanel notificationsPanel;

    public StudentDashboard() {
        this(null);
    }

    public StudentDashboard(Long studentId) {
        this.studentId = studentId;
        setTitle("Campus Events - Student Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(buildNavigation(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);

        cardLayout.show(contentPanel, "dashboard");
        setActiveNav(btnDashboard);
    }

    private JPanel buildNavigation() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setPreferredSize(new Dimension(220, 700));
        nav.setBackground(SIDEBAR_BG);
        nav.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        JLabel brand = new JLabel("Campus Events");
        brand.setFont(new Font("Arial", Font.BOLD, 16));
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnDashboard = navButton("Dashboard");
        btnBrowse = navButton("Browse Events");
        btnTickets = navButton("My Tickets");
        btnNotifications = navButton("Notifications");
        JButton btnLogout = navButton("Logout");

        btnDashboard.addActionListener(e -> {
            cardLayout.show(contentPanel, "dashboard");
            setActiveNav(btnDashboard);
        });
        btnBrowse.addActionListener(e -> {
            showBrowse();
            setActiveNav(btnBrowse);
        });
        btnTickets.addActionListener(e -> {
            cardLayout.show(contentPanel, "tickets");
            setActiveNav(btnTickets);
        });
        btnNotifications.addActionListener(e -> {
            cardLayout.show(contentPanel, "notifications");
            setActiveNav(btnNotifications);
        });
        btnLogout.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });

        nav.add(brand);
        nav.add(Box.createVerticalStrut(24));
        nav.add(btnDashboard);
        nav.add(Box.createVerticalStrut(8));
        nav.add(btnBrowse);
        nav.add(Box.createVerticalStrut(8));
        nav.add(btnTickets);
        nav.add(Box.createVerticalStrut(8));
        nav.add(btnNotifications);
        nav.add(Box.createVerticalGlue());
        nav.add(btnLogout);
        return nav;
    }

    private JButton navButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(196, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(SIDEBAR_BG);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private void setActiveNav(JButton active) {
        for (JButton b : new JButton[]{btnDashboard, btnBrowse, btnTickets, btnNotifications}) {
            b.setBackground(b == active ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        }
    }

    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        homePanel = new StudentHomePanel(studentId);
        browsePanel = new BrowseEventsPanel(studentId, this::showDetails);
        detailsPanel = new EventDetailsPanel(studentId, this::showBrowse);
        ticketsPanel = new StudentTicketsPanel(studentId);
        notificationsPanel = new StudentNotificationsPanel(studentId);

        contentPanel.add(homePanel, "dashboard");
        contentPanel.add(browsePanel, "browse");
        contentPanel.add(detailsPanel, "details");
        contentPanel.add(ticketsPanel, "tickets");
        contentPanel.add(notificationsPanel, "notifications");

        return contentPanel;
    }

    private void showBrowse() {
        cardLayout.show(contentPanel, "browse");
        setActiveNav(btnBrowse);
    }

    private void showDetails(EventResponseDTO event) {
        detailsPanel.setEvent(event);
        cardLayout.show(contentPanel, "details");
        setActiveNav(btnBrowse);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new StudentDashboard().setVisible(true));
    }
}
