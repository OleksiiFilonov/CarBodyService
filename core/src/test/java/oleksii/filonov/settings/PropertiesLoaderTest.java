package oleksii.filonov.settings;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class PropertiesLoaderTest {

    private PropertiesLoader propertiesLoader = new PropertiesLoader();

    @Test
    public void loadProperties() throws IOException {
        propertiesLoader.loadDefaultProperties();
        Settings settings = propertiesLoader.getSettings();
        assertEquals("Номер кампании", settings.getCampaignColumnNumberCampaignTitle());
        assertEquals("Описание", settings.getCampaignColumnDescriptionTitle());
        assertEquals("VIN", settings.getCampaignColumnVinListIdTitle());
        assertEquals("Номер кузова", settings.getClientColumnBodyNumber());
    }
}
