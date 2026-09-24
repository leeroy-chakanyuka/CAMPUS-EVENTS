package za.ac.cput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MyEventsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    private CardLayout cardLayout;
    private JPanel cardHolder;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> venueComboBox;
    private JTextField dateField;
    private JTextField capacityField;
    private int editingRow = -1;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public MyEventsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        cardLayout = new CardLayout();
        cardHolder = new JPanel(cardLayout);
        cardHolder.setBackground(Color.WHITE);

        cardHolder.add(buildListCard(), "list");
        cardHolder.add(buildFormCard(), "form");

        add(cardHolder, BorderLayout.CENTER);
        seedRows();
        cardLayout.show(cardHolder, "list");
    }

    // ---------------- LIST CARD ----------------

    private JPanel buildListCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);

        JLabel title = new JLabel("My Events");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        JButton createButton = new JButton("+ Create Event");
        createButton.setBackground(CPUT_RED);
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        createButton.addActionListener(e -> openForm(-1));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        header.add(createButton, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);
        card.add(buildTable(), BorderLayout.CENTER);
        return card;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Title", "Venue", "Date", "Capacity", "Status", "Edit", "Close"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // Edit and Close columns only
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setForeground(DARK_TEXT);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(LIGHT_BLUE);
        table.setSelectionForeground(DARK_TEXT);
        table.setGridColor(new Color(220, 220, 220));

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(CPUT_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        new TableButtonColumn(table, 5, row -> openForm(row));
        // Close registration is a one-way commit: once it flips to "Closed" the
        // cell stops being a button and renders as static red text
        new TableButtonColumn(table, 6, this::handleClose, CPUT_RED, "Closed");
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusBadge());

        return scrollPane;
    }

    // ---------------- FORM CARD (create/edit, one form two modes) ----------------

    private JPanel buildFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Create Event");
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(CPUT_BLUE);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        heading.setName("formHeading");

        titleField = new JTextField();
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        venueComboBox = new JComboBox<>(new String[]{
                "CPUT Auditorium", "Student Centre", "Computer Lab 1",
                "Computer Lab 2", "MultiPurpose Hall", "Library"
        });
        dateField = new JTextField();
        dateField.setToolTipText("Example: 2026-10-15");
        capacityField = new JTextField();

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBackground(Color.WHITE);
        fields.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0; gbc.gridy = 0;
        fields.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        fields.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        fields.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        fields.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0; gbc.gridy++;
        fields.add(new JLabel("Venue:"), gbc);
        gbc.gridx = 1;
        fields.add(venueComboBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        fields.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        fields.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        fields.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        fields.add(capacityField, gbc);

        JButton saveButton = new JButton("Create Event");
        saveButton.setName("saveButton");
        saveButton.setBackground(CPUT_RED);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(e -> submitEvent());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> cardLayout.show(cardHolder, "list"));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBackground(Color.WHITE);
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttons.add(saveButton);
        buttons.add(cancelButton);

        card.add(heading);
        card.add(Box.createVerticalStrut(16));
        card.add(fields);
        card.add(buttons);
        return card;
    }

    private void openForm(int row) {
        editingRow = row;
        Component[] comps = ((JPanel) cardHolder.getComponent(1)).getComponents();
        JLabel heading = null;
        JButton saveButton = null;
        for (Component c : comps) {
            if ("formHeading".equals(c.getName())) heading = (JLabel) c;
            if ("saveButton".equals(c.getName())) saveButton = (JButton) c;
        }

        if (row == -1) {
            titleField.setText("");
            descriptionArea.setText("");
            venueComboBox.setSelectedIndex(0);
            dateField.setText("");
            capacityField.setText("");
            if (heading != null) heading.setText("Create Event");
            if (saveButton != null) saveButton.setText("Create Event");
        } else {
            titleField.setText(tableModel.getValueAt(row, 0).toString());
            venueComboBox.setSelectedItem(tableModel.getValueAt(row, 1).toString());
            dateField.setText(tableModel.getValueAt(row, 2).toString());
            capacityField.setText(tableModel.getValueAt(row, 3).toString());
            if (heading != null) heading.setText("Edit Event");
            if (saveButton != null) saveButton.setText("Save Changes");
        }

        cardLayout.show(cardHolder, "form");
    }

    private void submitEvent() {
        // TODO: POST /event (create) or PUT /event/{id} (update) here — see sprint-scope-explicit.md
        String title = titleField.getText().trim();
        String date = dateField.getText().trim();
        String capacity = capacityField.getText().trim();

        if (title.isEmpty() || date.isEmpty() || capacity.isEmpty()) {
            return;
        }

        String venue = venueComboBox.getSelectedItem().toString();

        if (editingRow == -1) {
            tableModel.addRow(new Object[]{title, venue, date, capacity, "Open", "Edit", "Close registration"});
        } else {
            tableModel.setValueAt(title, editingRow, 0);
            tableModel.setValueAt(venue, editingRow, 1);
            tableModel.setValueAt(date, editingRow, 2);
            tableModel.setValueAt(capacity, editingRow, 3);
        }

        cardLayout.show(cardHolder, "list");
    }

    private void handleClose(int row) {
        // TODO: PUT /event/{id}/close here — see sprint-scope-explicit.md
        String status = tableModel.getValueAt(row, 4).toString();
        if (status.equals("Closed")) {
            return; // terminal
        }
        tableModel.setValueAt("Closed", row, 4);
        tableModel.setValueAt("Closed", row, 6);
    }

    private void seedRows() {
        // TODO: GET /event/organiser/{organiserId} here — see sprint-scope-explicit.md
        tableModel.addRow(new Object[]{"Tech Career Day", "CPUT Auditorium", "2026-09-15", "200", "Open", "Edit", "Close registration"});
        tableModel.addRow(new Object[]{"Chess Tournament", "Student Centre", "2026-09-20", "50", "Open", "Edit", "Close registration"});
        tableModel.addRow(new Object[]{"Java Workshop", "Computer Lab 2", "2026-09-25", "30", "Closed", "Edit", "Closed"});
    }
}
