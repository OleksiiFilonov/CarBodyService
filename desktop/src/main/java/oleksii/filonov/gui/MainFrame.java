package oleksii.filonov.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public MainFrame(final String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final MainFramePanel contentPanel = new MainFramePanel();
        setContentPane(contentPanel);
        positionFrameToTheCenter();
    }

    private void positionFrameToTheCenter() {
        // Get the size of the screen
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // Determine the new location of the window
        final int x = (dim.width) / 3;
        final int y = (dim.height) / 6;
        // Move the window
        this.setLocation(x, y);
    }

}
