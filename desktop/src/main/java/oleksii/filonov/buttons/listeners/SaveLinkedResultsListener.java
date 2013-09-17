package oleksii.filonov.buttons.listeners;

import oleksii.filonov.gui.MainFileChooser;
import oleksii.filonov.reader.ReadDataException;
import oleksii.filonov.writer.DataBuilder;
import oleksii.filonov.writer.WorkbookBuilder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class SaveLinkedResultsListener extends FileChooserListener {

	private final LinksCalculator linksCalculator;

	private final DataBuilder documentBuilder;

	public SaveLinkedResultsListener(final MainFileChooser fileChooser, final JComponent parentComponent) {
		super(fileChooser, parentComponent);
		linksCalculator = new LinksCalculator();
		documentBuilder = new WorkbookBuilder();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final int returnValue = getFileChooser().showSaveDialog(getParentComponent());
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			getFileChooser().setLinkedBodyFile(getFileChooser().getSelectedFile());
			try {
				linksCalculator.calculate(getFileChooser());
				documentBuilder.useWorkbook(getFileChooser().getCampaignFile());
				documentBuilder.createLinkedSheetWithName("Body");
				documentBuilder.writeBodyIdsColumnToLinkedSheet("Номер Кузова", linksCalculator.getBodyIds());
				documentBuilder.assignTasks(linksCalculator.getBodyIdLinks(), getFileChooser()
                        .getCampaignFile().getName());
				documentBuilder.saveToFile(getFileChooser().getLinkedBodyFile());
			} catch (final ReadDataException | InvalidFormatException exc) {
				System.err.println("Error while calculation links file: " + exc.getLocalizedMessage());
				JOptionPane.showMessageDialog(getParentComponent(), "Ошибка при поиски соответствий номеров кузовов");
			} catch (final IOException exc) {
				System.err.println("Error while saving body links file: " + exc.getLocalizedMessage());
				JOptionPane.showMessageDialog(getParentComponent(), "Ошибка при сохранении файла с результатами");
			}
        }
	}

}
