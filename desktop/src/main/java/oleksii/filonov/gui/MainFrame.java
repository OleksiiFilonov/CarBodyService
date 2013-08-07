package oleksii.filonov.gui;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Create the frame.
     */
    public MainFrame(final String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(150, 150, 600, 500);
        final MainFramePanel contentPanel = new MainFramePanel();
        setContentPane(contentPanel);
    }

}
