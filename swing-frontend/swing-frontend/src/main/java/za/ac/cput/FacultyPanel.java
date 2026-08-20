package za.ac.cput;



import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FacultyPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtName;
    private JTextField txtEmail;

    // same palette as MyEvents.java — one visual identity, not five
    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    private static final int LABEL_WIDTH = 140;
    private static final int FIELD_WIDTH = 260;
    private static final int FIELD_HEIGHT = 34;

    public FacultyPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
        add(buildCreateForm(), BorderLayout.SOUTH);

        seedRows();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Faculties");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Name", "Contact Email", "Status", "Action"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Action column only — needed so the button actually fires
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

        new TableButtonColumn(table, 3, this::handleStatusToggle);

        return scrollPane;
    }

    private void handleStatusToggle(int row) {
        // TODO: PUT /faculty/{id}/status
        String currentStatus = tableModel.getValueAt(row, 2).toString();
        if (currentStatus.equals("Active")) {
            tableModel.setValueAt("Inactive", row, 2);
            tableModel.setValueAt("Activate", row, 3);
        } else {
            tableModel.setValueAt("Active", row, 2);
            tableModel.setValueAt("Deactivate", row, 3);
        }
    }

    private JPanel buildCreateForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                new EmptyBorder(16, 0, 0, 0)
        ));

        JLabel formTitle = new JLabel("Create Faculty");
        formTitle.setFont(new Font("Arial", Font.BOLD, 15));
        formTitle.setForeground(CPUT_BLUE);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtName = new JTextField();
        txtEmail = new JTextField();

        JPanel fieldsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        fieldsRow.setBackground(Color.WHITE);
        fieldsRow.add(labeledField("Faculty name", txtName));
        fieldsRow.add(labeledField("Contact email", txtEmail));

        JButton createButton = new JButton("Create Faculty");
        createButton.setBackground(CPUT_RED);
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        createButton.addActionListener(e -> handleCreate());

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonRow.setBackground(Color.WHITE);
        buttonRow.add(createButton);

        form.add(formTitle);
        form.add(Box.createVerticalStrut(10));
        form.add(fieldsRow);
        form.add(buttonRow);
        return form;
    }

    private JPanel labeledField(String labelText, JTextField field) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(new Color(120, 120, 120));

        field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setMaximumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setFont(new Font("Arial", Font.PLAIN, 13));

        wrapper.add(label);
        wrapper.add(Box.createVerticalStrut(4));
        wrapper.add(field);
        return wrapper;
    }

    private void handleCreate() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();

        if (name.isEmpty() || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            JOptionPane.showMessageDialog(this, "Enter a valid faculty name and email.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO: POST /faculty here
        tableModel.addRow(new Object[]{name, email, "Active", "Deactivate"});
        txtName.setText("");
        txtEmail.setText("");
    }

    private void seedRows() {
        // TODO: GET /faculty here, response is List<FacultyResponseDTO>
        // FacultyResponseDTO fields: id, name, contactEmail, active
        // Column mapping: name -> col 0, contactEmail -> col 1,
        // active ? "Active" : "Inactive" -> col 2,
        // active ? "Deactivate" : "Activate" -> col 3
        tableModel.addRow(new Object[]{"Faculty of ICT", "ict@cput.ac.za", "Active", "Deactivate"});
        tableModel.addRow(new Object[]{"Faculty of Business", "business@cput.ac.za", "Inactive", "Activate"});
    }
public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FacultyPanel - standalone test");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new FacultyPanel());
            frame.setVisible(true);
        });
    }
}
