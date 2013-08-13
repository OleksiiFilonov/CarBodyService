package oleksii.filonov.buttons.listeners;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import oleksii.filonov.gui.ComponentsLocator;
import oleksii.filonov.gui.MainFileChooser;
import oleksii.filonov.gui.MainTable;
import oleksii.filonov.model.Record;
import oleksii.filonov.reader.ExcelReader;
import oleksii.filonov.reader.ReadDataException;

public class OpenClientsFileListener extends FileChooserListener {

	private static final String BODY_ID_MARKER = "Номер кузова";

	private final ExcelReader excelReader;

	public OpenClientsFileListener(final MainFileChooser fileChooser, final JComponent parentComponent) {
		super(fileChooser, parentComponent);
		excelReader = new ExcelReader();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final int returnValue = getFileChooser().showOpenDialog(getParentComponent());
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			final File selectedFile = getFileChooser().getSelectedFile();
			getFileChooser().setBodyIdFile(selectedFile);
			try {
				final String[] bodyIds = excelReader.readFirstSheetUniqueValues(selectedFile, BODY_ID_MARKER);
				final MainTable table = ComponentsLocator.getInstanse().getTable();
				table.getModel().removeAllRows();
				for (final String bodyId : bodyIds) {
					table.getModel().addRow(new Record(bodyId));
				}
				table.getModel().fireTableRowsInserted(0, bodyIds.length - 1);
			} catch (final ReadDataException exc) {
				System.err.println("Error occured while trying to read client file: " + exc.getLocalizedMessage());
				JOptionPane.showMessageDialog(getParentComponent(), "Ошибка при открытии файла с номерами кузовов");
			}
		}

	}
}
