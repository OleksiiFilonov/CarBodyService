package oleksii.filonov.readers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ColumnExcelReader {

    private ColumnReaderHelper columnReaderHelper;

    public Cell[] getColumnValues(final Sheet sheetToRead, final String columnMarker) {
        final List<Cell> result = new LinkedList<>();
        final Iterator<Row> rows = sheetToRead.rowIterator();
        final int columnIndex = this.columnReaderHelper.findCell(rows, columnMarker).getColumnIndex();
        while(rows.hasNext()) {
            final Row row = rows.next();
            final Cell cell = row.getCell(columnIndex);
            if(this.columnReaderHelper.isStringType(cell)) {
                result.add(cell);
            }
        }
        return result.toArray(new Cell[] {});
    }

    public void setColumnReaderHelper(final ColumnReaderHelper columnReaderHelper) {
        this.columnReaderHelper = columnReaderHelper;
    }

}
