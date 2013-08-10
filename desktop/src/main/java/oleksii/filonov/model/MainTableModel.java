package oleksii.filonov.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.google.common.collect.Lists;

public class MainTableModel extends AbstractTableModel {

    private static final String ID_COLUMN_TITLE = "Body Id";

    private static final String COLUMN_LINK_TITLE = "Link";

    private static final String[] INITIAL_COLUMN_NAMES = new String[] { ID_COLUMN_TITLE, COLUMN_LINK_TITLE };

    private static final long serialVersionUID = 1L;

    private final ArrayList<String> columnNames = Lists.newArrayList(INITIAL_COLUMN_NAMES);

    private final List<Record> records = Lists.newArrayList();

    @Override
    public void setValueAt(final Object bodyId, final int rowIndex, final int columnIndex) {
        if(columnIndex > 0) {
            throw new IllegalArgumentException(String.format("Column %s can't be editable", columnIndex));
        } else {
            final Record editedRecord = this.records.get(rowIndex);
            if(!bodyId.equals(editedRecord.getBodyId())) {
                editedRecord.setBodyId(bodyId.toString());
                editedRecord.getReferences().clear();
                editedRecord.setStatus(RecordStatus.UNDEFINED);
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }
    }

    @Override
    public String getValueAt(final int rowIndex, final int columnIndex) {
        if(this.records.size() <= rowIndex) {
            throw new IllegalArgumentException("Invalid row selected");
        }
        final Record clickedRecord = this.records.get(rowIndex);
        if(columnIndex == 0) {
            return clickedRecord.getBodyId();
        } else {
            return clickedRecord.findReference(columnIndex - 1);
        }
    }

    @Override
    public int getRowCount() {
        return this.records.size();
    }

    @Override
    public Class<String> getColumnClass(final int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        if(columnIndex == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getColumnName(final int columnIndex) {
        if(columnIndex < 0 || columnIndex > this.columnNames.size() - 1) {
            throw new IllegalArgumentException("Invalid column index");
        }

        return this.columnNames.get(columnIndex);
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.size();
    }

    public List<Record> getRecords() {
        return this.records;
    }

    public void addRow(final Record record) {
        getRecords().add(record);
    }

    public void addColumn() {
        this.columnNames.add(COLUMN_LINK_TITLE);
        fireTableStructureChanged();
    }

    public void removeColumn() {
        if(INITIAL_COLUMN_NAMES.length < this.columnNames.size()) {
            this.columnNames.remove(this.columnNames.size() - 1);
        }
        fireTableStructureChanged();
    }

}
