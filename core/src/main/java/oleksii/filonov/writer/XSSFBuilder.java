package oleksii.filonov.writer;

import com.google.common.collect.ListMultimap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

public class XSSFBuilder implements DataBuilder {

	private static final int DEFAULT_COLUMN_SIZE = 17;
	private static final int BODY_ID_COLUMN_INDEX = 0;

	private static final XSSFColor RED = new XSSFColor(new java.awt.Color(255, 0, 0));
	private static final XSSFColor YELLOW = new XSSFColor(new java.awt.Color(255, 255, 0));
	private static final XSSFColor GREEN = new XSSFColor(new java.awt.Color(51, 255, 51));

	private Workbook workBook;
	private CellStyle notFoundCellStyle;
	private CellStyle foundCellStyle;
    private CellStyle linkCellStyle;
	private CreationHelper creationHelper;

	private Sheet mainSheet;

	private Cell[] bodyIdCells;

    @Override
	public void createDocument(File campaignFile) throws IOException, InvalidFormatException {
		workBook = WorkbookFactory.create(campaignFile);
		creationHelper = workBook.getCreationHelper();
		initNotFoundCellStyle();
		initFoundCellStyle();
        initLinkCellStyle();
	}

	@Override
	public void createLinkedSheetWithName(final String sheetName) {
		mainSheet = workBook.createSheet(sheetName);
	}

	@Override
	public void writeBodyIdsColumnToLinkedSheet(final String bodyColumnMarker, final String[] bodyIds) {
		final Row bodyIdRow = mainSheet.createRow(0);
		mainSheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
		final Cell titleCell = bodyIdRow.createCell(BODY_ID_COLUMN_INDEX, CELL_TYPE_STRING);
		titleCell.setCellValue(bodyColumnMarker);
		bodyIdCells = new Cell[bodyIds.length];
		for (int rowIndex = 1; rowIndex <= bodyIds.length; rowIndex++) {
			final String bodyId = bodyIds[rowIndex - 1];
			final Cell bodyIdCell = mainSheet.createRow(rowIndex).createCell(0, CELL_TYPE_STRING);
			bodyIdCell.setCellStyle(notFoundCellStyle);
			bodyIdCell.setCellValue(bodyId);
			bodyIdCells[rowIndex - 1] = bodyIdCell;
		}
	}

	@Override
	public void saveToFile(final File fileToSave) throws IOException {
		final FileOutputStream recordStream = new FileOutputStream(fileToSave);
		workBook.write(recordStream);
		recordStream.close();
	}

	@Override
	public Cell[] getBodyIdCells() {
		return bodyIdCells;
	}

	@Override
	public void linkExistingBodyIds(final ListMultimap<String, String> linkedBodies, final String pathToCampaignFile) {
		for (final Cell bodyIdCell : bodyIdCells) {
			final List<String> links = linkedBodies.get(bodyIdCell.getStringCellValue());
			if (!links.isEmpty()) {
				bodyIdCell.setCellStyle(foundCellStyle);
				final Row bodyIdRow = bodyIdCell.getRow();
				writeLinksForFoundCell(pathToCampaignFile, links, bodyIdRow);
				int cellIndex = 1;
				while (bodyIdRow.getCell(cellIndex) != null) {
					++cellIndex;
				}
			}
		}
	}

	private void writeLinksForFoundCell(final String pathToCampaignFile, final List<String> links, final Row bodyIdRow) {
		for (int i = 0; i < links.size(); i++) {
			final Cell linkToVin = bodyIdRow.createCell(i + 1, Cell.CELL_TYPE_STRING);
			linkToVin.setCellValue(links.get(i));
			final Hyperlink cellHyperlink = creationHelper.createHyperlink(Hyperlink.LINK_FILE);
			cellHyperlink.setAddress(pathToCampaignFile + "#" + links.get(i));
			cellHyperlink.setLabel(links.get(i));
			linkToVin.setHyperlink(cellHyperlink);
            linkToVin.setCellStyle(linkCellStyle);
		}
	}

	private void initLinkCellStyle() {
		linkCellStyle = workBook.createCellStyle();
        linkCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        linkCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	}

    private void initFoundCellStyle() {
		foundCellStyle = workBook.createCellStyle();
        foundCellStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
		foundCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	}

	private void initNotFoundCellStyle() {
		notFoundCellStyle = workBook.createCellStyle();
		notFoundCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		notFoundCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	}
}
