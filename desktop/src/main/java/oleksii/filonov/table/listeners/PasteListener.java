package oleksii.filonov.table.listeners;

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

import oleksii.filonov.gui.MainTable;
import oleksii.filonov.model.Record;

import com.google.common.base.Strings;

public class PasteListener implements ActionListener {

    private static final int FIRST_INS_ROW_INDEX = 1;
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
                processClipboard(content, colSelected, rowSelected);
            } catch(final UnsupportedFlavorException e) {
                // String have to be the standard flavor
                System.err.println("UNSUPPORTED FLAVOR EXCEPTION " + e.getLocalizedMessage());
            } catch(final IOException e) {
                // The data is consumed?
                System.err.println("DATA CONSUMED EXCEPTION " + e.getLocalizedMessage());
            }
        }
    }

    private void processClipboard(final Transferable content, final int colSelected, final int rowSelected)
            throws UnsupportedFlavorException, IOException {
        final String clipboardContent = content.getTransferData(DataFlavor.stringFlavor).toString();
        final List<String> lines = removeEmptyLines(clipboardContent.split("\n"));
        if(!lines.isEmpty()) {
            this.table.setValueAt(lines.get(0), rowSelected, colSelected);
            int i = FIRST_INS_ROW_INDEX;
            for(; i < lines.size(); ++i) {
                if(!Strings.isNullOrEmpty(lines.get(i))) {
                    this.table.getModel().addRow(new Record(lines.get(i)), rowSelected + i);
                }
            }
            if(i > FIRST_INS_ROW_INDEX) {
                this.table.getModel().fireTableRowsInserted(rowSelected + FIRST_INS_ROW_INDEX,
                        rowSelected + i - FIRST_INS_ROW_INDEX);
            }
            this.table.repaint();
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
