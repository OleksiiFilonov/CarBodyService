package oleksii.filonov.reader;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

public class ReadBodyClientIdTest {

	private static String BODY_ID_MARKER = "Номер кузова";

	@Test
	public void testReadFromXLSXClientsFile() throws IOException {
		final InputStream ExcelFileToRead = new FileInputStream(Paths.get(".", "src", "test", "resources",
				"Clients.xls").toFile());
		final HSSFWorkbook clientWB = new HSSFWorkbook(ExcelFileToRead);
		final HSSFSheet firstSheet = clientWB.getSheetAt(0);
		final Iterator<Row> rows = firstSheet.rowIterator();
		final int columnIndex = findBodyIdColumn(rows);
		while (rows.hasNext()) {
			final Row row = rows.next();
			final Cell cell = row.getCell(columnIndex);
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				System.out.println(cell.getStringCellValue());
			}
		}
	}

	private int findBodyIdColumn(final Iterator<Row> rows) {
		int columnIndex;
		while (rows.hasNext()) {
			final Row row = rows.next();
			columnIndex = lookUpBodyIdColumn(row);
			if (columnIndex > -1) {
				return columnIndex;
			}
		}
		throw new RuntimeException("Колонка с номерами кузова не найдена");
	}

	private int lookUpBodyIdColumn(final Row row) {
		for (final Cell cell : row) {
			if (cell.getCellType() == CELL_TYPE_STRING) {
				if (BODY_ID_MARKER.equalsIgnoreCase(cell.getStringCellValue().trim())) {
					return cell.getColumnIndex();
				}
			}
		}
		return -1;
	}
}
