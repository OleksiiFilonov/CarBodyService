package oleksii.filonov;

import java.awt.EventQueue;

import oleksii.filonov.gui.MainFrame;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
