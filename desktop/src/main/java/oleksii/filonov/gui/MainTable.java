package oleksii.filonov.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellRenderer;

import oleksii.filonov.listeners.PasteListener;
import oleksii.filonov.model.MainTableModel;
import oleksii.filonov.model.Record;
import oleksii.filonov.model.RecordStatus;

public class MainTable extends JTable {

    private static final long serialVersionUID = 1L;

    private final BodyIdRenderer bodyIdRenderer = new BodyIdRenderer();

    public MainTable(final MainTableModel mainTableModel) {
        super(mainTableModel);
        getTableHeader().setReorderingAllowed(false);
        mainTableModel.getRecords().add(createRecord("KMHBT51DBBU022001", "'10C029'!B2", null, RecordStatus.FOUND));
        mainTableModel.getRecords().add(
                createRecord("KMHBT51DBBU022002", "'10C129'!B2", "'10C129'!B3", RecordStatus.NOT_FOUND));
        mainTableModel.getRecords().add(createRecord("KMHBT51DBBU022003", "'10C229'!B2", null, RecordStatus.UNDEFINED));
        final KeyStroke strokeCtrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        final KeyStroke strokeCtrlIns = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, ActionEvent.CTRL_MASK, false);
        final PasteListener pasteListener = new PasteListener(this);
        registerKeyboardAction(pasteListener, "PasteV", strokeCtrlV, JComponent.WHEN_FOCUSED);
        registerKeyboardAction(pasteListener, "PasteInsert", strokeCtrlIns, JComponent.WHEN_FOCUSED);
    }

    @Override
    public MainTableModel getModel() {
        return (MainTableModel)super.getModel();
    }

    @Override
    public TableCellRenderer getCellRenderer(final int row, final int column) {
        if(column == 0) {
            return this.bodyIdRenderer;
        } else {
            return super.getCellRenderer(row, column);
        }
    }

    private Record createRecord(final String bodyId, final String linkFirst, final String linkSecond,
            final RecordStatus status) {
        final Record record = new Record();
        record.setBodyId(bodyId);
        record.addReference(linkFirst);
        record.setStatus(status);
        return record;
    }
}
