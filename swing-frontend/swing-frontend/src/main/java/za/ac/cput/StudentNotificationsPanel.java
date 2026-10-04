package za.ac.cput;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.DTO.NotificationResponseDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class StudentNotificationsPanel extends JPanel {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final int RELOAD_INTERVAL_MS = 7 * 60 * 1000;

    private final Long studentId;
    private final List<Long> notificationIds = new ArrayList<>();
    private final Timer reloadTimer;

    private JTable inboxTable;
    private DefaultTableModel inboxModel;

    private CardLayout cardLayout;
    private JPanel cardHolder;

    private JLabel detailMessage;
    private JLabel detailReceived;
    private JLabel detailStatus;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);
    private static final Color UNREAD_BG = new Color(255, 249, 230);

    public StudentNotificationsPanel() {
        this(null);
    }

    public StudentNotificationsPanel(Long studentId) {
        this.studentId = studentId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        cardLayout = new CardLayout();
        cardHolder = new JPanel(cardLayout);
        cardHolder.setBackground(Color.WHITE);

        cardHolder.add(buildInboxCard(), "inbox");
        cardHolder.add(buildDetailCard(), "detail");

        add(cardHolder, BorderLayout.CENTER);
        SwingUtilities.invokeLater(this::loadInbox);
        reloadTimer = new Timer(RELOAD_INTERVAL_MS, e -> loadInbox());
        reloadTimer.setRepeats(true);
        reloadTimer.start();
        cardLayout.show(cardHolder, "inbox");
    }

    // ---------------- INBOX (receive-only) ----------------

    private JPanel buildInboxCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);

        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(CPUT_BLUE);

        JButton refreshButton = new JButton("↻ Refresh");
        refreshButton.setBackground(CPUT_BLUE);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.addActionListener(e -> loadInbox());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);
        card.add(buildInboxTable(), BorderLayout.CENTER);
        return card;
    }

    private JScrollPane buildInboxTable() {
        String[] columns = {"Message", "Received", "Status", "Action"};

        inboxModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        inboxTable = new JTable(inboxModel) {
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

        inboxTable.setRowHeight(38);
        inboxTable.setFont(new Font("Arial", Font.PLAIN, 13));
        inboxTable.setForeground(DARK_TEXT);
        inboxTable.setBackground(Color.WHITE);
        inboxTable.setSelectionBackground(LIGHT_BLUE);
        inboxTable.setSelectionForeground(DARK_TEXT);
        inboxTable.setGridColor(new Color(220, 220, 220));

        inboxTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        inboxTable.getTableHeader().setBackground(CPUT_BLUE);
        inboxTable.getTableHeader().setForeground(Color.WHITE);

        inboxTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = inboxTable.rowAtPoint(e.getPoint());
                    int col = inboxTable.columnAtPoint(e.getPoint());
                    if (row >= 0 && col != 3) openNotification(row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(inboxTable);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Mark as read is one-way: once read the action becomes static "—"
        new TableButtonColumn(inboxTable, 3, this::handleMarkAsRead, "—");
        inboxTable.getColumnModel().getColumn(2).setCellRenderer(new StatusBadge());

        return scrollPane;
    }

    // ---------------- DETAIL (open notification) ----------------

    private JPanel buildDetailCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        JButton backButton = new JButton("← Back to Notifications");
        backButton.setFont(new Font("Arial", Font.PLAIN, 13));
        backButton.setForeground(CPUT_BLUE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setFocusable(false);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.addActionListener(e -> cardLayout.show(cardHolder, "inbox"));

        detailStatus = new JLabel();
        detailStatus.setFont(new Font("Arial", Font.BOLD, 12));
        detailStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailReceived = new JLabel();
        detailReceived.setFont(new Font("Arial", Font.PLAIN, 12));
        detailReceived.setForeground(new Color(130, 130, 130));
        detailReceived.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailMessage = new JLabel();
        detailMessage.setFont(new Font("Arial", Font.PLAIN, 16));
        detailMessage.setForeground(DARK_TEXT);
        detailMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailMessage.setBorder(new EmptyBorder(20, 0, 20, 0));

        card.add(backButton);
        card.add(Box.createVerticalStrut(24));
        card.add(detailStatus);
        card.add(Box.createVerticalStrut(4));
        card.add(detailReceived);
        card.add(detailMessage);
        return card;
    }

    private void openNotification(int row) {
        if (row < 0 || row >= notificationIds.size()) return;
        String message = inboxModel.getValueAt(row, 0).toString();
        String received = inboxModel.getValueAt(row, 1).toString();
        if (!pushReadToServer(row)) return;
        inboxModel.setValueAt("Read", row, 2);
        inboxModel.setValueAt("—", row, 3);
        inboxTable.repaint();
        detailMessage.setText("<html><body style='width:400px'>" + message + "</body></html>");
        detailReceived.setText("Received " + received);
        detailStatus.setText("Read");
        detailStatus.setForeground(new Color(60, 150, 90));
        cardLayout.show(cardHolder, "detail");
    }

    private void handleMarkAsRead(int row) {
        if (!pushReadToServer(row)) return;
        inboxModel.setValueAt("Read", row, 2);
        inboxModel.setValueAt("—", row, 3);
        inboxTable.repaint();
    }

    private boolean pushReadToServer(int row) {
        String currentStatus = inboxModel.getValueAt(row, 2).toString();
        if (currentStatus.equals("Read")) return true;
        if (row < 0 || row >= notificationIds.size()) return false;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/notification/" + notificationIds.get(row) + "/read"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(""))
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return false;
            }
        } catch (Exception ex) {
            showError("Could not mark notification as read: " + ex.getMessage());
            return false;
        }
        return true;
    }

    private void loadInbox() {
        inboxModel.setRowCount(0);
        notificationIds.clear();
        if (studentId == null) return;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/notification/student/" + studentId))
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                showError(response.body());
                return;
            }
            List<NotificationResponseDTO> notifications = MAPPER.readValue(
                    response.body(), new TypeReference<List<NotificationResponseDTO>>() {});
            for (NotificationResponseDTO notification : notifications) {
                notificationIds.add(notification.getId());
                inboxModel.addRow(new Object[]{
                        notification.getMessage(),
                        displayDate(notification.getCreatedAt()),
                        notification.isRead() ? "Read" : "Unread",
                        notification.isRead() ? "—" : "Mark as read"
                });
            }
        } catch (Exception ex) {
            showError("Could not load notifications: " + ex.getMessage());
        }
        inboxTable.repaint();
    }

    private static String displayDate(String dateTime) {
        if (dateTime == null) return "";
        String cleaned = dateTime.replace('T', ' ');
        return cleaned.length() >= 16 ? cleaned.substring(0, 16) : cleaned;
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                this, message == null || message.isBlank() ? "Request failed" : message,
                "Student request failed", JOptionPane.ERROR_MESSAGE));
    }
}
