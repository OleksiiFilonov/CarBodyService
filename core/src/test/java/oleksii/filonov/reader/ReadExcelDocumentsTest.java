package oleksii.filonov.reader;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Ignore;
import org.junit.Test;

public class ReadExcelDocumentsTest {

	private static final String[] RESOURSE_FOLDER = new String[] { "src", "test", "resources" };
	private static final String VIN = "VIN";
	private static String BODY_ID_MARKER = "Номер кузова";

	@Test
	public void testReadFromXLSXClientsFile() throws IOException, InvalidFormatException {
		System.out.println(getBodyIdList());
	}

	private List<String> getBodyIdList() throws FileNotFoundException, IOException, InvalidFormatException {
		final List<String> result = new ArrayList<>();
		final Workbook clientWB = getWorkbook(RESOURSE_FOLDER, "Clients.xls");
		final Sheet firstSheet = clientWB.getSheetAt(0);
		final Iterator<Row> rows = firstSheet.rowIterator();
		final int columnIndex = findColumn(rows, BODY_ID_MARKER);
		while (rows.hasNext()) {
			final Row row = rows.next();
			final Cell cell = row.getCell(columnIndex);
			// row.getCell(columnIndex + 1, Row.RETURN_NULL_AND_BLANK);
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				result.add(cell.getStringCellValue());
			}
		}
		return result;
	}

	private Workbook getWorkbook(final String[] relativePathToFile, final String fileName)
			throws FileNotFoundException, IOException, InvalidFormatException {
		final String[] path = Arrays.copyOf(relativePathToFile, relativePathToFile.length + 1);
		path[relativePathToFile.length] = fileName;
		final InputStream ExcelFileToRead = new FileInputStream(Paths.get(".", path).toFile());
		return WorkbookFactory.create(ExcelFileToRead);
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
		throw new RuntimeException("Колонка \"" + lookUpValue + "\" не найдена");
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

	@Test
	public void fillResultFileWithBodyId() throws FileNotFoundException, InvalidFormatException, IOException {
		final Workbook resultWB = new XSSFWorkbook();
		final Sheet mainSheet = resultWB.createSheet("Body");
		final Row bodyIdRow = mainSheet.createRow(0);
		final Cell titleCell = bodyIdRow.createCell(0, CELL_TYPE_STRING);
		titleCell.setCellValue(BODY_ID_MARKER);
		final List<String> bodyIdList = getBodyIdList();
		for (int rowIndex = 1; rowIndex <= bodyIdList.size(); rowIndex++) {
			mainSheet.createRow(rowIndex).createCell(0, CELL_TYPE_STRING).setCellValue(bodyIdList.get(rowIndex - 1));
		}

		final Path resourceDir = Paths.get(".", RESOURSE_FOLDER);
		final File resultFile = new File(resourceDir.toFile(), "result.xlsx");
		final FileOutputStream recordStream = new FileOutputStream(resultFile);
		resultWB.write(recordStream);
		recordStream.close();
	}

	@Test
	@Ignore
	public void testReadFromCompaign() throws FileNotFoundException, IOException, InvalidFormatException {
		final Workbook compaignWB = getWorkbook(RESOURSE_FOLDER, "Campaign.xlsx");
		final int numbersOfSheet = compaignWB.getNumberOfSheets();
		for (int sheetIndex = 1; sheetIndex < numbersOfSheet; sheetIndex++) {
			System.out.println("----Sheet number " + sheetIndex);
			final Sheet vinSheet = compaignWB.getSheetAt(sheetIndex);
			final Iterator<Row> rows = vinSheet.rowIterator();
			final int columnIndex = findColumn(rows, VIN);
			while (rows.hasNext()) {
				final Row row = rows.next();
				final Cell cell = row.getCell(columnIndex);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					System.out.println(cell.getStringCellValue());
				}
			}
		}
	}

}
