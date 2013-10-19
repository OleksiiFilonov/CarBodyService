package oleksii.filonov.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.*;

import oleksii.filonov.processors.DataProcessorFacade;
import oleksii.filonov.processors.FilesToProcess;
import oleksii.filonov.processors.WorkbookProcessorFacade;

import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import oleksii.filonov.readers.ReadDataException;
import oleksii.filonov.settings.PropertiesLoader;
import oleksii.filonov.singleton.SettingsStorage;

public class MainWindow {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("localization/bundle", new EncodedControl());
    private JPanel mainPanel;
    private JButton clientsButton;
    private JButton campaignButton;
    private JButton resultsButton;
    private JLabel pathToClientsFileLabel;
    private JLabel pathToCampaignFileLabel;
    private JLabel pathToResultsFileLabel;
    private final FilesToProcess filesToProcess = new FilesToProcess();
    private final JFileChooser fileChooser = new JFileChooser();
    private final DataProcessorFacade processor;

    public MainWindow(final Frame mainFrame) throws IOException {
        processor = new WorkbookProcessorFacade();
        SettingsStorage.setSettings(PropertiesLoader.loadDefaultProperties());
        clientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                final int returnValue = fileChooser.showOpenDialog(mainPanel);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    filesToProcess.setClientsFile(fileChooser.getSelectedFile());
                    pathToClientsFileLabel.setText(filesToProcess.getClientsFile().getName());
                }
            }
        });
        campaignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final int returnValue = fileChooser.showOpenDialog(mainPanel);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    filesToProcess.setCampaignFile(fileChooser.getSelectedFile());
                    pathToCampaignFileLabel.setText(filesToProcess.getCampaignFile().getName());
                }
            }
        });
        resultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final int returnValue = fileChooser.showOpenDialog(mainPanel);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    filesToProcess.setResultFile(fileChooser.getSelectedFile());
                    pathToResultsFileLabel.setText(filesToProcess.getResultFile().getName());
                    try {
                        processor.createResultFile(SettingsStorage.getSettings(), filesToProcess);
                    } catch (ReadDataException rdExc) {
                        JOptionPane.showMessageDialog(mainFrame, rdExc.getLocalizedMessage(),
                                resourceBundle.getString("errorbox.title"),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    public static void fireMainWindow() throws IOException {
        final JFrame frame = new JFrame(resourceBundle.getString("main.window.title"));
        positionFrameToTheCenter(frame);
        frame.setContentPane(new MainWindow(frame).mainPanel);
        frame.setJMenuBar(new MainMenuBar(frame));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static void positionFrameToTheCenter(final JFrame frame) {
        // Get the size of the screen
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // Determine the new location of the window
        final int x = (dim.width) / 3;
        final int y = (dim.height) / 6;
        // Move the window
        frame.setLocation(x, y);
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
        mainPanel.setLayout(new FormLayout("fill:15px:noGrow,left:4dlu:noGrow,fill:107px:noGrow,left:17dlu:noGrow,fill:339px:noGrow", "center:19px:noGrow,top:4dlu:noGrow,center:65px:noGrow,top:4dlu:noGrow,center:81px:noGrow,top:4dlu:noGrow,center:86px:noGrow,top:4dlu:noGrow,center:19px:noGrow"));
        pathToClientsFileLabel = new JLabel();
        pathToClientsFileLabel.setAutoscrolls(true);
        pathToClientsFileLabel.setEnabled(true);
        pathToClientsFileLabel.setText("Path to Clients File");
        CellConstraints cc = new CellConstraints();
        mainPanel.add(pathToClientsFileLabel, cc.xy(5, 3));
        pathToCampaignFileLabel = new JLabel();
        pathToCampaignFileLabel.setText("Path To Campaign File");
        mainPanel.add(pathToCampaignFileLabel, cc.xy(5, 5));
        pathToResultsFileLabel = new JLabel();
        pathToResultsFileLabel.setText("Path to Results File");
        mainPanel.add(pathToResultsFileLabel, cc.xy(5, 7));
        resultsButton = new JButton();
        resultsButton.setEnabled(true);
        resultsButton.setHorizontalTextPosition(0);
        resultsButton.setIcon(new ImageIcon(getClass().getResource("/icons/save.png")));
        this.$$$loadButtonText$$$(resultsButton, ResourceBundle.getBundle("localization/bundle").getString("save.results.button.title"));
        resultsButton.setToolTipText(ResourceBundle.getBundle("localization/bundle").getString("save.results.tooltip"));
        mainPanel.add(resultsButton, cc.xy(3, 7));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, cc.xy(3, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        campaignButton = new JButton();
        campaignButton.setHorizontalTextPosition(0);
        campaignButton.setIcon(new ImageIcon(getClass().getResource("/icons/open_folder_yellow.png")));
        this.$$$loadButtonText$$$(campaignButton, ResourceBundle.getBundle("localization/bundle").getString("open.campaign.button.title"));
        campaignButton.setToolTipText(ResourceBundle.getBundle("localization/bundle").getString("open.campaign.file.tooltip"));
        mainPanel.add(campaignButton, cc.xy(3, 5));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, cc.xywh(1, 1, 2, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, cc.xyw(3, 1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        clientsButton = new JButton();
        clientsButton.setHideActionText(false);
        clientsButton.setHorizontalTextPosition(0);
        clientsButton.setIcon(new ImageIcon(getClass().getResource("/icons/open_folder_yellow.png")));
        this.$$$loadButtonText$$$(clientsButton, ResourceBundle.getBundle("localization/bundle").getString("open.clients.button.title"));
        clientsButton.setToolTipText(ResourceBundle.getBundle("localization/bundle").getString("open.clients.file.tooltip"));
        mainPanel.add(clientsButton, cc.xy(3, 3));
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
