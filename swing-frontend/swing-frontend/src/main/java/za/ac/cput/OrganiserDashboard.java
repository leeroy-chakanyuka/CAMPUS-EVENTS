package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class OrganiserDashboard extends JFrame {

    private final Long organiserId;
    private final boolean readOnly;

    private JPanel navigation;
    private CardLayout cardLayout;
    private JPanel contentPanel;



    private JButton btnDashboard;
    private JButton btnMyEvents;
    private JButton btnNotifications;
    private JButton btnLogout;

    public OrganiserDashboard() {
        this(null, true);
    }

    public OrganiserDashboard(Long organiserId) {
        this(organiserId, true);
    }

    public OrganiserDashboard(Long organiserId, boolean readOnly) {
        this.organiserId = organiserId;
        this.readOnly = readOnly;
        setTitle("Campus Events - Organiser Dashboard");
        AppShell.configureWindow(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setWindowIcon();

        add(buildNavigation(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
        if (readOnly) {
            add(buildApprovalBanner(), BorderLayout.NORTH);
        }

        cardLayout.show(contentPanel, "dashboard");
        setActiveNav(btnDashboard);
    }

    private JPanel buildApprovalBanner() {
        JPanel banner = new JPanel(new FlowLayout(FlowLayout.CENTER));
        banner.setBackground(new Color(255, 243, 205));
        banner.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 170, 12)));
        JLabel label = new JLabel("Waiting for admin approval — you can browse, but creating or editing events is disabled.");
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(102, 60, 0));
        banner.add(label);
        return banner;
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
        JPanel nav = AppShell.navigation("Organiser");
        navigation = nav;

        btnDashboard = navButton("Dashboard");
        btnMyEvents = navButton("My Events");
        btnNotifications = navButton("Notifications");
        btnLogout = navButton("Logout");

        btnDashboard.addActionListener(e -> switchTo("dashboard", btnDashboard));
        btnMyEvents.addActionListener(e -> switchTo("myEvents", btnMyEvents));
        btnNotifications.addActionListener(e -> switchTo("notifications", btnNotifications));
        btnLogout.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });

        nav.add(btnDashboard);
        nav.add(Box.createVerticalStrut(4));
        nav.add(btnMyEvents);
        nav.add(Box.createVerticalStrut(4));
        nav.add(btnNotifications);
        nav.add(Box.createVerticalGlue());
        nav.add(btnLogout);

        return nav;
    }

    private JButton navButton(String text) {
        String icon = switch (text) {
            case "Dashboard" -> "home";
            case "My Events" -> "calendar";
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

        contentPanel.add(buildDashboardPanel(), "dashboard");
        contentPanel.add(new MyEventsPanel(organiserId, readOnly), "myEvents");
        contentPanel.add(new OrganiserNotificationsPanel(organiserId), "notifications");

        return contentPanel;
    }

    private JPanel buildDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Welcome back");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(0, 51, 102));
        panel.add(title, BorderLayout.NORTH);

        // TODO: placeholder stat cards — these three big blocks are ugly.
        // We'll generate proper SVGs for the dashboard visuals later.
        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 20));
        cards.setBackground(Color.WHITE);
        cards.add(statCard("12", "Events created"));
        cards.add(statCard("8", "Currently open"));
        cards.add(statCard("245", "Tickets issued"));
        panel.add(cards, BorderLayout.CENTER);

        return panel;
    }

    private JPanel statCard(String number, String label) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JPanel inner = new JPanel();
        inner.setBackground(Color.WHITE);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel numberLabel = new JLabel(number, SwingConstants.CENTER);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 32));
        numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(label, SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(numberLabel);
        inner.add(Box.createVerticalStrut(10));
        inner.add(textLabel);
        card.add(inner);
        return card;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new OrganiserDashboard().setVisible(true));
    }
}