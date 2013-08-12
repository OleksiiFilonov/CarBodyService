package oleksii.filonov.buttons.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import oleksii.filonov.gui.MainFileChooser;

public class CalculateLinksListener implements ActionListener {

    private final MainFileChooser fileChooser;

    public CalculateLinksListener(final MainFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // TODO get body Id's from header
        // trace over Campaign
        // get result in list

    }

}
