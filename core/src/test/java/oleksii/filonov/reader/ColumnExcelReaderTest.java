package oleksii.filonov.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;

public class ColumnExcelReaderTest {

    private static final String[] BODY_ID_SOURCE = new String[] { "src", "test", "resources", "Clients.xls" };
    private static final String[] COMPAIGN_SOURCE = new String[] { "src", "test", "resources", "Campaign.xlsx" };

    private static final String WRONG_COLUMN_MARKER = "noSuchColumnName";
    private static final String BODY_ID_MARKER = "Номер кузова";
    private static final String VIN_MARKER = "VIN";

    private final ColumnExcelReader columnExcelReader = new ColumnExcelReader();

    private Sheet bodyIdSheet;

    @Before
    public void setUp() throws InvalidFormatException, IOException {
        final Workbook clientWB = WorkbookFactory.create(Paths.get(".", BODY_ID_SOURCE).toFile());
        this.bodyIdSheet = clientWB.getSheetAt(0);
        this.columnExcelReader.setColumnReaderHelper(new ColumnReaderHelper());
    }

    @Test
    public void testReadFromXLSXClientsFile() {
        final String[] uniqueBodyIds = this.columnExcelReader.getUniqueColumnValues(this.bodyIdSheet, BODY_ID_MARKER);
        assertEquals("The first body id is not valid", "KMHBT51DBBU022069", uniqueBodyIds[0]);
        assertEquals("The tenth body id is not valid", "KMHST81CADU139882", uniqueBodyIds[9]);
        assertEquals("The last body id is not valid", "KMHEC41BBCA350931", uniqueBodyIds[uniqueBodyIds.length - 1]);
    }

    @Test
    public void dontFindTheMarkerColumn() {
        final String columnToRead = WRONG_COLUMN_MARKER;
        try {
            this.columnExcelReader.getUniqueColumnValues(this.bodyIdSheet, columnToRead);
            fail(String.format("The test shouldn't find the column \"%s\" in source file", columnToRead));
        } catch(final ReadDataException exc) {
        }
    }

    @Test
    public void readFromCompaign() throws FileNotFoundException, IOException, InvalidFormatException {
        final Workbook compaignWB = WorkbookFactory.create(Paths.get("", COMPAIGN_SOURCE).toFile());
        final int numbersOfSheet = compaignWB.getNumberOfSheets();
        final Sheet seconSheet = compaignWB.getSheetAt(1);
        final String[] firstVins = this.columnExcelReader.getUniqueColumnValues(seconSheet, VIN_MARKER);
        assertEquals("The amount of vins on the first sheet is incorrect", 505, firstVins.length);
        assertEquals("The first vin on a first sheet is wrong", "KMHEC41BABA263951", firstVins[0]);
        assertEquals("The tenth vin on a first sheet is wrong", "KMHEC41CBBA240950", firstVins[9]);
        assertEquals("The last vin on a first sheet is wrong", "KMHEC41CBCA305034", firstVins[firstVins.length - 1]);
        final Sheet lastSheet = compaignWB.getSheetAt(numbersOfSheet - 1);
        final String[] lastVins = this.columnExcelReader.getUniqueColumnValues(lastSheet, VIN_MARKER);
        assertEquals("The amount of vins on the last sheet is incorrect", 2130, lastVins.length);
        assertEquals("The first vin on a last sheet is wrong", "KMHSH81BDBU693756", lastVins[0]);
        assertEquals("The tenth vin on a last sheet is wrong", "KMHSH81BDAU645940", lastVins[9]);
        assertEquals("The last vin on a last sheet is wrong", "KMHSH81XDBU775331", lastVins[lastVins.length - 1]);
        for(int sheetIndex = 2; sheetIndex < numbersOfSheet; sheetIndex++) {
            final Sheet vinSheet = compaignWB.getSheetAt(sheetIndex);
            this.columnExcelReader.getUniqueColumnValues(vinSheet, VIN_MARKER);
        }
    }

}
