package oleksii.filonov.gui;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public MainFrame(final String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final MainFramePanel contentPanel = new MainFramePanel();
        setContentPane(contentPanel);
    }

}
