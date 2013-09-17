package oleksii.filonov.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ColumnExcelReader {

    private ColumnReaderHelper columnReaderHelper;

    public String[] getColumnValues(final Sheet sheetToRead, final String columnMarker) {
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
