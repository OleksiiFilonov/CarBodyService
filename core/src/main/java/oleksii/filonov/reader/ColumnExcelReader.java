package oleksii.filonov.reader;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ColumnExcelReader implements ColumnReader {

	@Override
	public String[] getUniqueColumnValues(final File fileToRead, final String columnMarker) {
		try {
			final List<String> result = new ArrayList<>();
			final Workbook clientWB = WorkbookFactory.create(fileToRead);
			final Sheet firstSheet = clientWB.getSheetAt(0);
			final Iterator<Row> rows = firstSheet.rowIterator();
			final int columnIndex = findColumn(rows, columnMarker);
			while (rows.hasNext()) {
				final Row row = rows.next();
				final Cell cell = row.getCell(columnIndex);
				// row.getCell(columnIndex + 1, Row.RETURN_NULL_AND_BLANK);
				if (cell.getCellType() == Cell.CELL_TYPE_STRING && !result.contains(cell.getStringCellValue())) {
					result.add(cell.getStringCellValue());
				}
			}
			return result.toArray(new String[] {});
		} catch (InvalidFormatException | IOException exc) {
			throw new ReadDataException(exc);
		}
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
