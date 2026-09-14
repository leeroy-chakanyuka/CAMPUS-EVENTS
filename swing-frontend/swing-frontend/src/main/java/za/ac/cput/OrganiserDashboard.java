package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class OrganiserDashboard extends JFrame {

    private Long organiserId;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    private static final Color SIDEBAR_BG = new Color(0, 51, 102);
    private static final Color SIDEBAR_ACTIVE = new Color(0, 71, 133);

    private JButton btnDashboard;
    private JButton btnMyEvents;
    private JButton btnNotifications;
    private JButton btnLogout;



    public OrganiserDashboard() {
        this(null);
    }

    public OrganiserDashboard(Long organiserId) {

        this.organiserId = organiserId;

        // ALL YOUR EXISTING CONSTRUCTOR CODE CONTINUES HERE
        setTitle("Campus Events - Organiser Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        setWindowIcon();

        add(buildNavigation(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);

        cardLayout.show(contentPanel, "dashboard");
        setActiveNav(btnDashboard);
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
        for (JButton b : new JButton[]{btnDashboard, btnMyEvents, btnNotifications}) {
            b.setBackground(b == active ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        }
    }

    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(buildDashboardPanel(), "dashboard");
        contentPanel.add(
                new MyEventsPanel(organiserId),
                "myEvents"
        );
        contentPanel.add(
                new OrganiserNotificationsPanel(organiserId),
                "notifications" );
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
