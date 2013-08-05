package oleksii.filonov.gui;

import javax.swing.JTable;

import oleksii.filonov.model.MainTableModel;

public class MainFrameTable extends JTable {

	private static final long serialVersionUID = 1L;

	public MainFrameTable() {
		final MainTableModel model = new MainTableModel();
		setModel(model);
	}

}
