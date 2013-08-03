package oleksii.filonov.writer;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import oleksii.filonov.reader.ReadDataException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XSSFBuilder implements DataBuilder {

	private static String BODY_ID_MARKER = "Номер кузова";

	private XSSFWorkbook workBook;

	private XSSFSheet mainSheet;

	private Map<String, Cell> bodyIdCellMap;

	@Override
	public void createDocument() {
		workBook = new XSSFWorkbook();
	}

	@Override
	public void createMainSheetWithName(final String sheetName) {
		mainSheet = workBook.createSheet(sheetName);
	}

	@Override
	public void writeBodyIdsColumnToMainPage(final String[] bodyIds) {
		final Row bodyIdRow = mainSheet.createRow(0);
		final Cell titleCell = bodyIdRow.createCell(0, CELL_TYPE_STRING);
		titleCell.setCellValue(BODY_ID_MARKER);
		final Map<String, Cell> bodyIdCellMap = new HashMap<>(bodyIds.length);
		for (int rowIndex = 1; rowIndex <= bodyIds.length; rowIndex++) {
			final String bodyId = bodyIds[rowIndex - 1];
			final Cell bodyIdCell = mainSheet.createRow(rowIndex).createCell(0, CELL_TYPE_STRING);
			bodyIdCell.setCellValue(bodyId);
			bodyIdCellMap.put(bodyId, bodyIdCell);
		}

		this.bodyIdCellMap = Collections.unmodifiableMap(bodyIdCellMap);

	}

	@Override
	public void saveToFile(final File resultFile) throws IOException {
		final FileOutputStream recordStream = new FileOutputStream(resultFile);
		workBook.write(recordStream);
		recordStream.close();
	}

	@Override
	public void linkExistingBodyIds(final File campaignSource) throws ReadDataException {
		try {
			final Workbook clientWB = WorkbookFactory.create(campaignSource);
		} catch (InvalidFormatException | IOException e) {
			throw new ReadDataException(e);
		}

	}

	@Override
	public Map<String, Cell> getBodyIdCellMap() {
		return bodyIdCellMap;
	}

}
