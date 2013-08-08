package oleksii.filonov.gui;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static oleksii.filonov.model.RecordStatus.FOUND;
import static oleksii.filonov.model.RecordStatus.NOT_FOUND;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import oleksii.filonov.model.RecordStatus;

public class BodyIdRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        final Component cellRenderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                column);
        final MainTable tableModel = (MainTable)table;
        final RecordStatus cellStatus = tableModel.getModel().getRecords().get(row).getStatus();
        if(NOT_FOUND == cellStatus) {
            cellRenderer.setBackground(RED);
        } else if(FOUND == cellStatus) {
            cellRenderer.setBackground(GREEN);
        } else {
            cellRenderer.setBackground(WHITE);
        }
        setValue(value);
        return cellRenderer;
    }

}
