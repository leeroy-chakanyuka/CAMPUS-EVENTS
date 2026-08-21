package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MyEvents extends JFrame {

    private JTable eventsTable;
    private DefaultTableModel tableModel;


    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color LIGHT_GRAY = new Color(245, 245, 245);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public MyEvents() {

        setTitle("Campus Events - My Events");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createInterface();

        setVisible(true);
    }

    private void createInterface() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        mainPanel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("My Events");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(CPUT_BLUE);

        JButton createEventButton = new JButton("+ Create Event");
        createEventButton.setBackground(CPUT_RED);
        createEventButton.setForeground(Color.WHITE);
        createEventButton.setFocusPainted(false);
        createEventButton.setFont(new Font("Arial", Font.BOLD, 14));

        createEventButton.addActionListener(
                e -> new CreateEvent(this, -1, null)
        );

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(createEventButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // TABLE
        String[] columns = {
                "Title",
                "Venue",
                "Date",
                "Capacity",
                "Status",
                "Actions"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eventsTable = new JTable(tableModel);

        eventsTable.setRowHeight(35);
        eventsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        eventsTable.setForeground(DARK_TEXT);
        eventsTable.setBackground(Color.WHITE);
        eventsTable.setSelectionBackground(LIGHT_BLUE);
        eventsTable.setSelectionForeground(DARK_TEXT);
        eventsTable.setGridColor(new Color(220, 220, 220));

        eventsTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        eventsTable.getTableHeader().setBackground(CPUT_BLUE);
        eventsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(eventsTable);
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ACTION BUTTONS
        JPanel actionPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        actionPanel.setBackground(Color.WHITE);

        JButton editButton = new JButton("Edit");
        JButton closeButton = new JButton("Close Registration");

        editButton.setBackground(CPUT_BLUE);
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);

        closeButton.setBackground(CPUT_RED);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);

        editButton.addActionListener(
                e -> editSelectedEvent()
        );

        closeButton.addActionListener(
                e -> closeSelectedEvent()
        );

        actionPanel.add(editButton);
        actionPanel.add(closeButton);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // STATIC SEED DATA
        addEvent(
                "Tech Career Day",
                "CPUT Auditorium",
                "2026-09-15",
                "200",
                "Open"
        );

        addEvent(
                "Chess Tournament",
                "Student Centre",
                "2026-09-20",
                "50",
                "Open"
        );

        addEvent(
                "Java Workshop",
                "Computer Lab 2",
                "2026-09-25",
                "30",
                "Closed"
        );
    }

    public void addEvent(
            String title,
            String venue,
            String date,
            String capacity,
            String status
    ) {

        tableModel.addRow(
                new Object[]{
                        title,
                        venue,
                        date,
                        capacity,
                        status,
                        status.equals("Open")
                                ? "Edit / Close"
                                : "Edit"
                }
        );
    }

    private void editSelectedEvent() {

        int selectedRow = eventsTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event first."
            );

            return;
        }

        String title = tableModel.getValueAt(selectedRow, 0).toString();
        String venue = tableModel.getValueAt(selectedRow, 1).toString();
        String date = tableModel.getValueAt(selectedRow, 2).toString();
        String capacity = tableModel.getValueAt(selectedRow, 3).toString();

        new CreateEvent(
                this,
                selectedRow,
                new String[]{
                        title,
                        venue,
                        date,
                        capacity
                }
        );
    }

    public void updateEvent(
            int row,
            String title,
            String venue,
            String date,
            String capacity
    ) {

        tableModel.setValueAt(title, row, 0);
        tableModel.setValueAt(venue, row, 1);
        tableModel.setValueAt(date, row, 2);
        tableModel.setValueAt(capacity, row, 3);

        refreshEvents();
    }

    private void closeSelectedEvent() {

        int selectedRow = eventsTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event first."
            );

            return;
        }

        String status =
                tableModel.getValueAt(selectedRow, 4).toString();

        if (!status.equals("Open")) {

            JOptionPane.showMessageDialog(
                    this,
                    "This event is already closed."
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Close registration for this event?",
                "Close Registration",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            tableModel.setValueAt(
                    "Closed",
                    selectedRow,
                    4
            );

            tableModel.setValueAt(
                    "Closed",
                    selectedRow,
                    5
            );

            refreshEvents();
        }
    }

    public void refreshEvents() {

        eventsTable.revalidate();
        eventsTable.repaint();
    }

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    new FlatLightLaf()
            );

        } catch (UnsupportedLookAndFeelException e) {

            e.printStackTrace();
        }

        SwingUtilities.invokeLater(
                MyEvents::new
        );
    }
}