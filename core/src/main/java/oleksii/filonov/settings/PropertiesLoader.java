package oleksii.filonov.settings;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesLoader {

    private Settings settings;

    public Properties loadProperties() throws IOException {
        Properties prop = new Properties();
        System.out.println("-" + Paths.get("", "src", "main" , "resources", "config", "settings.properties").toAbsolutePath().toString() + "-");
        prop.load(new FileReader(Paths.get("", "src", "main", "resources", "config", "settings.properties").toFile()));
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
