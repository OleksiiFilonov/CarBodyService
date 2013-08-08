package oleksii.filonov.gui;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import oleksii.filonov.model.MainTableModel;
import oleksii.filonov.model.Record;
import oleksii.filonov.model.RecordStatus;

public class MainTable extends JTable {

    private static final long serialVersionUID = 1L;

    private final BodyIdRenderer bodyIdRenderer = new BodyIdRenderer();

    public MainTable(final MainTableModel mainTableModel) {
        super(mainTableModel);
        getTableHeader().setReorderingAllowed(false);
        mainTableModel.getRecords().add(createRecord("KMHBT51DBBU022001", "'10C029'!B2", RecordStatus.FOUND));
        mainTableModel.getRecords().add(createRecord("KMHBT51DBBU022002", "'10C129'!B2", RecordStatus.NOT_FOUND));
        mainTableModel.getRecords().add(createRecord("KMHBT51DBBU022003", "'10C229'!B2", RecordStatus.UNDEFINED));
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

    private Record createRecord(final String bodyId, final String link, final RecordStatus status) {
        final Record record = new Record();
        record.setBodyId(bodyId);
        record.addReference(link);
        record.setStatus(status);
        return record;
    }
}
