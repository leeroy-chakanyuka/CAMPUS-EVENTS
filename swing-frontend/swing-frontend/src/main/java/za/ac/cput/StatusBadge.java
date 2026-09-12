package za.ac.cput;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

// Shared renderer for status cells — colors them by text so state reads at a
// glance: green = open/active, red = closed/inactive/suspended/cancelled,
// orange = unread (needs attention), grey = read (settled).
public class StatusBadge extends DefaultTableCellRenderer {

    private static final Color GREEN_FG = new Color(27, 110, 55);
    private static final Color GREEN_BG = new Color(230, 245, 235);
    private static final Color RED_FG = new Color(172, 36, 48);
    private static final Color RED_BG = new Color(251, 233, 236);
    private static final Color ORANGE_FG = new Color(178, 98, 6);
    private static final Color ORANGE_BG = new Color(255, 245, 222);
    private static final Color GREY_FG = new Color(125, 125, 125);
    private static final Color GREY_BG = new Color(242, 242, 242);

    public StatusBadge() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(new Font("Arial", Font.BOLD, 12));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String s = value == null ? "" : value.toString();
        switch (s) {
            case "Open":
            case "Active":
                apply(GREEN_FG, GREEN_BG);
                break;
            case "Closed":
            case "Cancelled":
            case "Inactive":
            case "Suspended":
                apply(RED_FG, RED_BG);
                break;
            case "Unread":
                apply(ORANGE_FG, ORANGE_BG);
                break;
            case "Read":
                apply(GREY_FG, GREY_BG);
                break;
            default:
                setForeground(table.getForeground());
                setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
        }
        return this;
    }

    private void apply(Color fg, Color bg) {
        setForeground(fg);
        setBackground(bg);
    }
}
