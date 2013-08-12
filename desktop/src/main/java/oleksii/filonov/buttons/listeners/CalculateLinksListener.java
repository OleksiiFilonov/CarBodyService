package oleksii.filonov.buttons.listeners;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import oleksii.filonov.gui.MainFileChooser;

public class CalculateLinksListener extends FileChooserListener {

	public CalculateLinksListener(final MainFileChooser fileChooser, final JComponent parentComponent) {
		super(fileChooser, parentComponent);
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		if (getFileChooser().getCampaignFile() == null) {
			JOptionPane.showMessageDialog(getParentComponent(), "Пожалуйста укажите файл Кампании");
		} else {
		}
	}
}
