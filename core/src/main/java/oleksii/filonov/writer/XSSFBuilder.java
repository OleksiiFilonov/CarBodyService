package oleksii.filonov.writer;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import oleksii.filonov.reader.ColumnReaderHelper;
import oleksii.filonov.reader.ReadDataException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XSSFBuilder implements DataBuilder {

	private static final int OFFSET = 1;

	private static final int BODY_ID_COLUMN_INDEX = 0;

	private XSSFWorkbook workBook;
	private CellStyle notFoundCellStyle;
	private CellStyle foundCellStyle;
	private CreationHelper createHelper;

	private XSSFSheet mainSheet;

	private Map<String, Cell> bodyIdCellMap;

	private ColumnReaderHelper columnReaderHelper;

	@Override
	public void createDocument() {
		workBook = new XSSFWorkbook();
	}

	@Override
	public void createLinkedSheetWithName(final String sheetName) {
		mainSheet = workBook.createSheet(sheetName);
		initNotFoundCellStyle();
		initFoundCellStyle();
		createHelper = workBook.getCreationHelper();
	}

	private void initFoundCellStyle() {
		foundCellStyle = workBook.createCellStyle();
		foundCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
		foundCellStyle.setFillPattern(CellStyle.BIG_SPOTS);
	}

	private void initNotFoundCellStyle() {
		notFoundCellStyle = workBook.createCellStyle();
		notFoundCellStyle.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		notFoundCellStyle.setFillPattern(CellStyle.BIG_SPOTS);
	}

	@Override
	public void writeBodyIdsColumnToLinkedSheet(final String bodyColumnMarker, final String[] bodyIds) {
		final Row bodyIdRow = mainSheet.createRow(0);
		final Cell titleCell = bodyIdRow.createCell(BODY_ID_COLUMN_INDEX, CELL_TYPE_STRING);
		titleCell.setCellValue(bodyColumnMarker);
		final Map<String, Cell> bodyIdCellMap = new HashMap<>(bodyIds.length);
		for (int rowIndex = 1; rowIndex <= bodyIds.length; rowIndex++) {
			final String bodyId = bodyIds[rowIndex - 1];
			final Cell bodyIdCell = mainSheet.createRow(rowIndex).createCell(0, CELL_TYPE_STRING);
			bodyIdCell.setCellStyle(notFoundCellStyle);
			bodyIdCell.setCellValue(bodyId);
			bodyIdCellMap.put(bodyId, bodyIdCell);
		}

		this.bodyIdCellMap = Collections.unmodifiableMap(bodyIdCellMap);

	}

	@Override
	public void saveToFile(final File fileToSave) throws IOException {
		final FileOutputStream recordStream = new FileOutputStream(fileToSave);
		workBook.write(recordStream);
		recordStream.close();
	}

	@Override
	public void linkExistingBodyIds(final String vinColumnMarker, final File campaignSource) {
		try {
			final Workbook compaignWB = WorkbookFactory.create(campaignSource);
			final int numbersOfSheet = compaignWB.getNumberOfSheets();
			for (int sheetIndex = 1; sheetIndex < numbersOfSheet; sheetIndex++) {
				final Sheet vinSheet = compaignWB.getSheetAt(sheetIndex);
				final Iterator<Row> vinRows = vinSheet.rowIterator();
				final int vinColumnIndex = columnReaderHelper.findColumnIndex(vinRows, vinColumnMarker);
				while (vinRows.hasNext()) {
					final Row vinRow = vinRows.next();
					final Cell vinCell = vinRow.getCell(vinColumnIndex);
					if (columnReaderHelper.isStringType(vinCell)) {
						final Cell bodyIdCell = getBodyIdCellMap().get(vinCell.getStringCellValue());
						if (bodyIdCell != null) {
							bodyIdCell.setCellStyle(foundCellStyle);
							final Row bodyIdRow = bodyIdCell.getRow();
							int cellIndex = 1;
							while (bodyIdRow.getCell(cellIndex) != null) {
								++cellIndex;
							}
							final Cell linkToVin = bodyIdRow.createCell(cellIndex, Cell.CELL_TYPE_STRING);
							linkToVin.setCellValue(vinSheet.getSheetName() + "!B" + (vinRow.getRowNum() + OFFSET));
							final Hyperlink cellHyperlink = createHelper.createHyperlink(Hyperlink.LINK_FILE);
							cellHyperlink.setAddress(campaignSource.getAbsolutePath() + "#'" + vinSheet.getSheetName()
									+ "'.B" + (vinCell.getRowIndex() + OFFSET));
							cellHyperlink.setLabel(vinSheet.getSheetName());
							linkToVin.setHyperlink(cellHyperlink);
						}
					}
				}
			}
		} catch (InvalidFormatException | IOException e) {
			throw new ReadDataException(e);
		}

	}

	@Override
	public Map<String, Cell> getBodyIdCellMap() {
		return bodyIdCellMap;
	}

	public void setColumnReaderHelper(final ColumnReaderHelper columnReaderHelper) {
		this.columnReaderHelper = columnReaderHelper;
	}

}
