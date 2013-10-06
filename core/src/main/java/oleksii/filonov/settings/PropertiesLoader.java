package oleksii.filonov.settings;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public final class PropertiesLoader {

	public static Settings loadDefaultProperties() throws IOException {
		final Properties prop = new Properties();
		prop.load(PropertiesLoader.class.getResourceAsStream("/config/settings.properties"));
		return populateSettings(prop);
	}

	public static Settings loadPropertiesFrom(final Path pathToPropertiesFile) throws IOException {
		final Properties prop = new Properties();
		prop.load(new FileReader(pathToPropertiesFile.toFile()));
		return populateSettings(prop);
	}

	private static Settings populateSettings(final Properties prop) {
		Settings settings = new Settings();
		settings.setCampaignColumnNumberCampaignTitle(prop.getProperty("campaign.column.title.numberCampaign"));
		settings.setCampaignColumnDescriptionTitle(prop.getProperty("campaign.column.title.description"));
		settings.setCampaignColumnVinListIdTitle(prop.getProperty("campaign.column.title.vinListId"));
		settings.setClientColumnBodyNumber(prop.getProperty("client.column.title.bodyNumber"));
        return settings;
	}

}
