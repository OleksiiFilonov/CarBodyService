package oleksii.filonov.reader;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;

public class ExcelReader {

    private final ColumnExcelReader columnExcelReader;

    public ExcelReader() {
        this.columnExcelReader = new ColumnExcelReader();
        this.columnExcelReader.setColumnReaderHelper(new ColumnReaderHelper());
    }

    public Cell[] readFirstSheetUniqueValues(final File fileToRead, final String columnMarker) {
        try {
            Workbook workbook = WorkbookFactory.create(fileToRead);
            return this.columnExcelReader.getColumnValues(workbook.getSheetAt(0), columnMarker);
        } catch(InvalidFormatException | IOException e) {
            throw new ReadDataException("Exception when try to read clients info from file " + fileToRead, e);
        }
    }

}
