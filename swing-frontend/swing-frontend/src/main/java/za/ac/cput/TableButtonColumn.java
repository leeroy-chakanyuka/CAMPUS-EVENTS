package za.ac.cput;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.EventObject;
import java.util.Set;
import java.util.function.IntConsumer;

// Makes one column of a JTable behave like real clickable buttons, not just
// text. The button's label comes straight from the cell's value, so
// "Suspend"/"Reactivate"/"Force cancel"/etc. all just work by changing what
// string is in that cell.
//
// Values passed as terminalValues are committed one-way actions: they render
// as static bold text instead of a button and clicks on them are vetoed, so
// "Close registration" -> "Closed" genuinely becomes unclickable.
public class TableButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private static final Color TERMINAL_STOPPED = new Color(150, 40, 50);   // "Closed", "Cancelled"
    private static final Color TERMINAL_NONE = new Color(165, 165, 165);    // "—"

    private final JTable table;
    private final JButton renderButton = new JButton();
    private final JButton editButton = new JButton();
    private final JLabel staticLabel = new JLabel("", SwingConstants.CENTER);
    private final Set<String> terminalValues;
    private final IntConsumer onClick;
    private int editingRow;

    public TableButtonColumn(JTable table, int columnIndex, IntConsumer onClick) {
        this(table, columnIndex, onClick, (Color) null);
    }

    public TableButtonColumn(JTable table, int columnIndex, IntConsumer onClick, String... terminalValues) {
        this(table, columnIndex, onClick, null, terminalValues);
    }

    public TableButtonColumn(JTable table, int columnIndex, IntConsumer onClick, Color actionForeground, String... terminalValues) {
        this.table = table;
        this.onClick = onClick;
        this.terminalValues = new HashSet<>(Arrays.asList(terminalValues));

        if (actionForeground != null) {
            renderButton.setForeground(actionForeground);
            editButton.setForeground(actionForeground);
        }

        staticLabel.setFont(staticLabel.getFont().deriveFont(Font.BOLD, 13f));

        editButton.addActionListener(this::fireClick);
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(this);
        table.getColumnModel().getColumn(columnIndex).setCellEditor(this);
    }

    private void fireClick(ActionEvent e) {
        onClick.accept(editingRow);
        fireEditingStopped();
    }

    private boolean isTerminal(Object value) {
        return value != null && terminalValues.contains(value.toString());
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        if (!terminalValues.isEmpty() && e instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) e;
            int row = table.rowAtPoint(me.getPoint());
            int col = table.columnAtPoint(me.getPoint());
            if (row >= 0 && col >= 0 && isTerminal(table.getValueAt(row, col))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String text = value == null ? "" : value.toString();
        if (terminalValues.contains(text)) {
            staticLabel.setText(text);
            staticLabel.setForeground(text.equals("—") ? TERMINAL_NONE : TERMINAL_STOPPED);
            return staticLabel;
        }
        renderButton.setText(text);
        return renderButton;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editingRow = row;
        editButton.setText(value == null ? "" : value.toString());
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return editButton.getText();
    }
}
