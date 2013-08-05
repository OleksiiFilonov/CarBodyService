package oleksii.filonov.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPane;
	private final JTable table;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		final JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		final JButton linkToClient = new JButton("Clients");
		panel.add(linkToClient);

		final JButton linkToCompaign = new JButton("Compaign");
		panel.add(linkToCompaign);

		final JButton linkToResult = new JButton("Result");
		panel.add(linkToResult);

		table = new JTable();
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
			}
		});
		contentPane.add(table, BorderLayout.CENTER);

	}

}
