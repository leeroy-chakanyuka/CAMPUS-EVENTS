package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;
import za.ac.cput.DTO.EventResponseDTO;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    private final Long studentId;

    private JPanel navigation;
    private CardLayout cardLayout;
    private JPanel contentPanel;



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
        AppShell.configureWindow(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildNavigation(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);

        cardLayout.show(contentPanel, "dashboard");
        setActiveNav(btnDashboard);
    }

    private JPanel buildNavigation() {
        JPanel nav = AppShell.navigation("Student");
        navigation = nav;

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
        String icon = switch (text) {
            case "Dashboard" -> "home";
            case "Browse Events" -> "calendar";
            case "My Tickets" -> "ticket";
            case "Notifications" -> "bell";
            case "Logout" -> "logout";
            default -> "grid";
        };
        return AppShell.navButton(text, icon);
    }

    private void setActiveNav(JButton active) {
        AppShell.setActive(navigation, active);
    }

    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentPanel = AppShell.content(cardLayout);

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
