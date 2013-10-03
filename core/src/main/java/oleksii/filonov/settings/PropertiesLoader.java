package oleksii.filonov.settings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesLoader {

	private Settings settings;

	public Properties loadDefaultProperties() throws IOException {
		final Properties prop = new Properties();
		prop.load(getClass().getResourceAsStream(Paths.get("/", "config", "settings.properties").toString()));
		populateSettings(prop);
		return prop;
	}

	public Properties loadPropertiesFrom(final Path pathToPropertiesFile) throws FileNotFoundException, IOException {
		final Properties prop = new Properties();
		prop.load(new FileReader(pathToPropertiesFile.toFile()));
		populateSettings(prop);
		return prop;
	}

	private void populateSettings(final Properties prop) {
		settings = new Settings();
		settings.setCampaignColumnNumberCampaignTitle(prop.getProperty("campaign.column.title.numberCampaign"));
		settings.setCampaignColumnDescriptionTitle(prop.getProperty("campaign.column.title.description"));
		settings.setCampaignColumnVinListIdTitle(prop.getProperty("campaign.column.title.vinListId"));
		settings.setClientColumnBodyNumber(prop.getProperty("client.column.title.bodyNumber"));
	}

	public Settings getSettings() {
		return settings;
	}
}
