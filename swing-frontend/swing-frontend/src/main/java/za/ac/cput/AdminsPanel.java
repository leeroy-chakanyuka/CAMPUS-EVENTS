package za.ac.cput;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JPasswordField pwdTemp;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    private static final int FIELD_WIDTH = 200;
    private static final int FIELD_HEIGHT = 34;

    public AdminsPanel() {
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

        JLabel title = new JLabel("Admins");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Name", "Email", "Action"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Action column only
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

        new TableButtonColumn(table, 2, this::handleChangePassword);

        return scrollPane;
    }

    private void handleChangePassword(int row) {
        // TODO: PUT /admin/change-password here - also write the JOPtionPane flow to grab the neede data and change pwd
        JOptionPane.showMessageDialog(this, "Change password flow not built yet — placeholder click.");
    }

    private JPanel buildCreateForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                new EmptyBorder(16, 0, 0, 0)
        ));

        JLabel formTitle = new JLabel("Create Admin");
        formTitle.setFont(new Font("Arial", Font.BOLD, 15));
        formTitle.setForeground(CPUT_BLUE);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtEmail = new JTextField();
        pwdTemp = new JPasswordField();

        JPanel fieldsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        fieldsRow.setBackground(Color.WHITE);
        fieldsRow.add(labeledField("First name", txtFirstName));
        fieldsRow.add(labeledField("Last name", txtLastName));
        fieldsRow.add(labeledField("Email", txtEmail));
        fieldsRow.add(labeledField("Temporary password", pwdTemp));

        JButton createButton = new JButton("Create Admin");
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
        String first = txtFirstName.getText().trim();
        String last = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();

        if (first.isEmpty() || last.isEmpty() || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            JOptionPane.showMessageDialog(this, "Enter a valid name and email.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO: POST /admin here
        tableModel.addRow(new Object[]{first + " " + last, email, "Change password"});
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        pwdTemp.setText("");
    }

    private void seedRows() {
        // TODO: GET /admin here, response is List<AdminResponseDTO>
        // AdminResponseDTO fields: id, firstName, lastName, email (NO the password)
        // Column mapping: firstName+" "+lastName -> col 0, email -> col 1,
        // "Change password" -> col 2 (static label — action only enabled for own row,
        // check id against the currently logged-in admin's id before wiring the click)
        tableModel.addRow(new Object[]{"You", "you@cput.ac.za", "Change password"});
    }
public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("AdminsPanel - standalone test");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new AdminsPanel());
            frame.setVisible(true);
        });
    }
}
