package oleksii.filonov.gui;

import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import oleksii.filonov.buttons.listeners.OpenClientsFileListener;

public class TopButtonsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public TopButtonsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        final MainFileChooser fileChooser = new MainFileChooser(Paths.get("").toFile());
        final JButton loadClients = new JButton("Open Clients");
        add(loadClients);
        loadClients.addActionListener(new OpenClientsFileListener(fileChooser, this));
        final JButton loadCompaigns = new JButton("Open Compaign");
        add(loadCompaigns);
        final JButton calculate = new JButton("Calculate");
        add(calculate);
        final JButton saveResults = new JButton("Save Results");
        add(saveResults);
    }

}
