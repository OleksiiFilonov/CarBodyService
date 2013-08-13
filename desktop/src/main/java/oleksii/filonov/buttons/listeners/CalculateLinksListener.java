package oleksii.filonov.buttons.listeners;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import oleksii.filonov.gui.MainFileChooser;
import oleksii.filonov.reader.ReadDataException;

public class CalculateLinksListener extends FileChooserListener {

	private final LinksCalculator linksCalculator;

	public CalculateLinksListener(final MainFileChooser fileChooser, final JComponent parentComponent) {
		super(fileChooser, parentComponent);
		linksCalculator = new LinksCalculator();
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		if (getFileChooser().getCampaignFile() == null) {
			JOptionPane.showMessageDialog(getParentComponent(), "Пожалуйста укажите файл Кампании");
		} else {
			try {
				linksCalculator.calculate(getFileChooser());
			} catch (final ReadDataException exc) {
				System.err.println("Error while calculation links file: " + exc.getLocalizedMessage());
				JOptionPane.showMessageDialog(getParentComponent(), "Ошибка при поиски соответствий номеров кузовов");
			}
		}
	}

}
