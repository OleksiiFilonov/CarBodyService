package oleksii.filonov.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.google.common.collect.Lists;

public class MainTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private final ArrayList<String> columnNames = Lists.newArrayList("Body Id");

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getColumnName(final int columnIndex) {
		if (columnIndex < 0 || columnIndex > columnNames.size() - 1) {
			throw new IllegalArgumentException("Invalid column index");
		}

		return columnNames.get(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
