package oleksii.filonov.gui;


import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class MainMenuBar extends JMenuBar {


    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("localization/bundle", new EncodedControl());


    public MainMenuBar(final Frame owner) {
        final JMenu settingsMenu = new JMenu(resourceBundle.getString("menu.title.settings"));
        add(settingsMenu);
        settingsMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(final MenuEvent e) {
                SettingsDialog.fireSettingsDialog(owner);
            }

            @Override
            public void menuDeselected(final MenuEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void menuCanceled(final MenuEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }
}
