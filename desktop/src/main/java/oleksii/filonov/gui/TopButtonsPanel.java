package oleksii.filonov.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TopButtonsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public TopButtonsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		final JButton linkToClient = new JButton("Clients");
		add(linkToClient);
		final JButton linkToCompaign = new JButton("Compaign");
		add(linkToCompaign);
		final JButton linkToResult = new JButton("Result");
		add(linkToResult);
	}

}
