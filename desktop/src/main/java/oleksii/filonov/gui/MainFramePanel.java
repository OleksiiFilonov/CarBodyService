package oleksii.filonov.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainFramePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MainFramePanel() {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		final TopButtonsPanel topButtonsPanel = new TopButtonsPanel();
		add(topButtonsPanel, BorderLayout.NORTH);
	}

}
