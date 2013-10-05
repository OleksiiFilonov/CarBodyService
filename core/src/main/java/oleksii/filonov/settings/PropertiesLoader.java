package oleksii.filonov.settings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesLoader {

	private Settings settings;

	public Settings loadDefaultProperties() throws IOException {
		final Properties prop = new Properties();
		prop.load(getClass().getResourceAsStream(Paths.get("/", "config", "settings.properties").toString()));
		return populateSettings(prop);
	}

	public Settings loadPropertiesFrom(final Path pathToPropertiesFile) throws FileNotFoundException, IOException {
		final Properties prop = new Properties();
		prop.load(new FileReader(pathToPropertiesFile.toFile()));
		return populateSettings(prop);
	}

	private Settings populateSettings(final Properties prop) {
		settings = new Settings();
		settings.setCampaignColumnNumberCampaignTitle(prop.getProperty("campaign.column.title.numberCampaign"));
		settings.setCampaignColumnDescriptionTitle(prop.getProperty("campaign.column.title.description"));
		settings.setCampaignColumnVinListIdTitle(prop.getProperty("campaign.column.title.vinListId"));
		settings.setClientColumnBodyNumber(prop.getProperty("client.column.title.bodyNumber"));
        return settings;
	}

}
