package oleksii.filonov.gui;

import java.awt.*;
import java.util.ResourceBundle;
import javax.swing.*;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TestFrame {
    private JPanel mainPanel;
    private JButton openClientsButton;
    private JButton openCampaignButton;
    private JButton saveResultsButton;
    private JLabel pathToClientsFileLabel;
    private JLabel pathToCampaignFileLabel;
    private JLabel pathToResultsFileLabel;

    public static void main(final String[] args) {
        JFrame frame = new JFrame("TestFrame");
        //positionFrameToTheCenter(frame);
        frame.setContentPane(new TestFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static void positionFrameToTheCenter(JFrame frame) {
        // Get the size of the screen
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // Determine the new location of the window
        final int x = (dim.width) / 3;
        final int y = (dim.height) / 6;
        // Move the window
        frame.setLocation(x, y);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new FormLayout("fill:85px:noGrow,left:17dlu:noGrow,fill:435px:noGrow", "center:48px:noGrow,top:4dlu:noGrow,center:52px:noGrow,center:4dlu:noGrow,center:56px:noGrow"));
        openClientsButton = new JButton();
        this.$$$loadButtonText$$$(openClientsButton, ResourceBundle.getBundle("localization/bundle").getString("open.clients.button.title"));
        openClientsButton.setToolTipText(ResourceBundle.getBundle("localization/bundle").getString("open.clients.file.tooltip"));
        CellConstraints cc = new CellConstraints();
        mainPanel.add(openClientsButton, cc.xy(1, 1));
        openCampaignButton = new JButton();
        this.$$$loadButtonText$$$(openCampaignButton, ResourceBundle.getBundle("localization/bundle").getString("open.campaign.button.title"));
        openCampaignButton.setToolTipText(ResourceBundle.getBundle("localization/bundle").getString("open.campaign.file.tooltip"));
        mainPanel.add(openCampaignButton, cc.xy(1, 3));
        saveResultsButton = new JButton();
        saveResultsButton.setEnabled(true);
        this.$$$loadButtonText$$$(saveResultsButton, ResourceBundle.getBundle("localization/bundle").getString("save.results.button.title"));
        saveResultsButton.setToolTipText(ResourceBundle.getBundle("localization/bundle").getString("save.results.tooltip"));
        mainPanel.add(saveResultsButton, cc.xy(1, 5));
        pathToClientsFileLabel = new JLabel();
        pathToClientsFileLabel.setText("Path to Clients File");
        mainPanel.add(pathToClientsFileLabel, cc.xy(3, 1));
        pathToCampaignFileLabel = new JLabel();
        pathToCampaignFileLabel.setText("Path To Campaign File");
        mainPanel.add(pathToCampaignFileLabel, cc.xy(3, 3));
        pathToResultsFileLabel = new JLabel();
        pathToResultsFileLabel.setText("Path to Results File");
        mainPanel.add(pathToResultsFileLabel, cc.xy(3, 5));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
