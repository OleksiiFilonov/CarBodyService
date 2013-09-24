package oleksii.filonov.reader;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ColumnReaderHelper {

	public int findColumnCell(final Iterator<Row> rows, final String lookUpValue) {
		while (rows.hasNext()) {
			final Row row = rows.next();
			final int columnIndex = lookUpColumnId(row, lookUpValue);
			if (columnIndex > -1) {
				return columnIndex;
			}
		}
		throw new ReadDataException(String.format("Колонка \"%s\" не найдена", lookUpValue));
	}

	private int lookUpColumnId(final Row row, final String lookUpValue) {
		for (final Cell cell : row) {
			if (cell.getCellType() == CELL_TYPE_STRING) {
				if (lookUpValue.equalsIgnoreCase(cell.getStringCellValue().trim())) {
					return cell.getColumnIndex();
				}
			}
		}
		return -1;
	}

	public boolean isStringType(final Cell cell) {
		return cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING;
	}

}
