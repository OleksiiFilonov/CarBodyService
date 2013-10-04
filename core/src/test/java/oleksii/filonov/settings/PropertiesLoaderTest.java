package oleksii.filonov.settings;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

public class PropertiesLoaderTest {

	private final PropertiesLoader propertiesLoader = new PropertiesLoader();

	@Test
	public void loadDefaultProperties() throws IOException {
		propertiesLoader.loadDefaultProperties();
		final Settings settings = propertiesLoader.getSettings();
		assertEquals("Номер кампании", settings.getCampaignColumnNumberCampaignTitle());
		assertEquals("Описание", settings.getCampaignColumnDescriptionTitle());
		assertEquals("VIN", settings.getCampaignColumnVinListIdTitle());
		assertEquals("VIN номер автомобиля", settings.getClientColumnBodyNumber());
	}

	@Test
	public void loadPropertiesFrom() throws IOException {
		propertiesLoader.loadPropertiesFrom(Paths.get("", "src", "test", "resources", "config",
				"path_to_settings.properties"));
		final Settings settings = propertiesLoader.getSettings();
		assertEquals("Номер кампании", settings.getCampaignColumnNumberCampaignTitle());
		assertEquals("Описание", settings.getCampaignColumnDescriptionTitle());
		assertEquals("VIN", settings.getCampaignColumnVinListIdTitle());
		assertEquals("Номер кузова", settings.getClientColumnBodyNumber());
	}
}
