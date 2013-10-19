package oleksii.filonov.settings;

import org.junit.Test;

import java.io.IOException;

import static oleksii.filonov.TestConstants.ALTERNATIVE_SETTINGS_PATH;
import static org.junit.Assert.assertEquals;

public class PropertiesLoaderTest {

    @Test
    public void loadDefaultProperties() throws IOException {
        final Settings settings = PropertiesLoader.loadDefaultProperties();
        assertEquals("Номер кампании", settings.getCampaignColumnNumberCampaignTitle());
        assertEquals("Описание", settings.getCampaignColumnDescriptionTitle());
        assertEquals("VIN", settings.getCampaignColumnVinListIdTitle());
        assertEquals("VIN номер автомобиля", settings.getClientColumnBodyNumber());
    }

    @Test
    public void loadPropertiesFrom() throws IOException {
        final Settings settings = PropertiesLoader.loadPropertiesFrom(ALTERNATIVE_SETTINGS_PATH);
        assertEquals("Номер кампании", settings.getCampaignColumnNumberCampaignTitle());
        assertEquals("Описание", settings.getCampaignColumnDescriptionTitle());
        assertEquals("VIN", settings.getCampaignColumnVinListIdTitle());
        assertEquals("Номер кузова", settings.getClientColumnBodyNumber());
    }
}
