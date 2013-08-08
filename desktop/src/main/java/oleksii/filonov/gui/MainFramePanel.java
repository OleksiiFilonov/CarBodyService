package oleksii.filonov.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import oleksii.filonov.model.MainTableModel;

public class MainFramePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public MainFramePanel() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(0, 0));
        final TopButtonsPanel topButtonsPanel = new TopButtonsPanel();
        add(topButtonsPanel, BorderLayout.NORTH);
        final JScrollPane scrollPane = new JScrollPane(new MainTable(new MainTableModel()));
        add(scrollPane, BorderLayout.CENTER);
        add(new JButton("+"), BorderLayout.SOUTH);
    }

}
