package oleksii.filonov.reader;

import static oleksii.filonov.TestConstants.CAMPAIGN_FILE;
import static oleksii.filonov.TestConstants.CLIENT_FILE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ColumnReaderHelperTest {

    private static final String BODY_ID_MARKER = "Номер кузова";
    private static final String WRONG_COLUMN_MARKER = "noSuchColumnName";
    private static final int BODY_ID_COLUMN_INDEX = 5;

    private Sheet bodyIdSheet;

    private final ColumnReaderHelper columnReaderHelper = new ColumnReaderHelper();

    @Before
    public void setUp() throws InvalidFormatException, IOException {
        final Workbook clientWB = WorkbookFactory.create(CLIENT_FILE);
        this.bodyIdSheet = clientWB.getSheetAt(0);
    }

    @Test
    public void findBodyIdColumnIndex() {
        assertEquals(BODY_ID_COLUMN_INDEX,
                this.columnReaderHelper.findCell(this.bodyIdSheet.iterator(), BODY_ID_MARKER).getColumnIndex());
    }

    @Test
    public void shouldNotFindBodyIdColumnIndex() {
        final String columnToRead = WRONG_COLUMN_MARKER;
        try {
            this.columnReaderHelper.findCell(this.bodyIdSheet.iterator(), columnToRead);
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
        final Workbook clientWB = WorkbookFactory.create(CAMPAIGN_FILE);
        final Sheet campaignSheet = clientWB.getSheetAt(0);
        final Iterator<Row> rowIterator = campaignSheet.rowIterator();
        final int columnIndex = this.columnReaderHelper.findCell(rowIterator, "Список VIN").getColumnIndex();
        while(rowIterator.hasNext()) {
            final Row row = rowIterator.next();
            final Cell cell = row.getCell(columnIndex);
            if(this.columnReaderHelper.isStringType(cell)) {
                System.out.println(cell.getHyperlink().getAddress());
            }
        }
    }


}
