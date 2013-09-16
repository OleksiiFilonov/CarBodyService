package oleksii.filonov.gui;

import oleksii.filonov.buttons.listeners.CalculateLinksListener;
import oleksii.filonov.buttons.listeners.OpenCampaignFileListener;
import oleksii.filonov.buttons.listeners.OpenClientsFileListener;
import oleksii.filonov.buttons.listeners.SaveLinkedResultsListener;

import javax.swing.*;
import java.nio.file.Paths;

public class TopButtonsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public TopButtonsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		final MainFileChooser fileChooser = new MainFileChooser(Paths.get("").toFile());
		final JButton loadClients = new JButton("Загрузить клиетов");
		add(loadClients);
		loadClients.addActionListener(new OpenClientsFileListener(fileChooser, this));
		final JButton loadCampaigns = new JButton("Загрузить Кампании");
		add(loadCampaigns);
		loadCampaigns.addActionListener(new OpenCampaignFileListener(fileChooser, this));
		final JButton saveResults = new JButton("Сохранить результаты");
		add(saveResults);
		saveResults.addActionListener(new CalculateLinksListener(fileChooser, this));
		saveResults.addActionListener(new SaveLinkedResultsListener(fileChooser, this));
	}

}
