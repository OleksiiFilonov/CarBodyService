package oleksii.filonov.reader;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ColumnExcelReader implements ColumnReader {

	@Override
	public String[] getUniqueColumnValues(final Sheet sheetToRead, final String columnMarker) {
		final List<String> result = new ArrayList<>();
		final Iterator<Row> rows = sheetToRead.rowIterator();
		final int columnIndex = findColumn(rows, columnMarker);
		while (rows.hasNext()) {
			final Row row = rows.next();
			final Cell cell = row.getCell(columnIndex);
			if (isStringType(cell) && !result.contains(cell.getStringCellValue())) {
				result.add(cell.getStringCellValue());
			}
		}
		return result.toArray(new String[] {});
	}

	private boolean isStringType(final Cell cell) {
		return cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING;
	}

	private int findColumn(final Iterator<Row> rows, final String lookUpValue) {
		int columnIndex;
		while (rows.hasNext()) {
			final Row row = rows.next();
			columnIndex = lookUpBodyIdColumn(row, lookUpValue);
			if (columnIndex > -1) {
				return columnIndex;
			}
		}
		throw new ReadDataException(String.format("Колонка \"%s\" не найдена", lookUpValue));
	}

	private int lookUpBodyIdColumn(final Row row, final String lookUpValue) {
		for (final Cell cell : row) {
			if (cell.getCellType() == CELL_TYPE_STRING) {
				if (lookUpValue.equalsIgnoreCase(cell.getStringCellValue().trim())) {
					return cell.getColumnIndex();
				}
			}
		}
		return -1;
	}

}
