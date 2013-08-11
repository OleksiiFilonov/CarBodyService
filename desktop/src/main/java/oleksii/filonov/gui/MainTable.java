package oleksii.filonov.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellRenderer;

import oleksii.filonov.model.MainTableModel;
import oleksii.filonov.model.Record;
import oleksii.filonov.model.RecordStatus;
import oleksii.filonov.table.listeners.DeleteListener;
import oleksii.filonov.table.listeners.InsertListener;
import oleksii.filonov.table.listeners.PasteListener;

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
        setFillsViewportHeight(true);
        registerKeyboardEvents();
    }

    private void registerKeyboardEvents() {
        registerPasteEvents();
        registerInsertEvent();
        registerDeleteEvent();
    }

    private void registerDeleteEvent() {
        final ActionListener deleteListener = new DeleteListener(this);
        final KeyStroke strokeDelete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        registerKeyboardAction(deleteListener, "Delete", strokeDelete, JComponent.WHEN_FOCUSED);
    }

    private void registerInsertEvent() {
        final ActionListener insertListener = new InsertListener(this);
        final KeyStroke strokeInsert = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0);
        registerKeyboardAction(insertListener, "InsertRow", strokeInsert, JComponent.WHEN_FOCUSED);
    }

    private void registerPasteEvents() {
        final KeyStroke strokeCtrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        final KeyStroke strokeCtrlIns = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, ActionEvent.CTRL_MASK, false);
        final ActionListener pasteListener = new PasteListener(this);
        registerKeyboardAction(pasteListener, "PasteCtrlV", strokeCtrlV, JComponent.WHEN_FOCUSED);
        registerKeyboardAction(pasteListener, "PasteCtrlInsert", strokeCtrlIns, JComponent.WHEN_FOCUSED);
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

    @Override
    public String getValueAt(final int row, final int column) {
        return (String)super.getValueAt(row, column);
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
