package oleksii.filonov.gui;

import javax.swing.JTable;

import oleksii.filonov.model.MainTableModel;

public class MainTable extends JTable {

    public MainTable(final MainTableModel mainTableModel) {
        super(mainTableModel);
    }

    private static final long serialVersionUID = 1L;

}
