package za.ac.cput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;

public class NotificationsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);
    private static final Color UNREAD_BG = new Color(255, 249, 230);

    public NotificationsPanel() {
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

        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        header.add(title, BorderLayout.WEST);
        return header;

    }

    private JScrollPane buildTable() {
        String[] columns = {"Message", "Received", "Status", "Action"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Action column only
            }
        };

        table = new JTable(tableModel) {
            // unread rows get a highlight background — read rows stay plain white
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    String status = getValueAt(row, 2).toString();
                    c.setBackground(status.equals("Unread") ? UNREAD_BG : Color.WHITE);
                }
                return c;
            }
        };

        table.setRowHeight(38);
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

        new TableButtonColumn(table, 3, this::handleMarkAsRead);

        return scrollPane;
    }

    private void handleMarkAsRead(int row) {
        // TODO: PUT /notification/{id}/read here
        String currentStatus = tableModel.getValueAt(row, 2).toString();
        if (currentStatus.equals("Read")) {
            return; // already read, no toggle back
        }
        tableModel.setValueAt("Read", row, 2);
        tableModel.setValueAt("—", row, 3);
        table.repaint();
    }

    private void seedRows() {
        // TODO: GET /notification?recipientId=&recipientType=ADMIN here,
        // response is List<NotificationResponseDTO>
        // NotificationResponseDTO fields: id, message, recipientId, recipientType, read, createdAt
        // Column mapping: message -> col 0, createdAt (formatted) -> col 1,
        // read ? "Read" : "Unread" -> col 2, read ? "—" : "Mark as read" -> col 3
        tableModel.addRow(new Object[]{"New organiser signup pending review", "2 min ago", "Unread", "Mark as read"});
        tableModel.addRow(new Object[]{"Faculty of Fashion status changed to Active", "1 hour ago", "Unread", "Mark as read"});
        tableModel.addRow(new Object[]{"3 students suspended this week", "Yesterday", "Read", "—"});
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("NotificationsPanel - standalone test");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new NotificationsPanel());
            frame.setVisible(true);
        });
    }
}
