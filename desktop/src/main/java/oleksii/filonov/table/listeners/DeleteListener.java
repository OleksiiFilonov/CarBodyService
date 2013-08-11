package oleksii.filonov.table.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import oleksii.filonov.gui.MainTable;

public class DeleteListener implements ActionListener {

    private static final int FIRST_COLUMN = 0;
    private final MainTable table;

    public DeleteListener(final MainTable table) {
        this.table = table;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final int rowSelected = this.table.getSelectedRow();
        if(rowSelected > -1) {
            this.table.getModel().removeRows(rowSelected, this.table.getSelectedRowCount());
            this.table.getModel().fireTableStructureChanged();
            this.table.changeSelection(rowSelected - 1, FIRST_COLUMN, false, false);
        }
    }
}
