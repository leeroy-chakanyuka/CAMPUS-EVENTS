package za.ac.cput;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public StudentsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        seedRows();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Students");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Name", "Email", "Student #", "Faculty", "Status", "Action"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Action column only
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

        new TableButtonColumn(table, 5, this::handleStatusToggle);

        return scrollPane;
    }

    private void handleStatusToggle(int row) {
        // TODO: PUT /student/{id}/status here
        String currentStatus = tableModel.getValueAt(row, 4).toString();
        if (currentStatus.equals("Active")) {
            tableModel.setValueAt("Suspended", row, 4);
            tableModel.setValueAt("Reactivate", row, 5);
        } else {
            tableModel.setValueAt("Active", row, 4);
            tableModel.setValueAt("Suspend", row, 5);
        }
    }

    private void seedRows() {
        // TODO: GET /student here, response is List<StudentResponseDTO>
        // StudentResponseDTO fields: id, firstName, lastName, email, studentNumber, facultyName, active
        // Column mapping: firstName+" "+lastName -> col 0, email -> col 1,
        // studentNumber -> col 2, facultyName -> col 3,
        // active ? "Active" : "Suspended" -> col 4,
        // active ? "Suspend" : "Reactivate" -> col 5
        tableModel.addRow(new Object[]{"Sipho Nkosi", "sipho@mycput.ac.za", "219012345", "ICT", "Active", "Suspend"});
        tableModel.addRow(new Object[]{"Amahle Dube", "amahle@mycput.ac.za", "221098765", "Business", "Suspended", "Reactivate"});
    }
public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("StudentsPanel - standalone test");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new StudentsPanel());
            frame.setVisible(true);
        });
    }
}
