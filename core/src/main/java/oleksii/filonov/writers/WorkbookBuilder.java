package oleksii.filonov.writers;

import com.google.common.collect.ListMultimap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WorkbookBuilder implements DataBuilder {

    private static final int SHIFT_ROW_OFFSET = 1;
    private static final int VIN_LINK_COLUMN_INDEX_OFFSET = 1;
    private static final int VIN_DESCRIPTION_COLUMN_INDEX_OFFSET = 2;
    private static final char[] COLUMN_INDEXES = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'Y', 'V', 'W', 'X', 'Y', 'Z'};
    private static final int EXCEL_ROW_OFFSET = 1;
    private static final int VIN_LIST_LINK_COL_INDEX = 6;
    private static final int VIN_DESC_COL_INDEX = 7;

    private Workbook clientWorkbook;
    private CellStyle foundCellStyle;
    private CellStyle linkCellStyle;
    private CreationHelper creationHelper;
    private String pathToCampaignFile;
    private Map<String, String> vinListDescriptionMap;
    //distance between vin body id number column and last column
    private int taskOffset = 0;

    @Override
    public void useWorkbook(final Workbook clientWorkbook) {
        this.clientWorkbook = clientWorkbook;
        creationHelper = this.clientWorkbook.getCreationHelper();
        initFoundCellStyle();
        initLinkCellStyle();
    }

    @Override
    public void saveToFile(final File fileToSave) throws IOException {
        clientWorkbook.getSheetAt(0).autoSizeColumn(VIN_LIST_LINK_COL_INDEX);
        clientWorkbook.getSheetAt(0).autoSizeColumn(VIN_DESC_COL_INDEX);
        final FileOutputStream recordStream = new FileOutputStream(fileToSave);
        clientWorkbook.write(recordStream);
        recordStream.close();
    }

    @Override
    public void setPathToCampaignFile(final String pathToCampaignFile) {
        this.pathToCampaignFile = pathToCampaignFile;
    }

    public void setTaskOffset(final int taskOffset) {
        this.taskOffset = taskOffset;
    }

    @Override
    public void assignTasks(final Cell[] bodyIdCells, final ListMultimap<String, Cell> linksToBodies) {
        for (final Cell bodyIdCell : bodyIdCells) {
            final List<Cell> vinLinks = linksToBodies.get(bodyIdCell.getStringCellValue().trim());
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

    private void linkBodyIdsWithVINLists(final int bodyIdColumnIndex, final List<Cell> foundBodyIdsOnVinLists,
                                         final Row bodyIdRow) {

        createLinkToVinListCell(bodyIdColumnIndex, foundBodyIdsOnVinLists.get(0), bodyIdRow);

        final Sheet clientSheet = bodyIdRow.getSheet();
        final int bodyIdRowRowNum = bodyIdRow.getRowNum();
        for (int i = 1; i < foundBodyIdsOnVinLists.size(); i++) {
            clientSheet.shiftRows(bodyIdRowRowNum + i, clientSheet.getLastRowNum(), SHIFT_ROW_OFFSET);
            final Row newRow = clientSheet.createRow(bodyIdRowRowNum + i);
            final Cell linkVinCell = foundBodyIdsOnVinLists.get(i);
            createLinkToVinListCell(bodyIdColumnIndex, linkVinCell, newRow);
        }
    }

    private void createLinkToVinListCell(final int bodyIdColumnIndex, final Cell foundBodyIdCellOnVinList,
                                         final Row bodyIdRow) {
        final Cell linkToVin = bodyIdRow.createCell(bodyIdColumnIndex + taskOffset + VIN_LINK_COLUMN_INDEX_OFFSET, Cell.CELL_TYPE_STRING);
        final String sheetName = foundBodyIdCellOnVinList.getSheet().getSheetName();
        linkToVin.setCellValue(sheetName);
        final Hyperlink cellHyperlink = creationHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
        cellHyperlink.setAddress(pathToCampaignFile + '#' + linkToCell(foundBodyIdCellOnVinList));
        cellHyperlink.setLabel(sheetName);
        linkToVin.setHyperlink(cellHyperlink);
        linkToVin.setCellStyle(linkCellStyle);
        final Cell vinDescriptionCell = bodyIdRow.createCell(bodyIdColumnIndex + taskOffset + VIN_DESCRIPTION_COLUMN_INDEX_OFFSET,
                Cell.CELL_TYPE_STRING);
        vinDescriptionCell.setCellValue(vinListDescriptionMap.get(sheetName));
        vinDescriptionCell.setCellStyle(linkCellStyle);
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
        return "'" + bodyIdCellOnVinList.getSheet().getSheetName() + "'!"
                + COLUMN_INDEXES[bodyIdCellOnVinList.getColumnIndex()]
                + (bodyIdCellOnVinList.getRowIndex() + EXCEL_ROW_OFFSET);
    }

    @Override
    public void setVinListDescriptionMap(final Map<String, String> vinListDescriptionMap) {
        this.vinListDescriptionMap = vinListDescriptionMap;
    }
}
