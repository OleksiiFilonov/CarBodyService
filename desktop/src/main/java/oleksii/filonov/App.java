package oleksii.filonov;

import java.awt.EventQueue;

import oleksii.filonov.gui.MainFrame;

public class App {
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createAndShowGUI();
                } catch(final Exception e) {
                    e.printStackTrace();
                }
            }

            private void createAndShowGUI() {
                final MainFrame frame = new MainFrame("Body Ids");
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
