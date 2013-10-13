package oleksii.filonov.readers;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;

import static oleksii.filonov.TestConstants.CLIENT_FILE;
import static oleksii.filonov.TestConstants.CLIENT_FILE2;
import static org.junit.Assert.assertEquals;

public class ColumnExcelReaderTest {

    private ColumnExcelReader columnExcelReader;

    @Before
    public void setUp() throws Exception {
        columnExcelReader = new ColumnExcelReader();
        columnExcelReader.setColumnReaderHelper(new ColumnReaderHelper());
    }

    @Test
    public void findDistanceToEndWithoutOffset() throws Exception {
        final Workbook clientWB = WorkbookFactory.create(CLIENT_FILE);
        final Sheet bodyIdSheetClient = clientWB.getSheetAt(0);
        assertEquals("First client file has body id marker as a final column index",
                0, columnExcelReader.findDistanceToEndFrom("Номер кузова", bodyIdSheetClient));
    }

    @Test
    public void findDistanceToEndWithOffset() throws Exception {
        final Workbook clientWB = WorkbookFactory.create(CLIENT_FILE2);
        final Sheet bodyIdSheetClient = clientWB.getSheetAt(0);
        assertEquals("Second client file has offset to a final column index",
                2, columnExcelReader.findDistanceToEndFrom("VIN номер автомобиля", bodyIdSheetClient));
    }
}
