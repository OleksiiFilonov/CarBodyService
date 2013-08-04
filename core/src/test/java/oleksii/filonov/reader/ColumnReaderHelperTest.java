package oleksii.filonov.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;

public class ColumnReaderHelperTest {

	private static final String BODY_ID_MARKER = "Номер кузова";
	private static final String WRONG_COLUMN_MARKER = "noSuchColumnName";
	private static final String[] BODY_ID_SOURCE = new String[] { "src", "test", "resources", "Clients.xls" };
	private static final int BODY_ID_COLUMN_INDEX = 5;

	private Sheet bodyIdSheet;

	private final ColumnReaderHelper columnReaderHelper = new ColumnReaderHelper();

	@Before
	public void setUp() throws InvalidFormatException, IOException {
		final Workbook clientWB = WorkbookFactory.create(Paths.get(".", BODY_ID_SOURCE).toFile());
		bodyIdSheet = clientWB.getSheetAt(0);
	}

	@Test
	public void findBodyIdColumnIndex() {
		assertEquals(BODY_ID_COLUMN_INDEX, columnReaderHelper.findColumnIndex(bodyIdSheet.iterator(), BODY_ID_MARKER));
	}

	@Test
	public void willNotFindBodyIdColumnIndex() {
		final String columnToRead = WRONG_COLUMN_MARKER;
		try {
			columnReaderHelper.findColumnIndex(bodyIdSheet.iterator(), columnToRead);
			fail(String.format("The test shouldn't find the column \"%s\" in source file", columnToRead));
		} catch (final ReadDataException exc) {
		}
	}

	@Test
	public void checkCellStringType() {
		final Cell stringTypeCell = mock(Cell.class);
		when(stringTypeCell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
		assertTrue(columnReaderHelper.isStringType(stringTypeCell));
	}

	@Test
	public void checkCellIsNull() {
		final Cell nullCell = null;
		assertFalse(columnReaderHelper.isStringType(nullCell));
	}

	@Test
	public void checkNumericType() {
		final Cell stringTypeCell = mock(Cell.class);
		when(stringTypeCell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
		assertFalse(columnReaderHelper.isStringType(stringTypeCell));
	}
}
