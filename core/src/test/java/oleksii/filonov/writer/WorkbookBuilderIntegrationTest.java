package oleksii.filonov.writer;

import com.google.common.collect.ListMultimap;
import oleksii.filonov.reader.CampaignProcessor;
import oleksii.filonov.reader.ColumnExcelReader;
import oleksii.filonov.reader.ColumnReaderHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

import static oleksii.filonov.TestConstants.*;

public class WorkbookBuilderIntegrationTest {

	private static final String LINKED_SHEET_NAME = "Body";
	private static final String BODY_ID_MARKER = "Номер кузова";
	private static final String VIN_MARKER = "VIN";

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
        DataBuilder excelBuilder = new WorkbookBuilder();
        final Workbook clientWB = WorkbookFactory.create(CLIENT_FILE);
        excelBuilder.createDocument(CLIENT_FILE);
		excelBuilder.createLinkedSheetWithName(LINKED_SHEET_NAME);
		final String[] uniqueBodyIds = columnExcelReader.getUniqueColumnValues(clientWB.getSheetAt(0), BODY_ID_MARKER);
		excelBuilder.writeBodyIdsColumnToLinkedSheet(BODY_ID_MARKER, uniqueBodyIds);
		final ListMultimap<String, String> linkedBodyIdWithCampaigns = campaignProcessor.linkBodyIdWithCampaigns(
				uniqueBodyIds, CAMPAIGN_FILE, VIN_MARKER);
		excelBuilder.linkExistingBodyIds(linkedBodyIdWithCampaigns, CAMPAIGN_FILE.getName());
		excelBuilder.saveToFile(LINKED_RESULT_PATH.toFile());
	}

    @Test
    public void printHyperLinksFromResultLink() throws InvalidFormatException, IOException {
        final Workbook clientWB = WorkbookFactory.create(LINKED_RESULT_PATH.toFile());
        final Sheet campaignSheet = clientWB.getSheet(LINKED_SHEET_NAME);
        final Iterator<Row> rows = campaignSheet.rowIterator();
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
