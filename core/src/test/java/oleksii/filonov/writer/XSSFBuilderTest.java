package oleksii.filonov.writer;

import com.google.common.collect.ListMultimap;
import oleksii.filonov.TestConstants;
import oleksii.filonov.reader.CampaignProcessor;
import oleksii.filonov.reader.ColumnReaderHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

import static oleksii.filonov.TestConstants.LINKED_RESULT_PATH;
import static oleksii.filonov.TestConstants.TARGET_RESOURCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XSSFBuilderTest {

    private static final String LINKED_SHEET_NAME = "Body";
	private static final String BODY_ID_MARKER = "Номер кузова";
	private static final String VIN_MARKER = "VIN";

	private static final String FIRST_BODY_ID = "KMHBT51DBBU022001";
	private static final String SECOND_BODY_ID = "KMHSU81XDDU112002";
	private static final String THIRD_BODY_ID = "KMHEC41BBCA350003";
	private static final String REAL_BODY_ID_FIRST_SHEET_ROW_ONE = "KMHEC41BABA263951";
	private static final String REAL_BODY_ID_FIRST_SHEET_ROW_TEN = "KMHEC41CBBA240950";
	private static final String NO_SUCH_BODY_ID_FIRST_SHEET_ROW_TEN = "KMH00000000000000";
	private static final String[] BODY_IDS = new String[] { FIRST_BODY_ID, SECOND_BODY_ID, THIRD_BODY_ID };

	private ColumnReaderHelper columnReaderHelper;
	private CampaignProcessor campaignProcessor;

	private XSSFBuilder excelBuilder;

	@Before
	public void setUp() throws IOException, InvalidFormatException {
        if(!Files.exists(TARGET_RESOURCE)) {
            Files.createDirectory(TARGET_RESOURCE);
        }
		excelBuilder = new XSSFBuilder();
		columnReaderHelper = new ColumnReaderHelper();
		campaignProcessor = new CampaignProcessor();
		campaignProcessor.setColumnReaderHelper(columnReaderHelper);
		excelBuilder.createDocument(TestConstants.CLIENT_FILE);
		excelBuilder.createLinkedSheetWithName(LINKED_SHEET_NAME);
	}

	@Test
	public void formResultFileWitLinks() throws IOException, InvalidFormatException {
		final String[] bodyIds = new String[] { REAL_BODY_ID_FIRST_SHEET_ROW_ONE, REAL_BODY_ID_FIRST_SHEET_ROW_TEN,
				NO_SUCH_BODY_ID_FIRST_SHEET_ROW_TEN };
		excelBuilder.writeBodyIdsColumnToLinkedSheet(BODY_ID_MARKER, bodyIds);
		final ListMultimap<String, String> bodyIdLinks = campaignProcessor.linkBodyIdWithCampaigns(bodyIds,
				TestConstants.CAMPAIGN_FILE, VIN_MARKER);
		excelBuilder.linkExistingBodyIds(bodyIdLinks, TestConstants.CAMPAIGN_FILE.getName());
		excelBuilder.saveToFile(LINKED_RESULT_PATH.toFile());
		final Cell[] bodyIdCells = excelBuilder.getBodyIdCells();
		assertEquals(REAL_BODY_ID_FIRST_SHEET_ROW_ONE, bodyIdCells[0].getStringCellValue());
		checkWrittenLinkedResultFile();
	}

	private void checkWrittenLinkedResultFile() throws IOException, InvalidFormatException {
		final Workbook linkedResultWB = WorkbookFactory.create(LINKED_RESULT_PATH.toFile());
		final Sheet linkedResultSheet = linkedResultWB.getSheet(LINKED_SHEET_NAME);
		final Iterator<Row> rows = linkedResultSheet.iterator();
		final int bodyIdColumnIndex = columnReaderHelper.findColumnIndex(rows, BODY_ID_MARKER);
		assertEquals(0, bodyIdColumnIndex);
		final Row firstVinRow = rows.next();
		final Cell firstBodyIdCell = firstVinRow.getCell(0);
		assertEquals(REAL_BODY_ID_FIRST_SHEET_ROW_ONE, firstBodyIdCell.getStringCellValue());
		final Cell firstVinCell = firstVinRow.getCell(1);
		assertEquals("'10C029'!B2", firstVinCell.getStringCellValue());
		final Row secondVinRow = rows.next();
		final Cell tenthCompaignVinCell = secondVinRow.getCell(1);
		assertEquals("'10C029'!B11", tenthCompaignVinCell.getStringCellValue());
		final Row thirdVinRow = rows.next();
		assertNull(thirdVinRow.getCell(1));
	}

}
