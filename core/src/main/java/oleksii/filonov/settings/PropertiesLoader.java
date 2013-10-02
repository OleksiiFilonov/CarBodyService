package oleksii.filonov.settings;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesLoader {

	private Settings settings;

	public Properties loadProperties() throws IOException {
		final Properties prop = new Properties();
		prop.load(getClass().getResourceAsStream(Paths.get("/", "config", "settings.properties").toString()));
		settings = new Settings();
		settings.setCampaignColumnNumberCampaignTitle(prop.getProperty("campaign.column.title.numberCampaign"));
		settings.setCampaignColumnDescriptionTitle(prop.getProperty("campaign.column.title.description"));
		settings.setCampaignColumnVinListIdTitle(prop.getProperty("campaign.column.title.vinListId"));
		settings.setClientColumnBodyNumber(prop.getProperty("client.column.title.bodyNumber"));
		return prop;
	}

	public Settings getSettings() {
		return settings;
	}
}
