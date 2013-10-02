package oleksii.filonov;

import java.awt.EventQueue;

import oleksii.filonov.gui.MainWindow;

public class App {

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainWindow.fireMainWindow();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
