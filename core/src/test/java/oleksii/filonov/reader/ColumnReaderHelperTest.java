package oleksii.filonov.reader;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ColumnReaderHelperTest {

    private static final String BODY_ID_MARKER = "Номер кузова";
    private static final String WRONG_COLUMN_MARKER = "noSuchColumnName";
    private static final String[] BODY_ID_SOURCE = new String[] { "src", "test", "resources", "Clients.xls" };
    private static final String[] COMPAIGN_FILE = new String[] { "src", "test", "resources", "Campaign.xlsx" };
    private static final int BODY_ID_COLUMN_INDEX = 5;

    private Sheet bodyIdSheet;

    private final ColumnReaderHelper columnReaderHelper = new ColumnReaderHelper();

    @Before
    public void setUp() throws InvalidFormatException, IOException {
        final Workbook clientWB = WorkbookFactory.create(Paths.get(".", BODY_ID_SOURCE).toFile());
        this.bodyIdSheet = clientWB.getSheetAt(0);
    }

    @Test
    public void findBodyIdColumnIndex() {
        assertEquals(BODY_ID_COLUMN_INDEX,
                this.columnReaderHelper.findColumnCell(this.bodyIdSheet.iterator(), BODY_ID_MARKER));
    }

    @Test
    public void shouldNotFindBodyIdColumnIndex() {
        final String columnToRead = WRONG_COLUMN_MARKER;
        try {
            this.columnReaderHelper.findColumnCell(this.bodyIdSheet.iterator(), columnToRead);
            fail(String.format("The test shouldn't find the column \"%s\" in source file", columnToRead));
        } catch(final ReadDataException exc) {
        }
    }

    @Test
    public void checkCellStringType() {
        final Cell stringTypeCell = mock(Cell.class);
        when(stringTypeCell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
        assertTrue(this.columnReaderHelper.isStringType(stringTypeCell));
    }

    @Test
    public void checkCellIsNull() {
        final Cell nullCell = null;
        assertFalse(this.columnReaderHelper.isStringType(nullCell));
    }

    @Test
    public void checkNumericType() {
        final Cell stringTypeCell = mock(Cell.class);
        when(stringTypeCell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
        assertFalse(this.columnReaderHelper.isStringType(stringTypeCell));
    }

    @Ignore
    @Test
    public void printHyperLinksFromCompaign() throws InvalidFormatException, IOException {
        final Workbook clientWB = WorkbookFactory.create(Paths.get("", COMPAIGN_FILE).toFile());
        final Sheet compaignSheet = clientWB.getSheetAt(0);
        final Iterator<Row> rows = compaignSheet.rowIterator();
        final int columnIndex = this.columnReaderHelper.findColumnCell(rows, "Список VIN");
        while(rows.hasNext()) {
            final Row row = rows.next();
            final Cell cell = row.getCell(columnIndex);
            if(this.columnReaderHelper.isStringType(cell)) {
                System.out.println(cell.getHyperlink().getAddress());
            }
        }
    }


}
