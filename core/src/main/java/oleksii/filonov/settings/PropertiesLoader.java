package oleksii.filonov.settings;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public final class PropertiesLoader {

    private static final String DEFAULT_CONFIG_PATH = "/config/settings.properties";

    public static Settings loadDefaultProperties() throws IOException {
        final Properties prop = new Properties();
        prop.load(PropertiesLoader.class.getResourceAsStream(DEFAULT_CONFIG_PATH));
        return populateSettings(prop);
    }

    public static Settings loadPropertiesFrom(final Path pathToPropertiesFile) throws IOException {
        final Properties prop = new Properties();
        prop.load(new FileReader(pathToPropertiesFile.toFile()));
        return populateSettings(prop);
    }

    private static Settings populateSettings(final Properties prop) {
        final Settings settings = new Settings();
        settings.setCampaignColumnNumberCampaignTitle(prop.getProperty("campaign.column.title.numberCampaign"));
        settings.setCampaignColumnDescriptionTitle(prop.getProperty("campaign.column.title.description"));
        settings.setCampaignColumnVinListIdTitle(prop.getProperty("campaign.column.title.vinListId"));
        settings.setClientColumnBodyNumber(prop.getProperty("client.column.title.bodyNumber"));
        return settings;
    }

}
