package oleksii.filonov.buttons.listeners;

import java.awt.event.ActionListener;

import javax.swing.JComponent;

import oleksii.filonov.gui.MainFileChooser;

public abstract class FileChooserListener implements ActionListener {

    private final MainFileChooser fileChooser;
    private final JComponent parentComponent;

    public FileChooserListener(final MainFileChooser fileChooser, final JComponent parentComponent) {
        this.fileChooser = fileChooser;
        this.parentComponent = parentComponent;
    }

    public MainFileChooser getFileChooser() {
        return this.fileChooser;
    }

    public JComponent getParentComponent() {
        return this.parentComponent;
    }

}