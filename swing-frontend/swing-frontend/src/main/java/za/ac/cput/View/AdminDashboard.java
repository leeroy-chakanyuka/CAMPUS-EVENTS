package za.ac.cput.View;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private JPanel navigationPanel;
    private JPanel contentPanel;

    private JButton btnFaculty;
    private JButton btnStudents;
    private JButton btnOrganisers;
    private JButton btnEvents;
    private JButton btnReports;
    private JButton btnLogout;

    public AdminDashboard() {

        setTitle("Campus Events - Admin Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createHeader();
        createNavigation();
        createContent();

        setVisible(true);
    }

    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(32, 55, 100));
        header.setPreferredSize(new Dimension(1200, 60));

        JLabel title = new JLabel("Campus Events Administration");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);
    }

    private void createNavigation() {

        navigationPanel = new JPanel();
        navigationPanel.setLayout(new GridLayout(6,1,5,5));
        navigationPanel.setPreferredSize(new Dimension(220,700));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        btnFaculty = new JButton("Faculty");
        btnStudents = new JButton("Students");
        btnOrganisers = new JButton("Organisers");
        btnEvents = new JButton("Events");
        btnReports = new JButton("Reports");
        btnLogout = new JButton("Logout");

        navigationPanel.add(btnFaculty);
        navigationPanel.add(btnStudents);
        navigationPanel.add(btnOrganisers);
        navigationPanel.add(btnEvents);
        navigationPanel.add(btnReports);
        navigationPanel.add(btnLogout);

        add(navigationPanel, BorderLayout.WEST);
    }

    private void createContent() {

        contentPanel = new JPanel(new BorderLayout());

        JLabel welcome = new JLabel("Welcome Administrator", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subtitle = new JLabel(
                "Select an option from the menu.",
                SwingConstants.CENTER);

        subtitle.setFont(new Font("Arial", Font.PLAIN,18));

        JPanel center = new JPanel();
        center.setLayout(new GridLayout(2,1));

        center.add(welcome);
        center.add(subtitle);

        contentPanel.add(center, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    public JButton getBtnFaculty() {
        return btnFaculty;
    }

    public JButton getBtnStudents() {
        return btnStudents;
    }

    public JButton getBtnOrganisers() {
        return btnOrganisers;
    }

    public JButton getBtnEvents() {
        return btnEvents;
    }

    public JButton getBtnReports() {
        return btnReports;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }
}