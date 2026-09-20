package za.ac.cput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class OrganiserNotificationsPanel extends JPanel {
    private static final String BASE_URL =
            "http://localhost:8080";
    private final HttpClient httpClient =
            HttpClient.newHttpClient();
    private Long organiserId;

    private JTable inboxTable;
    private DefaultTableModel inboxModel;

    private CardLayout cardLayout;
    private JPanel cardHolder;

    private JLabel detailMessage;
    private JLabel detailReceived;
    private JLabel detailStatus;
    private JButton detailMarkAsRead;
    private int openRow = -1;

    private static final Color CPUT_BLUE = new Color(0, 51, 102);
    private static final Color CPUT_RED = new Color(190, 30, 45);
    private static final Color LIGHT_BLUE = new Color(235, 242, 250);
    private static final Color DARK_TEXT = new Color(40, 40, 40);
    private static final Color UNREAD_BG = new Color(255, 249, 230);

    public OrganiserNotificationsPanel() {
        this(null);
    }

    public OrganiserNotificationsPanel(Long organiserId) {

        this.organiserId = organiserId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        cardLayout = new CardLayout();
        cardHolder = new JPanel(cardLayout);
        cardHolder.setBackground(Color.WHITE);

        cardHolder.add(buildInboxCard(), "inbox");
        cardHolder.add(buildDetailCard(), "detail");

        add(cardHolder, BorderLayout.CENTER);
        seedInbox();
        cardLayout.show(cardHolder, "inbox");
    }

    // ---------------- INBOX (receive) ----------------

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
        refreshButton.addActionListener(e -> handleRefresh());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);
        card.add(buildInboxTable(), BorderLayout.CENTER);
        return card;
    }


    private void handleRefresh() {
        // TODO: GET /notification/organiser/{recipientId} here — refetch instead of reseeding
        openRow = -1;
        inboxModel.setRowCount(0);
        seedInbox();
        inboxTable.repaint();
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

        detailMarkAsRead = new JButton("Mark as read");
        detailMarkAsRead.setBackground(CPUT_RED);
        detailMarkAsRead.setForeground(Color.WHITE);
        detailMarkAsRead.setFocusPainted(false);
        detailMarkAsRead.setFont(new Font("Arial", Font.BOLD, 13));
        detailMarkAsRead.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailMarkAsRead.addActionListener(e -> {
            if (openRow >= 0) {
                handleMarkAsRead(openRow);
                refreshDetailView();
            }
        });

        card.add(backButton);
        card.add(Box.createVerticalStrut(24));
        card.add(detailStatus);
        card.add(Box.createVerticalStrut(4));
        card.add(detailReceived);
        card.add(detailMessage);
        card.add(detailMarkAsRead);
        return card;
    }

    private void openNotification(int row) {
        openRow = row;
        refreshDetailView();
        cardLayout.show(cardHolder, "detail");
    }

    private void refreshDetailView() {
        if (openRow < 0) return;
        String message = inboxModel.getValueAt(openRow, 0).toString();
        String received = inboxModel.getValueAt(openRow, 1).toString();
        String status = inboxModel.getValueAt(openRow, 2).toString();

        detailMessage.setText("<html><body style='width:400px'>" + message + "</body></html>");
        detailReceived.setText("Received " + received);
        detailStatus.setText(status);
        detailStatus.setForeground(status.equals("Unread") ? CPUT_RED : new Color(60, 150, 90));
        detailMarkAsRead.setVisible(status.equals("Unread"));
    }

    private void handleMarkAsRead(int row) {
        // TODO: PUT /notification/{id}/read here — path variable, same as GET /event/{id}
        String currentStatus = inboxModel.getValueAt(row, 2).toString();
        if (currentStatus.equals("Read")) return;
        inboxModel.setValueAt("Read", row, 2);
        inboxModel.setValueAt("—", row, 3);
        inboxTable.repaint();
    }

    private void seedInbox() {
        // TODO: GET /notification/organiser/{recipientId} here — path variable
        // response is List<NotificationResponseDTO>: id, message, recipientId, recipientType, read, createdAt
        // Column mapping: message -> col 0, createdAt -> col 1, read?"Read":"Unread" -> col 2, read?"—":"Mark as read" -> col 3
        inboxModel.addRow(new Object[]{"Event registrations opened", "2 min ago", "Unread", "Mark as read"});
        inboxModel.addRow(new Object[]{"New event reminder", "1 hour ago", "Unread", "Mark as read"});
        inboxModel.addRow(new Object[]{"System notification", "Yesterday", "Read", "—"});
    }
    private String loadNotificationsFromBackend()
            throws Exception {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        BASE_URL
                                                + "/notification/organiser/"
                                                + organiserId
                                )
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Could not load notifications. HTTP "
                            + response.statusCode()
            );
        }

        return response.body();
    }
    private void markNotificationAsRead(
            Long notificationId) throws Exception {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        BASE_URL
                                                + "/notification/"
                                                + notificationId
                                                + "/read"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .PUT(
                                HttpRequest.BodyPublishers.noBody()
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Could not mark notification as read. HTTP "
                            + response.statusCode()
            );
        }
    }
    private void sendNotificationToBackend(
            Long recipientId,
            String title,
            String message) throws Exception {

        String json =
                "{"
                        + "\"recipientId\":"
                        + recipientId
                        + ","
                        + "\"title\":\""
                        + escapeJson(title)
                        + "\","
                        + "\"message\":\""
                        + escapeJson(message)
                        + "\""
                        + "}";

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        BASE_URL
                                                + "/notification/send"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        json
                                )
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Could not send notification. HTTP "
                            + response.statusCode()
            );
        }
    }

    private String escapeJson(String value) {

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
