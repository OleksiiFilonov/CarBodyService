package oleksii.filonov.reader;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static oleksii.filonov.TestConstants.CAMPAIGN_FILE;
import static oleksii.filonov.TestConstants.CLIENT_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ColumnExcelReaderTest {

    private static final String WRONG_COLUMN_MARKER = "noSuchColumnName";
    private static final String BODY_ID_MARKER = "Номер кузова";
    private static final String VIN_MARKER = "VIN";

    private final ColumnExcelReader columnExcelReader = new ColumnExcelReader();

    private Sheet bodyIdSheet;

    @Before
    public void setUp() throws InvalidFormatException, IOException {
        final Workbook clientWB = WorkbookFactory.create(CLIENT_FILE);
        this.bodyIdSheet = clientWB.getSheetAt(0);
        this.columnExcelReader.setColumnReaderHelper(new ColumnReaderHelper());
    }

    @Test
    public void testReadFromXLSXClientsFile() {
        final String[] uniqueBodyIds = this.columnExcelReader.getColumnValues(this.bodyIdSheet, BODY_ID_MARKER);
        assertEquals("The first body id is not valid", "KMHBT51DBBU022069", uniqueBodyIds[0]);
        assertEquals("The tenth body id is not valid", "KMHST81CADU139882", uniqueBodyIds[9]);
        assertEquals("The last body id is not valid", "KMHEC41BBCA350931", uniqueBodyIds[uniqueBodyIds.length - 1]);
    }

    @Test
    public void dontFindTheMarkerColumn() {
        final String columnToRead = WRONG_COLUMN_MARKER;
        try {
            this.columnExcelReader.getColumnValues(this.bodyIdSheet, columnToRead);
            fail(String.format("The test shouldn't find the column \"%s\" in source file", columnToRead));
        } catch(final ReadDataException exc) {
        }
    }

    @Test
    public void readFromCompaign() throws FileNotFoundException, IOException, InvalidFormatException {
        final Workbook campaignWB = WorkbookFactory.create(CAMPAIGN_FILE);
        final int numbersOfSheet = campaignWB.getNumberOfSheets();
        final Sheet seconSheet = campaignWB.getSheetAt(1);
        final String[] firstVins = this.columnExcelReader.getColumnValues(seconSheet, VIN_MARKER);
        assertEquals("The amount of vins on the first sheet is incorrect", 505, firstVins.length);
        assertEquals("The first vin on a first sheet is wrong", "KMHEC41BABA263951", firstVins[0]);
        assertEquals("The tenth vin on a first sheet is wrong", "KMHEC41CBBA240950", firstVins[9]);
        assertEquals("The last vin on a first sheet is wrong", "KMHEC41CBCA305034", firstVins[firstVins.length - 1]);
        final Sheet lastSheet = campaignWB.getSheetAt(numbersOfSheet - 1);
        final String[] lastVins = this.columnExcelReader.getColumnValues(lastSheet, VIN_MARKER);
        assertEquals("The amount of vins on the last sheet is incorrect", 2130, lastVins.length);
        assertEquals("The first vin on a last sheet is wrong", "KMHSH81BDBU693756", lastVins[0]);
        assertEquals("The tenth vin on a last sheet is wrong", "KMHSH81BDAU645940", lastVins[9]);
        assertEquals("The last vin on a last sheet is wrong", "KMHSH81XDBU775331", lastVins[lastVins.length - 1]);
        for(int sheetIndex = 2; sheetIndex < numbersOfSheet; sheetIndex++) {
            final Sheet vinSheet = campaignWB.getSheetAt(sheetIndex);
            this.columnExcelReader.getColumnValues(vinSheet, VIN_MARKER);
        }
    }

}
