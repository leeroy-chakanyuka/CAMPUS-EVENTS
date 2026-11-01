package za.ac.cput;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/** Readability helpers: long user-entered strings should wrap, not disappear. */
public final class UiText {
    private UiText() { }

    public static JTextArea wrapping(String value, int rows) {
        JTextArea text = new JTextArea(value == null ? "" : value);
        text.setEditable(false);
        text.setFocusable(false);
        text.setOpaque(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(UiTheme.body());
        text.setForeground(UiTheme.INK);
        text.setRows(Math.max(rows, 1));
        text.setBorder(null);
        text.getAccessibleContext().setAccessibleDescription(value == null ? "" : value);
        return text;
    }

    public static void enableMultilineColumn(JTable table, int column, int preferredWidth) {
        table.getColumnModel().getColumn(column).setPreferredWidth(preferredWidth);
        table.getColumnModel().getColumn(column).setCellRenderer(new WrapRenderer());
    }

    public static void configureTable(JTable table, int minimumWidth) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(40);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(UiTheme.label());
        table.getTableHeader().setBackground(UiTheme.CANVAS);
        table.getTableHeader().setForeground(UiTheme.MUTED);
        table.setFont(UiTheme.body());
        table.setForeground(UiTheme.INK);
        for (int c = 0; c < table.getColumnCount(); c++) {
            if (table.getColumnModel().getColumn(c).getPreferredWidth() < minimumWidth) {
                table.getColumnModel().getColumn(c).setPreferredWidth(minimumWidth);
            }
        }
    }

    /**
     * Height is computed per row using the widest rendered content; repainting after
     * column resizing lets Swing remeasure rows rather than clipping them.
     */
    private static class WrapRenderer extends JTextArea implements TableCellRenderer {
        WrapRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setEditable(false);
            setBorder(UiTheme.paddedBorder(9, 10, 9, 10));
            setFont(UiTheme.body());
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean selected, boolean focus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setForeground(selected ? table.getSelectionForeground() : UiTheme.INK);
            setBackground(selected ? table.getSelectionBackground() : UiTheme.WHITE);
            setSize(Math.max(40, table.getColumnModel().getColumn(column).getWidth()), Integer.MAX_VALUE);
            int wantedHeight = Math.max(table.getRowHeight(), getPreferredSize().height);
            if (table.getRowHeight(row) < wantedHeight) table.setRowHeight(row, wantedHeight);
            return this;
        }
    }
}
