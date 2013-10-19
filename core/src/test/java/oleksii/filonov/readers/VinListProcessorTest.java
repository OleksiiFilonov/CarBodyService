package oleksii.filonov.readers;

import oleksii.filonov.TestConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class VinListProcessorTest {

    private static final int VIN_LIST_VARIANTS_NUMBER = 38;
    private VinListProcessor vinListProcessor;

    @Before
    public void setUp() {
        vinListProcessor = new VinListProcessor();
        vinListProcessor.setColumnReaderHelper(new ColumnReaderHelper());
    }


    @Test
    public void formVinListIdDescriptionMap() {
        final Map<String, String> vinListDescriptionMap = vinListProcessor.mapVinListIdToDescription(TestConstants.CAMPAIGN_FILE, "Номер кампании", "Описание");
        assertThat(vinListDescriptionMap.size(), equalTo(VIN_LIST_VARIANTS_NUMBER));
        assertEquals("ЗАМЕНА ПОЛОЖИТЕЛЬНОЙ КЛЕММЫ АККУМУЛЯТОРНОЙ БАТАРЕИ(TSB No. In English : HCE11-92-P550-YFHG)", vinListDescriptionMap.get("10C059"));
    }
}
