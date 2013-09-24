package oleksii.filonov.reader;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Map;

import oleksii.filonov.TestConstants;
import org.junit.Before;
import org.junit.Test;

public class VinListProcessorTest {

    private VinListProcessor vinListProcessor;

    @Before
    public void setUp() {
        vinListProcessor = new VinListProcessor();
        vinListProcessor.setColumnReaderHelper(new ColumnReaderHelper());
    }


    @Test
    public void formVinListIdDescriptionMap() {
        Map<String, String> vinListDescriptionMap = vinListProcessor.mapVinListIdToDescription(TestConstants.CAMPAIGN_FILE, "Номер кампании", "Описание");
        assertThat(vinListDescriptionMap.size(), equalTo(38));
        assertEquals("ЗАМЕНА ПОЛОЖИТЕЛЬНОЙ КЛЕММЫ АККУМУЛЯТОРНОЙ БАТАРЕИ(TSB No. In English : HCE11-92-P550-YFHG)", vinListDescriptionMap.get("10C059"));
    }
}
