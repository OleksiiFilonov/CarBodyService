package oleksii.filonov.gui;


import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class MainMenuBar extends JMenuBar {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("localization/bundle");


    public MainMenuBar() {
        final JMenu settingsMenu = new JMenu(resourceBundle.getString("menu.title.settings"));
        add(settingsMenu);
        settingsMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                SettingsDialog.fireSettingsDialog();
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }
}