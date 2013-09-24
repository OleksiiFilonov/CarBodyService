package oleksii.filonov.reader;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ColumnReaderHelper {

	public Cell findCell(final Iterator<Row> rowsIterator, final String lookUpValue) {
		while (rowsIterator.hasNext()) {
			final Row row = rowsIterator.next();
			final Cell foundCell = lookUpColumnId(row, lookUpValue);
			if (foundCell != null) {
				return foundCell;
			}
		}
		throw new ReadDataException(String.format("Колонка \"%s\" не найдена", lookUpValue));
	}

	private Cell lookUpColumnId(final Row row, final String lookUpValue) {
		for (final Cell cell : row) {
			if (cell.getCellType() == CELL_TYPE_STRING) {
				if (lookUpValue.equalsIgnoreCase(cell.getStringCellValue().trim())) {
					return cell;
				}
			}
		}
        return null;
	}

	public boolean isStringType(final Cell cell) {
		return cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING;
	}

}
