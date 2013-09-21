package oleksii.filonov.buttons.listeners;

import oleksii.filonov.gui.ComponentsLocator;
import oleksii.filonov.gui.MainFileChooser;
import oleksii.filonov.gui.MainTable;
import oleksii.filonov.model.Record;
import oleksii.filonov.reader.ExcelReader;
import oleksii.filonov.reader.ReadDataException;
import org.apache.poi.ss.usermodel.Cell;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

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
				final Cell[] bodyIds = excelReader.readFirstSheetUniqueValues(selectedFile, BODY_ID_MARKER);
				final MainTable table = ComponentsLocator.getInstanse().getTable();
				table.getModel().removeAllRows();
				for (final Cell bodyId : bodyIds) {
					table.getModel().addRow(new Record(bodyId.getStringCellValue()));
				}
				table.getModel().fireTableRowsInserted(0, bodyIds.length - 1);
			} catch (final ReadDataException exc) {
				System.err.println("Error occured while trying to read client file: " + exc.getLocalizedMessage());
				JOptionPane.showMessageDialog(getParentComponent(), "Ошибка при открытии файла с номерами кузовов");
			}
		}

	}
}
