package oleksii.filonov.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ColumnExcelReader {

    private ColumnReaderHelper columnReaderHelper;

    public String[] getUniqueColumnValues(final Sheet sheetToRead, final String columnMarker) {
        final List<String> result = new ArrayList<>();
        final Iterator<Row> rows = sheetToRead.rowIterator();
        final int columnIndex = this.columnReaderHelper.findColumnIndex(rows, columnMarker);
        while(rows.hasNext()) {
            final Row row = rows.next();
            final Cell cell = row.getCell(columnIndex);
            if(this.columnReaderHelper.isStringType(cell) && !result.contains(cell.getStringCellValue())) {
                result.add(cell.getStringCellValue());
            }
        }
        return result.toArray(new String[] {});
    }

    public void setColumnReaderHelper(final ColumnReaderHelper columnReaderHelper) {
        this.columnReaderHelper = columnReaderHelper;
    }

}
