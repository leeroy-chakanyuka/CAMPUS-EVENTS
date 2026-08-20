package za.ac.cput;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OrganisersPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public OrganisersPanel() {
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

        JLabel title = new JLabel("Organisers");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Name", "Email", "Faculty", "Status", "Action"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Action column only
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

        new TableButtonColumn(table, 4, this::handleStatusToggle);

        return scrollPane;
    }

    private void handleStatusToggle(int row) {
        // TODO: PUT /organiser/{id}/status here — see sprint-scope-explicit.md
        String currentStatus = tableModel.getValueAt(row, 3).toString();
        if (currentStatus.equals("Active")) {
            tableModel.setValueAt("Suspended", row, 3);
            tableModel.setValueAt("Reactivate", row, 4);
        } else {
            tableModel.setValueAt("Active", row, 3);
            tableModel.setValueAt("Suspend", row, 4);
        }
    }

    private void seedRows() {
        // TODO: GET /organiser here, response is List<OrganiserResponseDTO>
        // OrganiserResponseDTO fields: id, firstName, lastName, email, facultyName, active
        // Column mapping: firstName+" "+lastName -> col 0, email -> col 1,
        // facultyName -> col 2, active ? "Active" : "Suspended" -> col 3,
        // active ? "Suspend" : "Reactivate" -> col 4
        tableModel.addRow(new Object[]{"Thandeka Zulu", "thandeka@cput.ac.za", "ICT", "Active", "Suspend"});
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("OrganisersPanel - standalone test");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new OrganisersPanel());
            frame.setVisible(true);
        });
    }
}
