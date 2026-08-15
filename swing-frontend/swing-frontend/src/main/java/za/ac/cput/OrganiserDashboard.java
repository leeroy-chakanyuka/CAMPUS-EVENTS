package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class OrganiserDashboard extends JFrame {

    private JPanel contentPanel;
    private JPanel navigationPanel;
    private JLabel breadcrumbLabel;
    private JButton notificationButton;
    private JPopupMenu notificationMenu;

    private final Map<String, JButton> navigationButtons = new HashMap<>();

    public OrganiserDashboard() {

        setTitle("Campus Events - Organiser");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout());

        createSidebar();
        createTopBar();
        createContent();

        showDashboard();

        setVisible(true);
    }

    // =========================
    // SIDEBAR
    // =========================

    private void createSidebar() {

        navigationPanel = new JPanel();

        navigationPanel.setLayout(
                new BoxLayout(navigationPanel, BoxLayout.Y_AXIS)
        );

        navigationPanel.setPreferredSize(
                new Dimension(220, 700)
        );

        navigationPanel.setBorder(
                new EmptyBorder(20, 15, 20, 15)
        );

        JLabel appName = new JLabel("Campus Events");

        appName.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        appName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel("Organiser");

        roleLabel.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        navigationPanel.add(appName);
        navigationPanel.add(Box.createVerticalStrut(5));
        navigationPanel.add(roleLabel);
        navigationPanel.add(Box.createVerticalStrut(30));

        addNavigationButton("Dashboard");
        addNavigationButton("My Events");
        addNavigationButton("Notifications");

        navigationPanel.add(Box.createVerticalGlue());

        JButton logoutButton = new JButton("Logout");

        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        logoutButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 40)
        );

        logoutButton.addActionListener(e -> logout());

        navigationPanel.add(logoutButton);

        add(navigationPanel, BorderLayout.WEST);
    }

    private void addNavigationButton(String name) {

        JButton button = new JButton(name);

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 40)
        );

        button.addActionListener(e -> navigate(name));

        navigationButtons.put(name, button);

        navigationPanel.add(button);

        navigationPanel.add(
                Box.createVerticalStrut(8)
        );
    }

    // =========================
    // NAVIGATION
    // =========================

    private void navigate(String destination) {

        switch (destination) {

            case "Dashboard":
                showDashboard();
                break;

            case "My Events":
                openMyEvents();
                break;

            case "Notifications":
                new Notifications();
                break;

            default:
                break;
        }
    }

    // =========================
    // TOP BAR
    // =========================

    private void createTopBar() {

        JPanel topBar = new JPanel(
                new BorderLayout()
        );

        topBar.setPreferredSize(
                new Dimension(980, 60)
        );

        topBar.setBorder(
                new EmptyBorder(10, 20, 10, 20)
        );

        breadcrumbLabel = new JLabel(
                "Dashboard"
        );

        breadcrumbLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        JPanel rightPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        notificationButton = new JButton("🔔");

        notificationButton.setToolTipText(
                "Notifications"
        );

        createNotificationMenu();

        notificationButton.addActionListener(e ->
                notificationMenu.show(
                        notificationButton,
                        0,
                        notificationButton.getHeight()
                )
        );

        JLabel userChip = new JLabel(
                "  Organiser  "
        );

        userChip.setBorder(
                BorderFactory.createLineBorder(
                        Color.GRAY
                )
        );

        rightPanel.add(notificationButton);
        rightPanel.add(userChip);

        topBar.add(
                breadcrumbLabel,
                BorderLayout.WEST
        );

        topBar.add(
                rightPanel,
                BorderLayout.EAST
        );

        add(
                topBar,
                BorderLayout.NORTH
        );
    }

    // =========================
    // NOTIFICATION DROPDOWN
    // =========================

    private void createNotificationMenu() {

        notificationMenu = new JPopupMenu();

        JMenuItem notification1 =
                new JMenuItem(
                        "Event registrations opened"
                );

        JMenuItem notification2 =
                new JMenuItem(
                        "New event reminder"
                );

        JMenuItem notification3 =
                new JMenuItem(
                        "System notification"
                );

        notificationMenu.add(notification1);
        notificationMenu.add(notification2);
        notificationMenu.add(notification3);
    }

    // =========================
    // CONTENT PANEL
    // =========================

    private void createContent() {

        contentPanel = new JPanel(
                new BorderLayout()
        );

        contentPanel.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        add(
                contentPanel,
                BorderLayout.CENTER
        );
    }

    // =========================
    // DASHBOARD
    // =========================

    private void showDashboard() {

        contentPanel.removeAll();

        breadcrumbLabel.setText(
                "Dashboard"
        );

        JPanel dashboardPanel =
                new JPanel(
                        new BorderLayout()
                );

        dashboardPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JLabel title =
                new JLabel(
                        "Organiser Dashboard"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        dashboardPanel.add(
                title,
                BorderLayout.NORTH
        );

        JPanel cardsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                20,
                                20
                        )
                );

        cardsPanel.add(
                createStatCard(
                        "12",
                        "Events created"
                )
        );

        cardsPanel.add(
                createStatCard(
                        "8",
                        "Currently open"
                )
        );

        cardsPanel.add(
                createStatCard(
                        "245",
                        "Tickets issued"
                )
        );

        dashboardPanel.add(
                cardsPanel,
                BorderLayout.CENTER
        );

        contentPanel.add(
                dashboardPanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================
    // STATISTIC CARD
    // =========================

    private JPanel createStatCard(
            String number,
            String label
    ) {

        JPanel card =
                new JPanel(
                        new GridBagLayout()
                );

        card.setBorder(
                BorderFactory.createLineBorder(
                        Color.GRAY
                )
        );

        JPanel inner =
                new JPanel();

        inner.setLayout(
                new BoxLayout(
                        inner,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel numberLabel =
                new JLabel(
                        number,
                        SwingConstants.CENTER
                );

        numberLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        32
                )
        );

        numberLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel labelLabel =
                new JLabel(
                        label,
                        SwingConstants.CENTER
                );

        labelLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        labelLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        inner.add(numberLabel);

        inner.add(
                Box.createVerticalStrut(10)
        );

        inner.add(labelLabel);

        card.add(inner);

        return card;
    }

    // =========================
    // OPEN MY EVENTS
    // =========================

    private void openMyEvents() {

        new MyEvents().setVisible(true);

        dispose();
    }

    // =========================
    // OPEN NOTIFICATIONS
    // =========================

    private void openNotifications() {

        new Notifications().setVisible(true);

        dispose();
    }

    // =========================
    // LOGOUT
    // =========================

    private void logout() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice == JOptionPane.YES_OPTION) {

            dispose();


        }
    }

    // =========================
    // MAIN
    // =========================

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    new FlatLightLaf()
            );

        } catch (
                UnsupportedLookAndFeelException e
        ) {

            e.printStackTrace();
        }

        SwingUtilities.invokeLater(
                OrganiserDashboard::new
        );
    }
}