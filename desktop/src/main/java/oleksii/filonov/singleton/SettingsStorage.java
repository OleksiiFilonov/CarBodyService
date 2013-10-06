package oleksii.filonov.singleton;

import oleksii.filonov.settings.Settings;

public class SettingsStorage {

    private static Settings settings;

    public static Settings getSettings() {
        return settings;
    }

    public static void setSettings(Settings settings) {
        SettingsStorage.settings = settings;
    }
}
