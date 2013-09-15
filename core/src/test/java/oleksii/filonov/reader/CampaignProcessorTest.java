package oleksii.filonov.reader;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ListMultimap;

public class CampaignProcessorTest {
	private static final int MAX_FOUND_LINKS = 3;

	private static final String VIN_MARKER = "VIN";

	private static final String REAL_BODY_ID_MEET_THREE_TIMES = "KMHSH81XDBU758082";
	private static final String REAL_BODY_ID_FIRST_SHEET_ROW_ONE = "KMHEC41BABA263951";
	private static final String REAL_BODY_ID_MEET_TWICE = "KMHDH41EBCU198780";
	private static final String REAL_BODY_ID_FIRST_SHEET_ROW_TEN = "KMHEC41CBBA240950";
	private static final String NO_SUCH_BODY_ID_FIRST_SHEET_ROW_TEN = "KMH00000000000000";
	private static final String REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER = "Z94CT41CBBR015022";

	private static final String[] BODY_IDS = new String[] { REAL_BODY_ID_MEET_THREE_TIMES,
			REAL_BODY_ID_FIRST_SHEET_ROW_ONE, REAL_BODY_ID_MEET_TWICE, REAL_BODY_ID_FIRST_SHEET_ROW_TEN,
			NO_SUCH_BODY_ID_FIRST_SHEET_ROW_TEN, REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER };
	private static final String[] COMPAIGN_FILE = new String[] { "src", "test", "resources", "Campaign.xlsx" };

	private CampaignProcessor processor;

	@Before
	public void setUp() {
		processor = new CampaignProcessor();
		processor.setColumnReaderHelper(new ColumnReaderHelper());
	}

	@Test
	public void linkBodyIdWithCampaig() {
		final ListMultimap<String, String> resultMap = processor.linkBodyIdWithCampaigns(BODY_IDS,
				Paths.get("", COMPAIGN_FILE).toFile(), VIN_MARKER);
		assertThat(processor.getMaxReferenceNumber(), equalTo(MAX_FOUND_LINKS));

		assertThat(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES).size(), equalTo(3));
		assertThat(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES).get(0), equalTo("'10C061'!B122"));
		assertThat(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES).get(1), equalTo("'10C116'!B335"));
		assertThat(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES).get(2), equalTo("'31C017'!B473"));

		assertThat(resultMap.get(REAL_BODY_ID_FIRST_SHEET_ROW_ONE).size(), equalTo(1));
		assertThat(resultMap.get(REAL_BODY_ID_FIRST_SHEET_ROW_ONE).get(0), equalTo("'10C029'!B2"));

		assertThat(resultMap.get(REAL_BODY_ID_MEET_TWICE).size(), equalTo(2));
		assertThat(resultMap.get(REAL_BODY_ID_MEET_TWICE).get(0), equalTo("'10C116'!B1085"));
		assertThat(resultMap.get(REAL_BODY_ID_MEET_TWICE).get(1), equalTo("'10C150'!B684"));

		assertThat(resultMap.get(REAL_BODY_ID_FIRST_SHEET_ROW_TEN).size(), equalTo(1));
		assertThat(resultMap.get(REAL_BODY_ID_FIRST_SHEET_ROW_TEN).get(0), equalTo("'10C029'!B11"));

		assertThat(resultMap.get(NO_SUCH_BODY_ID_FIRST_SHEET_ROW_TEN).size(), equalTo(0));

		assertThat(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER).size(), equalTo(3));
		assertThat(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER).get(0), equalTo("'10CR07'!B758"));
		assertThat(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER).get(1), equalTo("'10CR08'!B1045"));
		assertThat(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER).get(2), equalTo("'20CR22'!B10264"));
	}
}
