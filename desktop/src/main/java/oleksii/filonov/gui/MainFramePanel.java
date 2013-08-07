package oleksii.filonov.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import oleksii.filonov.model.MainTableModel;

public class MainFramePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public MainFramePanel() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(0, 0));
        final TopButtonsPanel topButtonsPanel = new TopButtonsPanel();
        add(topButtonsPanel, BorderLayout.NORTH);
        final MainTable table = new MainTable(new MainTableModel());
        add(table, BorderLayout.CENTER);
    }

}
