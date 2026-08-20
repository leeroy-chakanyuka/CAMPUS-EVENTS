package za.ac.cput;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EventsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);

    public EventsPanel() {
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

        JLabel title = new JLabel("Events (cross-faculty)");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Title", "Faculty", "Organiser", "Date", "Status", "Action"};

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

        new TableButtonColumn(table, 5, this::handleForceCancel);

        return scrollPane;
    }

    private void handleForceCancel(int row) {
        // TODO: PUT /event/{id}/force-cancel here
        String currentStatus = tableModel.getValueAt(row, 4).toString();
        if (currentStatus.equals("Cancelled")) {
            return;
        }
        tableModel.setValueAt("Cancelled", row, 4);
        tableModel.setValueAt("Cancelled", row, 5);
    }

    private void seedRows() {
        // TODO: GET /event here, response is List<EventResponseDTO>
        // EventResponseDTO fields: id, title, description, eventDate, capacity,
        // open, venueName, organiserName, facultyName, createdAt
        // Column mapping: title -> col 0, facultyName -> col 1, organiserName -> col 2,
        // eventDate -> col 3, open ? "Open" : "Cancelled" -> col 4,
        // open ? "Force cancel" : "Cancelled" -> col 5
        // force cancel should just flip the open boolean to false, will make sure we enforce something on the db to make it
        // impossible to change it back once cancelled
        tableModel.addRow(new Object[]{"Career Fair 2026", "Business", "Thandeka Zulu", "20 Sep", "Open", "Force cancel"});
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("EventsPanel - standalone test");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new EventsPanel());
            frame.setVisible(true);
        });
    }
}
