package oleksii.filonov.reader;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Map;

import oleksii.filonov.TestConstants;
import org.junit.Test;

public class VinListProcessorTest {

    private VinListProcessor vinListProcessor = new VinListProcessor();


    @Test
    public void formVinListIdDescriptionMap() {
        Map<String, String> vinListDescriptionMap = vinListProcessor.mapVinListIdToDescription(TestConstants.CAMPAIGN_FILE);
        assertThat(vinListDescriptionMap.size(), equalTo(38));
    }
}
