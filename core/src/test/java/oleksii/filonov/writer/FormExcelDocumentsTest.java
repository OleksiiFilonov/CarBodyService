package oleksii.filonov.writer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.Before;
import org.junit.Test;

public class FormExcelDocumentsTest {

	private static final String FIRST_BODY_ID = "KMHBT51DBBU022001";
	private static final String SECOND_BODY_ID = "KMHSU81XDDU112002";
	private static final String THIRD_BODY_ID = "KMHEC41BBCA350003";
	private static final String REAL_BODY_ID = "KMHEC41BBBA289488";
	private static final String[] BODY_IDS = new String[] { FIRST_BODY_ID, SECOND_BODY_ID, THIRD_BODY_ID };
	private static final String[] RESULT_FILE = new String[] { "src", "test", "resources", "result.xlsx" };
	private static final String[] LINKED_RESULT_FILE = new String[] { "src", "test", "resources", "resultLinks.xlsx" };
	private static final String[] COMPAIGN_FILE = new String[] { "src", "test", "resources", "Campaign.xlsx" };

	private final DataBuilder excelBuilder = new XSSFBuilder();

	@Before
	public void setUp() {
		excelBuilder.createDocument();
		excelBuilder.createMainSheetWithName("Body");
	}

	@Test
	public void fillResultFileWithBodyId() throws IOException {
		excelBuilder.writeBodyIdsColumnToMainPage(BODY_IDS);
		excelBuilder.saveToFile(Paths.get(".", RESULT_FILE).toFile());
		final Map<String, Cell> bodyIdCellMap = excelBuilder.getBodyIdCellMap();
		assertEquals(FIRST_BODY_ID, bodyIdCellMap.get(FIRST_BODY_ID).getStringCellValue());
		assertEquals(SECOND_BODY_ID, bodyIdCellMap.get(SECOND_BODY_ID).getStringCellValue());
		assertEquals(THIRD_BODY_ID, bodyIdCellMap.get(THIRD_BODY_ID).getStringCellValue());
	}

	@Test
	public void formResultFileWitLinks() throws IOException {
		excelBuilder.writeBodyIdsColumnToMainPage(new String[] { REAL_BODY_ID });
		excelBuilder.linkExistingBodyIds(Paths.get(".", COMPAIGN_FILE).toFile());
		excelBuilder.saveToFile(Paths.get(".", LINKED_RESULT_FILE).toFile());
		final Map<String, Cell> bodyIdCellMap = excelBuilder.getBodyIdCellMap();
		final Cell linkedBodyIdCell = bodyIdCellMap.get(REAL_BODY_ID);
		assertEquals(REAL_BODY_ID, linkedBodyIdCell.getStringCellValue());
	}
}
