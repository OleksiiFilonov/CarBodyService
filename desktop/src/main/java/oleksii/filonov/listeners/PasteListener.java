package oleksii.filonov.listeners;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.CellEditor;

import oleksii.filonov.gui.MainTable;
import oleksii.filonov.model.Record;

import com.google.common.base.Strings;

public class PasteListener implements ActionListener {

    private final MainTable table;

    public PasteListener(final MainTable table) {
        this.table = table;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable content = clipboard.getContents(this);
        final int colSelected = this.table.getSelectedColumn();
        final int rowSelected = this.table.getSelectedRow();
        if(content != null && this.table.isCellEditable(rowSelected, colSelected)) {
            try {
                final String clipboardContent = content.getTransferData(DataFlavor.stringFlavor).toString();
                final List<String> lines = removeEmptyLines(clipboardContent.split("\n"));
                if(!lines.isEmpty()) {
                    this.table.setValueAt(lines.get(0), rowSelected, colSelected);
                    int i = 1;
                    for(; i < lines.size(); ++i) {
                        if(!Strings.isNullOrEmpty(lines.get(i))) {
                            this.table.getModel().addRow(new Record(lines.get(i)));
                            if(this.table.getEditingRow() == (rowSelected + i)
                                    && this.table.getEditingColumn() == colSelected) {
                                final CellEditor editor = this.table.getCellEditor();
                                editor.cancelCellEditing();
                                this.table.editCellAt(rowSelected + i, colSelected);
                            }
                        }
                    }
                    this.table.repaint();
                }
            } catch(final UnsupportedFlavorException e) {
                // String have to be the standard flavor
                System.err.println("UNSUPPORTED FLAVOR EXCEPTION " + e.getLocalizedMessage());
            } catch(final IOException e) {
                // The data is consumed?
                System.err.println("DATA CONSUMED EXCEPTION " + e.getLocalizedMessage());
            }
        }
    }

    private List<String> removeEmptyLines(final String[] values) {
        final List<String> result = new ArrayList<>();
        for(final String value : values) {
            if(!value.isEmpty()) {
                result.add(value);
            }
        }
        return result;
    }
}
