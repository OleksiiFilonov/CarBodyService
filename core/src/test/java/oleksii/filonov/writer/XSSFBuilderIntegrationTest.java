package oleksii.filonov.writer;

import java.io.IOException;
import java.nio.file.Paths;

import oleksii.filonov.reader.ColumnExcelReader;
import oleksii.filonov.reader.ColumnReaderHelper;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;

public class XSSFBuilderIntegrationTest {

	private static final String LINKED_SHEET_NAME = "Body";
	private static String BODY_ID_MARKER = "Номер кузова";
	private static String VIN_MARKER = "VIN";

	private static final String[] LINKED_RESULT_FILE = new String[] { "src", "test", "resources", "resultLinks.xlsx" };
	private static final String[] COMPAIGN_FILE = new String[] { "src", "test", "resources", "Campaign.xlsx" };
	private static final String[] BODY_ID_SOURCE = new String[] { "src", "test", "resources", "Clients.xls" };

	private Sheet bodyIdSheet;

	private final ColumnReaderHelper columnReaderHelper = new ColumnReaderHelper();

	private final XSSFBuilder excelBuilder = new XSSFBuilder();

	private final ColumnExcelReader columnExcelReader = new ColumnExcelReader();

	@Before
	public void setUp() throws InvalidFormatException, IOException {
		excelBuilder.setColumnReaderHelper(columnReaderHelper);
		columnExcelReader.setColumnReaderHelper(columnReaderHelper);
		final Workbook clientWB = WorkbookFactory.create(Paths.get(".", BODY_ID_SOURCE).toFile());
		bodyIdSheet = clientWB.getSheetAt(0);
		columnExcelReader.setColumnReaderHelper(new ColumnReaderHelper());
	}

	@Test
	public void formLinkedDocument() throws IOException {
		excelBuilder.createDocument();
		excelBuilder.createLinkedSheetWithName(LINKED_SHEET_NAME);
		final String[] uniqueBodyIds = columnExcelReader.getUniqueColumnValues(bodyIdSheet, BODY_ID_MARKER);
		excelBuilder.writeBodyIdsColumnToLinkedSheet(BODY_ID_MARKER, uniqueBodyIds);
		excelBuilder.linkExistingBodyIds(VIN_MARKER, Paths.get(".", COMPAIGN_FILE).toFile());
		excelBuilder.saveToFile(Paths.get("", LINKED_RESULT_FILE).toFile());

	}
}
