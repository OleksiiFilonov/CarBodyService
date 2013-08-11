package oleksii.filonov.table.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import oleksii.filonov.gui.MainTable;
import oleksii.filonov.model.Record;

public class InsertListener implements ActionListener {

    private static final int FIRST_COLUMN = 0;
    private final MainTable table;

    public InsertListener(final MainTable table) {
        this.table = table;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final int rowInsIndex = this.table.getSelectedRow() + 1;
        this.table.getModel().addRow(new Record(), rowInsIndex);
        this.table.getModel().fireTableRowsInserted(rowInsIndex, rowInsIndex);
        this.table.changeSelection(rowInsIndex, FIRST_COLUMN, false, false);
        this.table.editCellAt(rowInsIndex, FIRST_COLUMN);
    }
}
