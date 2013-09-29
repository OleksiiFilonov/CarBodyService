package oleksii.filonov.readers;

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
    private static final String VIN_LIST_TITLE = "Список VIN";
    private static final String VIN_DESCRIPTION = "Описание";

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

    @Test
    public void findCellFrom() throws IOException, InvalidFormatException {
        final Iterator<Row> rowIterator = getCampaignVinListIterator();
        final Cell vinLIstTitleCell = columnReaderHelper.findCell(rowIterator, VIN_LIST_TITLE);
        final Cell descriptionTitleCell = columnReaderHelper.findCellFrom(vinLIstTitleCell, rowIterator, VIN_DESCRIPTION);
        assertEquals(VIN_DESCRIPTION, descriptionTitleCell.getStringCellValue());
    }

    @Test(expected = ReadDataException.class)
    public void shouldNotFindCellFromBecauseOfInvalidOrder() throws IOException, InvalidFormatException {
        final Iterator<Row> rowIterator = getCampaignVinListIterator();
        final Cell vinLIstTitleCell = columnReaderHelper.findCell(rowIterator, VIN_DESCRIPTION);
        final Cell descriptionTitleCell = columnReaderHelper.findCellFrom(vinLIstTitleCell, rowIterator, VIN_LIST_TITLE);
        assertEquals(VIN_DESCRIPTION, descriptionTitleCell.getStringCellValue());
    }

    @Test
    @Ignore
    public void printHyperLinksFromCampaign() throws InvalidFormatException, IOException {
        final Iterator<Row> rowIterator = getCampaignVinListIterator();
        final int columnIndex = this.columnReaderHelper.findCell(rowIterator, VIN_LIST_TITLE).getColumnIndex();
        while(rowIterator.hasNext()) {
            final Row row = rowIterator.next();
            final Cell cell = row.getCell(columnIndex);
            if(this.columnReaderHelper.isStringType(cell)) {
                System.out.println(cell.getHyperlink().getAddress());
            }
        }
    }

    private Iterator<Row> getCampaignVinListIterator() throws IOException, InvalidFormatException {
        final Workbook campaignWB = WorkbookFactory.create(CAMPAIGN_FILE);
        final Sheet campaignSheet = campaignWB.getSheetAt(0);
        return campaignSheet.rowIterator();
    }


}
