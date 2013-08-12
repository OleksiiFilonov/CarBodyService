package oleksii.filonov.gui;

import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import oleksii.filonov.buttons.listeners.CalculateLinksListener;
import oleksii.filonov.buttons.listeners.OpenCampaignFileListener;
import oleksii.filonov.buttons.listeners.OpenClientsFileListener;
import oleksii.filonov.buttons.listeners.SaveLinkedResultsListener;

public class TopButtonsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public TopButtonsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        final MainFileChooser fileChooser = new MainFileChooser(Paths.get("").toFile());
        final JButton loadClients = new JButton("Open Clients");
        add(loadClients);
        loadClients.addActionListener(new OpenClientsFileListener(fileChooser, this));
        final JButton loadCampaigns = new JButton("Open Compaign");
        add(loadCampaigns);
        loadCampaigns.addActionListener(new OpenCampaignFileListener(fileChooser, this));
        final JButton calculateLinks = new JButton("Calculate");
        add(calculateLinks);
        calculateLinks.addActionListener(new CalculateLinksListener(fileChooser));
        final JButton saveResults = new JButton("Save Results");
        add(saveResults);
        saveResults.addActionListener(new SaveLinkedResultsListener(fileChooser, this));
    }

}
