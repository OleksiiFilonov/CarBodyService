package oleksii.filonov.readers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import oleksii.filonov.TestConstants;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ListMultimap;

public class CampaignProcessorTest {

	private static final String VIN_MARKER = "VIN";

	private static final String REAL_BODY_ID_MEET_THREE_TIMES = "KMHSH81XDBU758082";
	private static final String REAL_BODY_ID_FIRST_SHEET_ROW_ONE = "KMHEC41BABA263951";
	private static final String REAL_BODY_ID_MEET_TWICE = "KMHDH41EBCU198780";
	private static final String REAL_BODY_ID_FIRST_SHEET_ROW_TEN = "KMHEC41CBBA240950";
	private static final String NO_SUCH_BODY_ID_FIRST_SHEET_ROW_TEN = "KMH00000000000000";
	private static final String REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER = "Z94CT41CBBR015022";

	private static final Cell[] BODY_IDS = new Cell[] { new StringCell(REAL_BODY_ID_MEET_THREE_TIMES),
			new StringCell(REAL_BODY_ID_FIRST_SHEET_ROW_ONE), new StringCell(REAL_BODY_ID_MEET_TWICE),
			new StringCell(REAL_BODY_ID_FIRST_SHEET_ROW_TEN), new StringCell(NO_SUCH_BODY_ID_FIRST_SHEET_ROW_TEN),
			new StringCell(REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER) };
	private static final int VIN_COLUMN_INDEX = 1;

	private CampaignProcessor processor;

	@Before
	public void setUp() {
		processor = new CampaignProcessor();
		processor.setColumnReaderHelper(new ColumnReaderHelper());
	}

	@Test
	public void linkBodyIdWithCampaign() {
		final ListMultimap<String, Cell> resultMap = processor.linkBodyIdWithCampaigns(BODY_IDS,
				TestConstants.CAMPAIGN_FILE, VIN_MARKER);

		assertThat(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES).size(), equalTo(3));
		assertVinListCell(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES).get(0), "10C061", 121);
		assertVinListCell(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES).get(1), "10C116", 334);
		assertVinListCell(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES).get(2), "31C017", 472);

		assertThat(resultMap.get(REAL_BODY_ID_FIRST_SHEET_ROW_ONE).size(), equalTo(1));
		assertVinListCell(resultMap.get(REAL_BODY_ID_FIRST_SHEET_ROW_ONE).get(0), "10C029", 1);

		assertThat(resultMap.get(REAL_BODY_ID_MEET_TWICE).size(), equalTo(2));
		assertVinListCell(resultMap.get(REAL_BODY_ID_MEET_TWICE).get(0), "10C116", 1084);
		assertVinListCell(resultMap.get(REAL_BODY_ID_MEET_TWICE).get(1), "10C150", 683);

		assertThat(resultMap.get(REAL_BODY_ID_FIRST_SHEET_ROW_TEN).size(), equalTo(1));
		assertVinListCell(resultMap.get(REAL_BODY_ID_FIRST_SHEET_ROW_TEN).get(0), "10C029", 10);

		assertThat(resultMap.get(NO_SUCH_BODY_ID_FIRST_SHEET_ROW_TEN).size(), equalTo(0));

		assertThat(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER).size(), equalTo(3));
		assertVinListCell(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER).get(0), "10CR07", 757);
		assertVinListCell(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER).get(1), "10CR08", 1044);
		assertVinListCell(resultMap.get(REAL_BODY_ID_MEET_THREE_TIMES_ANOTHER).get(2), "20CR22", 10263);
	}

	private void assertVinListCell(final Cell bodyIdFound, final String vinSheetName, final int vinRowIndex) {
		assertThat(bodyIdFound.getSheet().getSheetName(), equalTo(vinSheetName));
		assertThat(bodyIdFound.getColumnIndex(), equalTo(VIN_COLUMN_INDEX));
		assertThat(bodyIdFound.getRowIndex(), equalTo(vinRowIndex));
	}

}
