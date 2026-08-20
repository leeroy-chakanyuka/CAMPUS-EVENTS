package za.ac.cput;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.IntConsumer;

// Makes one column of a JTable behave like real clickable buttons, not just
// text. The button's label comes straight from the cell's value, so
// "Suspend"/"Reactivate"/"Force cancel"/etc. all just work by changing what
// string is in that cell.
public class TableButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private final JButton renderButton = new JButton();
    private final JButton editButton = new JButton();
    private final IntConsumer onClick;
    private int editingRow;

    public TableButtonColumn(JTable table, int columnIndex, IntConsumer onClick) {
        this.onClick = onClick;
        editButton.addActionListener(this::fireClick);
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(this);
        table.getColumnModel().getColumn(columnIndex).setCellEditor(this);
    }

    private void fireClick(ActionEvent e) {
        onClick.accept(editingRow);
        fireEditingStopped();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        renderButton.setText(value == null ? "" : value.toString());
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
