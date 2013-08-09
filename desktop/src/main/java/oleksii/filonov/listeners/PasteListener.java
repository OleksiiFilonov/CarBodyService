package oleksii.filonov.listeners;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.CellEditor;

import oleksii.filonov.gui.MainTable;

public class PasteListener implements ActionListener {

    private final MainTable table;

    public PasteListener(final MainTable table) {
        this.table = table;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable content = clipboard.getContents(this);
        if(content != null) {
            try {
                final String value = content.getTransferData(DataFlavor.stringFlavor).toString();
        
                final int col = this.table.getSelectedColumn();
                final int row = this.table.getSelectedRow();
                if(this.table.isCellEditable(row, col)) {
                    this.table.setValueAt(value, row, col);
                    if(this.table.getEditingRow() == row && this.table.getEditingColumn() == col) {
                        final CellEditor editor = this.table.getCellEditor();
                        editor.cancelCellEditing();
                        this.table.editCellAt(row, col);
                    }// end if
                }// end if
                this.table.repaint();
            } catch(final UnsupportedFlavorException e) {
                // String have to be the standard flavor
                System.err.println("UNSUPPORTED FLAVOR EXCEPTION " + e.getLocalizedMessage());
            } catch(final IOException e) {
                // The data is consumed?
                System.err.println("DATA CONSUMED EXCEPTION " + e.getLocalizedMessage());
            }// end try
        }// end if
    }
}
