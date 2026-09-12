package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateEvent extends JFrame {

    private final MyEvents myEvents;
    private final int editingRow;
    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JComboBox<String> venueComboBox;
    private final JTextField dateField;
    private final JTextField capacityField;

    private final boolean editMode;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);

    public CreateEvent(
            MyEvents myEvents,
            int editingRow,
            String[] existingEvent
    ) {

        this.myEvents = myEvents;
        this.editingRow = editingRow;

        editMode = existingEvent != null;

        setTitle(
                editMode
                        ? "Campus Events - Edit Event"
                        : "Campus Events - Create Event"
        );

        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        mainPanel.setBorder(
                new EmptyBorder(25, 30, 25, 30)
        );

        JLabel heading = new JLabel(
                editMode
                        ? "Edit Event"
                        : "Create Event"
        );

        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(CPUT_BLUE);

        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(
                new GridBagLayout()
        );

        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(new JLabel("Title:"), gbc);

        titleField = new JTextField();

        gbc.gridx = 1;

        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(
                new JLabel("Description:"),
                gbc
        );

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane descriptionScrollPane =
                new JScrollPane(descriptionArea);

        gbc.gridx = 1;

        formPanel.add(
                descriptionScrollPane,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(
                new JLabel("Venue:"),
                gbc
        );

        venueComboBox = new JComboBox<>(
                new String[]{
                        "CPUT Auditorium",
                        "Student Centre",
                        "Computer Lab 1",
                        "Computer Lab 2",
                        "MultiPurpose Hall",
                        "Library"
                }
        );

        gbc.gridx = 1;

        formPanel.add(
                venueComboBox,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(
                new JLabel("Date:"),
                gbc
        );

        dateField = new JTextField();

        dateField.setToolTipText(
                "Example: 2026-10-15"
        );

        gbc.gridx = 1;

        formPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(
                new JLabel("Capacity:"),
                gbc
        );

        capacityField = new JTextField();

        gbc.gridx = 1;

        formPanel.add(capacityField, gbc);

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        buttonPanel.setBackground(Color.WHITE);

        JButton cancelButton = new JButton("Cancel");

        JButton submitButton = new JButton(
                editMode
                        ? "Save Changes"
                        : "Create Event"
        );

        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);

        submitButton.setBackground(CPUT_RED);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);

        cancelButton.addActionListener(
                e -> dispose()
        );

        submitButton.addActionListener(
                e -> submitEvent()
        );

        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        if (editMode) {
            loadExistingEvent(existingEvent);
        }

        setVisible(true);
    }

    private void loadExistingEvent(
            String[] existingEvent
    ) {

        titleField.setText(existingEvent[0]);

        venueComboBox.setSelectedItem(
                existingEvent[1]
        );

        dateField.setText(existingEvent[2]);

        capacityField.setText(existingEvent[3]);
    }

    private void submitEvent() {

        String title =
                titleField.getText().trim();

        String date =
                dateField.getText().trim();

        String capacity =
                capacityField.getText().trim();

        if (title.isEmpty()
                || date.isEmpty()
                || capacity.isEmpty()) {

            return;
        }

        String venue =
                venueComboBox
                        .getSelectedItem()
                        .toString();

        if (!editMode) {

            myEvents.addEvent(
                    title,
                    venue,
                    date,
                    capacity,
                    "Open"
            );

            myEvents.refreshEvents();

            dispose();

            return;
        }

        myEvents.updateEvent(
                editingRow,
                title,
                venue,
                date,
                capacity
        );

        dispose();
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
                () -> new CreateEvent(null, -1, null)
        );
    }
}