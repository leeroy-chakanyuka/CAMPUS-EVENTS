package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Notifications extends JFrame {

    private JComboBox<String> eventComboBox;
    private JComboBox<String> attendeeComboBox;
    private JTextArea messageArea;

    private JTable notificationTable;
    private DefaultTableModel tableModel;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);

    public Notifications() {

        setTitle("Campus Events - Notifications");
        setSize(1200, 700);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);
        setResizable(false);

        createInterface();

        setVisible(true);
    }

    private void createInterface() {

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(Color.WHITE);

        mainPanel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        JLabel titleLabel =
                new JLabel("Notifications");

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        titleLabel.setForeground(CPUT_BLUE);

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                new JLabel("Event:"),
                gbc
        );

        eventComboBox =
                new JComboBox<>(
                        new String[]{
                                "Tech Career Day",
                                "Chess Tournament",
                                "Java Workshop"
                        }
                );

        gbc.gridx = 1;

        formPanel.add(
                eventComboBox,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(
                new JLabel("Attendee:"),
                gbc
        );

        attendeeComboBox =
                new JComboBox<>(
                        new String[]{
                                "Thabo Mokoena",
                                "Lerato Mthembu",
                                "Mpho Baloyi",
                                "John Smith"
                        }
                );

        gbc.gridx = 1;

        formPanel.add(
                attendeeComboBox,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy++;

        gbc.anchor =
                GridBagConstraints.NORTHWEST;

        formPanel.add(
                new JLabel("Message:"),
                gbc
        );

        messageArea =
                new JTextArea(5, 30);

        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JScrollPane messageScrollPane =
                new JScrollPane(messageArea);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;

        formPanel.add(
                messageScrollPane,
                gbc
        );

        JButton sendButton =
                new JButton("Send");

        sendButton.setBackground(CPUT_RED);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);

        sendButton.addActionListener(
                e -> sendNotification()
        );

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        formPanel.add(
                sendButton,
                gbc
        );

        mainPanel.add(
                formPanel,
                BorderLayout.NORTH
        );

        String[] columns = {
                "Event",
                "Attendee",
                "Message"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        notificationTable =
                new JTable(tableModel);

        notificationTable.setRowHeight(30);
        notificationTable.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        notificationTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        notificationTable.getTableHeader()
                .setBackground(CPUT_BLUE);

        notificationTable.getTableHeader()
                .setForeground(Color.WHITE);

        JScrollPane tableScrollPane =
                new JScrollPane(notificationTable);

        mainPanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        add(mainPanel);
    }

    private void sendNotification() {

        String message =
                messageArea
                        .getText()
                        .trim();

        if (message.isEmpty()) {
            return;
        }

        String event =
                eventComboBox
                        .getSelectedItem()
                        .toString();

        String attendee =
                attendeeComboBox
                        .getSelectedItem()
                        .toString();

        tableModel.addRow(
                new Object[]{
                        event,
                        attendee,
                        message
                }
        );

        messageArea.setText("");

        notificationTable.revalidate();
        notificationTable.repaint();
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
                Notifications::new
        );
    }
}