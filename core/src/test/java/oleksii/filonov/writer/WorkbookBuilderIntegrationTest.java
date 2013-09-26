package oleksii.filonov.writer;

import static oleksii.filonov.TestConstants.*;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import com.google.common.collect.*;
import oleksii.filonov.reader.*;
import org.apache.poi.openxml4j.exceptions.*;
import org.apache.poi.ss.usermodel.*;
import org.hamcrest.*;
import org.junit.*;

public class WorkbookBuilderIntegrationTest {

	private static final String BODY_ID_MARKER = "Номер кузова";
	private static final String VIN_MARKER = "VIN";
    private static final int VIN_LINK_COLUMN_INDEX = 6;
    private static final Matcher<Integer> LINK_COL_INDEX_MATCHER = CoreMatchers.equalTo(VIN_LINK_COLUMN_INDEX);

    private ColumnReaderHelper columnReaderHelper;

    private ColumnExcelReader columnExcelReader;

	private CampaignProcessor campaignProcessor;

	@Before
	public void setUp() throws InvalidFormatException, IOException {
        if(!Files.exists(TARGET_RESOURCE)) {
            Files.createDirectory(TARGET_RESOURCE);
        }
		columnReaderHelper = new ColumnReaderHelper();
		campaignProcessor = new CampaignProcessor();
		campaignProcessor.setColumnReaderHelper(columnReaderHelper);
        columnExcelReader = new ColumnExcelReader();
        columnExcelReader.setColumnReaderHelper(columnReaderHelper);
    }

	@Test
	public void formLinkedDocument() throws IOException, InvalidFormatException {
        final Workbook clientWB = WorkbookFactory.create(CLIENT_FILE);
        final Sheet clientSheet = clientWB.getSheetAt(0);
        final Cell[] bodyIds = columnExcelReader.getColumnValues(clientSheet, BODY_ID_MARKER);
        DataBuilder excelBuilder = new WorkbookBuilder();
        excelBuilder.useWorkbook(clientWB);
        excelBuilder.setPathToCampaignFile(CAMPAIGN_FILE.getName());
		final ListMultimap<String, Cell> linkedBodyIdWithCampaigns = campaignProcessor.linkBodyIdWithCampaigns(
				bodyIds, CAMPAIGN_FILE, VIN_MARKER);
        final Map<String, String> bodyIdDescriptionMap = new HashMap<>();
        excelBuilder.assignTasks(bodyIds, bodyIdDescriptionMap, linkedBodyIdWithCampaigns);
		excelBuilder.saveToFile(LINKED_RESULT_PATH.toFile());
        final Workbook workbookForVerification = WorkbookFactory.create(LINKED_RESULT_PATH.toFile());
        final Sheet verifyClientSheet = workbookForVerification.getSheetAt(0);
        final Iterator<Row> clientIterator = verifyClientSheet.rowIterator();
        //check for cell type
        assertThat(getColumnIndex(clientIterator, "10C150"), LINK_COL_INDEX_MATCHER);
        assertThat(getColumnIndex(clientIterator, "10C116"), LINK_COL_INDEX_MATCHER);
        assertThat(getColumnIndex(clientIterator, "10C150"), LINK_COL_INDEX_MATCHER);
        assertThat(getColumnIndex(clientIterator, "10CR07"), LINK_COL_INDEX_MATCHER);
        assertThat(getColumnIndex(clientIterator, "10CR08"), LINK_COL_INDEX_MATCHER);
        assertThat(getColumnIndex(clientIterator, "20CR22"), LINK_COL_INDEX_MATCHER);
    }

    private int getColumnIndex(Iterator<Row> clientIterator, String lookUpValue) {
        return columnReaderHelper.findCell(clientIterator, lookUpValue).getColumnIndex();
    }

    @Test
    public void read() throws IOException, InvalidFormatException {
        final Workbook workbookForVerification = WorkbookFactory.create(LINKED_RESULT_PATH.toFile());
        final Sheet verifyClientSheet = workbookForVerification.getSheetAt(0);
        final Iterator<Row> clientIterator = verifyClientSheet.rowIterator();
        while (clientIterator.hasNext()) {
            Row row = clientIterator.next();
            Cell cell = row.getCell(6);
            if (cell != null) {
                Hyperlink hyperlink = cell.getHyperlink();
                if (hyperlink != null)
                    System.out.println(hyperlink.getAddress());
            }
        }
    }

}
