package oleksii.filonov.buttons.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class OpenClientsFileListener implements ActionListener {

    private final JFileChooser fileChooser;

    private final JComponent parentComponent;

    public OpenClientsFileListener(final JFileChooser fileChooser, final JComponent parentComponent) {
        this.fileChooser = fileChooser;
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        this.fileChooser.showSaveDialog(this.parentComponent);
        System.out.println(this.fileChooser.getSelectedFile().getAbsolutePath());
    }

}
