package oleksii.filonov.readers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ColumnExcelReader {

    private ColumnReaderHelper columnReaderHelper;

    public Cell[] extractCellsFromColumn(final String columnMarker, final Sheet sheetToRead) {
        final Iterator<Row> rows = sheetToRead.rowIterator();
        final int columnIndex = this.columnReaderHelper.findCell(rows, columnMarker).getColumnIndex();
        return extractCellsForColumnIndex(columnIndex, rows);
    }

    private Cell[] extractCellsForColumnIndex(final int columnIndex, final Iterator<Row> rows) {
        final List<Cell> result = new LinkedList<>();
        while (rows.hasNext()) {
            fillIndexedCellToExtracted(columnIndex, rows, result);
        }
        return result.toArray(new Cell[result.size()]);
    }

    private void fillIndexedCellToExtracted(final int columnIndex, final Iterator<Row> rows, final List<Cell> result) {
        final Row row = rows.next();
        final Cell cell = row.getCell(columnIndex);
        if (this.columnReaderHelper.isStringType(cell)) {
            result.add(cell);
        }
    }

    public int findDistanceToEndFrom(final String fromCellValue, final Sheet sheetToRead) {
        final Iterator<Row> rows = sheetToRead.rowIterator();
        final Cell fromCell = columnReaderHelper.findCell(rows, fromCellValue);
        return lastColumnIndex(fromCell) - fromCell.getColumnIndex();
    }

    private int lastColumnIndex(final Cell fromCell) {
        return fromCell.getRow().getLastCellNum() - 1;
    }

    public void setColumnReaderHelper(final ColumnReaderHelper columnReaderHelper) {
        this.columnReaderHelper = columnReaderHelper;
    }

}
