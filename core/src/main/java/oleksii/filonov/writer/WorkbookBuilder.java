package oleksii.filonov.writer;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;
import org.apache.poi.openxml4j.exceptions.*;
import org.apache.poi.ss.usermodel.*;

public class WorkbookBuilder implements DataBuilder {

    private static final int SHIFT_ROW_OFFSET = 1;
    private static final int OFFSET = 1;
    private static final char[] COLUMN_INDEXES = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'Y', 'V', 'W', 'X', 'Y', 'Z' };
    private static final int EXCEL_ROW_OFFSET = 1;

    private Workbook clientWorkbook;
	private CellStyle foundCellStyle;
    private CellStyle linkCellStyle;
	private CreationHelper creationHelper;
    private String pathToCampaignFile;

    @Override
	public void useWorkbook(Workbook clientWorkbook) throws IOException, InvalidFormatException {
		this.clientWorkbook = clientWorkbook;
		creationHelper = this.clientWorkbook.getCreationHelper();
		initFoundCellStyle();
        initLinkCellStyle();
	}

	@Override
	public void saveToFile(final File fileToSave) throws IOException {
        clientWorkbook.getSheetAt(0).autoSizeColumn(6);
		final FileOutputStream recordStream = new FileOutputStream(fileToSave);
		clientWorkbook.write(recordStream);
		recordStream.close();
	}

    @Override
    public void setPathToCampaignFile(String pathToCampaignFile) {
        this.pathToCampaignFile = pathToCampaignFile;
    }

    @Override
	public void assignTasks(final Cell[] bodyIdCells, final Map<String, String> bodyIdDescriptionMap, final ListMultimap<String, Cell> linksToBodies) {
		for (final Cell bodyIdCell : bodyIdCells) {
			final List<Cell> vinLinks = linksToBodies.get(bodyIdCell.getStringCellValue());
			if (!vinLinks.isEmpty()) {
                bodyIdCell.setCellStyle(foundCellStyle);
                final Row bodyIdRow = bodyIdCell.getRow();
				linkBodyIdsWithVINLists(bodyIdCell.getColumnIndex(), vinLinks, bodyIdRow);
				int cellIndex = 1;
				while (bodyIdRow.getCell(cellIndex) != null) {
					++cellIndex;
				}
			}
		}
	}

	private void linkBodyIdsWithVINLists(final int bodyIdColumnIndex, final List<Cell> foundBodyIdsOnVinLists, final Row bodyIdRow) {

        addVinListToBodyId(bodyIdColumnIndex, foundBodyIdsOnVinLists.get(0), bodyIdRow);

        final Sheet clientSheet = bodyIdRow.getSheet();
        final int bodyIdRowRowNum = bodyIdRow.getRowNum();
        for (int i = 1; i < foundBodyIdsOnVinLists.size(); i++) {
            clientSheet.shiftRows(bodyIdRowRowNum + i, clientSheet.getLastRowNum(), SHIFT_ROW_OFFSET);
            final Row newRow = clientSheet.createRow(bodyIdRowRowNum + i);
            addVinListToBodyId(bodyIdColumnIndex, foundBodyIdsOnVinLists.get(i), newRow);
		}
	}

    private void addVinListToBodyId(final int bodyIdColumnIndex, final Cell foundBodyIdCellOnVinList, final Row bodyIdRow) {
        final Cell linkToVin = bodyIdRow.createCell(bodyIdColumnIndex + 1, Cell.CELL_TYPE_STRING);
        linkToVin.setCellValue(foundBodyIdCellOnVinList.getSheet().getSheetName());
        final Hyperlink cellHyperlink = creationHelper.createHyperlink(Hyperlink.LINK_FILE);
        cellHyperlink.setAddress(pathToCampaignFile + '#' + linkToCell(foundBodyIdCellOnVinList));
        cellHyperlink.setLabel(foundBodyIdCellOnVinList.getSheet().getSheetName());
        linkToVin.setHyperlink(cellHyperlink);
        linkToVin.setCellStyle(linkCellStyle);
    }

    private void initLinkCellStyle() {
		linkCellStyle = clientWorkbook.createCellStyle();
        linkCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        linkCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	}

    private void initFoundCellStyle() {
		foundCellStyle = clientWorkbook.createCellStyle();
        foundCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		foundCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	}

    private String linkToCell(final Cell bodyIdCellOnVinList) {
        return "'" + bodyIdCellOnVinList.getSheet().getSheetName() + "'!" + COLUMN_INDEXES[bodyIdCellOnVinList.getColumnIndex()]
                + (bodyIdCellOnVinList.getRowIndex() + EXCEL_ROW_OFFSET);
    }

}
