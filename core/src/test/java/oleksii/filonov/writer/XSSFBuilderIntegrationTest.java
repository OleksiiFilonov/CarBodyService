package oleksii.filonov.writer;

import com.google.common.collect.ListMultimap;
import oleksii.filonov.TestConstants;
import oleksii.filonov.reader.CampaignProcessor;
import oleksii.filonov.reader.ColumnExcelReader;
import oleksii.filonov.reader.ColumnReaderHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class XSSFBuilderIntegrationTest {

	private static final String LINKED_SHEET_NAME = "Body";
	private static final String BODY_ID_MARKER = "Номер кузова";
	private static final String VIN_MARKER = "VIN";

	private static final String[] COMPAIGN_FILE = new String[] { "src", "test", "resources", "Campaign.xlsx" };
	private static final File CAMPAIGN_FILE = Paths.get(".", COMPAIGN_FILE).toFile();
	private static final String[] BODY_ID_SOURCE = new String[] { "src", "test", "resources", "Clients.xls" };

	private Sheet bodyIdSheet;

	private ColumnReaderHelper columnReaderHelper;

	private XSSFBuilder excelBuilder;

	private ColumnExcelReader columnExcelReader;

	private CampaignProcessor campaignProcessor;

	@Before
	public void setUp() throws InvalidFormatException, IOException {
        if(!Files.exists(TestConstants.TARGET_RESOURCE)) {
            Files.createDirectory(TestConstants.TARGET_RESOURCE);
        }
		columnReaderHelper = new ColumnReaderHelper();
		campaignProcessor = new CampaignProcessor();
		campaignProcessor.setColumnReaderHelper(columnReaderHelper);
		excelBuilder = new XSSFBuilder();
		final Workbook clientWB = WorkbookFactory.create(Paths.get(".", BODY_ID_SOURCE).toFile());
		bodyIdSheet = clientWB.getSheetAt(0);
		columnExcelReader = new ColumnExcelReader();
		columnExcelReader.setColumnReaderHelper(columnReaderHelper);
	}

	@Test
	public void formLinkedDocument() throws IOException {
		excelBuilder.createDocument();
		excelBuilder.createLinkedSheetWithName(LINKED_SHEET_NAME);
		final String[] uniqueBodyIds = columnExcelReader.getUniqueColumnValues(bodyIdSheet, BODY_ID_MARKER);
		excelBuilder.writeBodyIdsColumnToLinkedSheet(BODY_ID_MARKER, uniqueBodyIds);
		final ListMultimap<String, String> linkedBodyIdWithCampaigns = campaignProcessor.linkBodyIdWithCampaigns(
				uniqueBodyIds, CAMPAIGN_FILE, VIN_MARKER);
		excelBuilder.linkExistingBodyIds(linkedBodyIdWithCampaigns, CAMPAIGN_FILE.getName());
		excelBuilder.saveToFile(TestConstants.LINKED_RESULT_FILE.toFile());
	}

    @Test
    public void printHyperLinksFromResultLink() throws InvalidFormatException, IOException {
        final Workbook clientWB = WorkbookFactory.create(TestConstants.LINKED_RESULT_FILE.toFile());
        final Sheet compaignSheet = clientWB.getSheetAt(0);
        final Iterator<Row> rows = compaignSheet.rowIterator();
        rows.next();
        final int columnIndex = 1;
        while(rows.hasNext()) {
            final Row row = rows.next();
            final Cell cell = row.getCell(columnIndex);
            if(this.columnReaderHelper.isStringType(cell)) {
                System.out.println(cell.getRowIndex() + ":" + cell.getHyperlink().getAddress());
            }
        }
    }
}
