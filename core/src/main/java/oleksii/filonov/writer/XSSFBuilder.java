package oleksii.filonov.writer;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import oleksii.filonov.reader.CampaignProcessor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ListMultimap;

public class XSSFBuilder implements DataBuilder {

	private static final int DEFAULT_COLUMN_SIZE = 17;
	private static final int BODY_ID_COLUMN_INDEX = 0;

	private static final XSSFColor RED = new XSSFColor(new java.awt.Color(255, 0, 0));
	private static final XSSFColor GREEN = new XSSFColor(new java.awt.Color(51, 255, 51));

	private XSSFWorkbook workBook;
	private XSSFCellStyle notFoundCellStyle;
	private XSSFCellStyle foundCellStyle;
	private XSSFCreationHelper createHelper;

	private XSSFSheet mainSheet;

	private Cell[] bodyIdCells;

	private String[] bodyIds;

	private CampaignProcessor campaignProcessor;

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
		foundCellStyle.setFillForegroundColor(GREEN);
		foundCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	}

	private void initNotFoundCellStyle() {
		notFoundCellStyle = workBook.createCellStyle();
		notFoundCellStyle.setFillForegroundColor(RED);
		notFoundCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	}

	@Override
	public void writeBodyIdsColumnToLinkedSheet(final String bodyColumnMarker, final String[] bodyIds) {
		this.bodyIds = bodyIds;
		final Row bodyIdRow = mainSheet.createRow(0);
		mainSheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
		final Cell titleCell = bodyIdRow.createCell(BODY_ID_COLUMN_INDEX, CELL_TYPE_STRING);
		titleCell.setCellValue(bodyColumnMarker);
		bodyIdCells = new Cell[bodyIds.length];
		for (int rowIndex = 1; rowIndex <= bodyIds.length; rowIndex++) {
			final String bodyId = bodyIds[rowIndex - 1];
			final XSSFCell bodyIdCell = mainSheet.createRow(rowIndex).createCell(0, CELL_TYPE_STRING);
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
	public void linkExistingBodyIds(final String vinColumnMarker, final File campaignSource) {
		final ListMultimap<String, String> linkedBodies = campaignProcessor.linkBodyIdWithCampaigns(
				Arrays.copyOf(bodyIds, bodyIds.length), campaignSource, vinColumnMarker);
		for (final Cell bodyIdCell : bodyIdCells) {
			final List<String> links = linkedBodies.get(bodyIdCell.getStringCellValue());
			if (!links.isEmpty()) {
				bodyIdCell.setCellStyle(foundCellStyle);
				final Row bodyIdRow = bodyIdCell.getRow();
				writeLinksForFoundCell(campaignSource, links, bodyIdRow);
				int cellIndex = 1;
				while (bodyIdRow.getCell(cellIndex) != null) {
					++cellIndex;
				}
			}
		}
	}

	private void writeLinksForFoundCell(final File campaignSource, final List<String> links, final Row bodyIdRow) {
		for (int i = 0; i < links.size(); i++) {
			final Cell linkToVin = bodyIdRow.createCell(i + 1, Cell.CELL_TYPE_STRING);
			linkToVin.setCellValue(links.get(i));
			final XSSFHyperlink cellHyperlink = createHelper.createHyperlink(Hyperlink.LINK_FILE);
			cellHyperlink.setAddress(campaignSource.getName() + "#" + links.get(i));
			cellHyperlink.setLabel(links.get(i));
			linkToVin.setHyperlink(cellHyperlink);
		}
	}

	@Override
	public Cell[] getBodyIdCells() {
		return bodyIdCells;
	}

	public void setCampaignProcessor(final CampaignProcessor campaignProcessor) {
		this.campaignProcessor = campaignProcessor;
	}

}
