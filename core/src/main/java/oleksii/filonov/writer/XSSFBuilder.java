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
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XSSFBuilder implements DataBuilder {

    private static final int DEFAULT_COLUMN_SIZE = 17;
    private static final int OFFSET = 1;
    private static final int BODY_ID_COLUMN_INDEX = 0;
    private static char[] COLUMN_INDEXES = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'Y', 'V', 'W', 'X', 'Y', 'Z' };

    private static final XSSFColor RED = new XSSFColor(new java.awt.Color(255, 0, 0));
    private static final XSSFColor GREEN = new XSSFColor(new java.awt.Color(51, 255, 51));

    private XSSFWorkbook workBook;
    private XSSFCellStyle notFoundCellStyle;
    private XSSFCellStyle foundCellStyle;
    private XSSFCreationHelper createHelper;

    private XSSFSheet mainSheet;

    private Map<String, Cell> bodyIdCellMap;

    private ColumnReaderHelper columnReaderHelper;

    @Override
    public void createDocument() {
        this.workBook = new XSSFWorkbook();
    }

    @Override
    public void createLinkedSheetWithName(final String sheetName) {
        this.mainSheet = this.workBook.createSheet(sheetName);
        initNotFoundCellStyle();
        initFoundCellStyle();
        this.createHelper = this.workBook.getCreationHelper();
    }

    private void initFoundCellStyle() {
        this.foundCellStyle = this.workBook.createCellStyle();
        this.foundCellStyle.setFillForegroundColor(GREEN);
        this.foundCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    private void initNotFoundCellStyle() {
        this.notFoundCellStyle = this.workBook.createCellStyle();
        this.notFoundCellStyle.setFillForegroundColor(RED);
        this.notFoundCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    @Override
    public void writeBodyIdsColumnToLinkedSheet(final String bodyColumnMarker, final String[] bodyIds) {
        final Row bodyIdRow = this.mainSheet.createRow(0);
        this.mainSheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
        final Cell titleCell = bodyIdRow.createCell(BODY_ID_COLUMN_INDEX, CELL_TYPE_STRING);
        titleCell.setCellValue(bodyColumnMarker);
        final Map<String, Cell> bodyIdCellMap = new HashMap<>(bodyIds.length);
        for(int rowIndex = 1; rowIndex <= bodyIds.length; rowIndex++) {
            final String bodyId = bodyIds[rowIndex - 1];
            final XSSFCell bodyIdCell = this.mainSheet.createRow(rowIndex).createCell(0, CELL_TYPE_STRING);
            bodyIdCell.setCellStyle(this.notFoundCellStyle);
            bodyIdCell.setCellValue(bodyId);
            bodyIdCellMap.put(bodyId, bodyIdCell);
        }

        this.bodyIdCellMap = Collections.unmodifiableMap(bodyIdCellMap);

    }

    @Override
    public void saveToFile(final File fileToSave) throws IOException {
        final FileOutputStream recordStream = new FileOutputStream(fileToSave);
        this.workBook.write(recordStream);
        recordStream.close();
    }

    @Override
    public void linkExistingBodyIds(final String vinColumnMarker, final File campaignSource) {
        try {
            final Workbook compaignWB = WorkbookFactory.create(campaignSource);
            final int numbersOfSheet = compaignWB.getNumberOfSheets();
            for(int sheetIndex = 1; sheetIndex < numbersOfSheet; sheetIndex++) {
                final Sheet vinSheet = compaignWB.getSheetAt(sheetIndex);
                final Iterator<Row> vinRows = vinSheet.rowIterator();
                final int vinColumnIndex = this.columnReaderHelper.findColumnIndex(vinRows, vinColumnMarker);
                while(vinRows.hasNext()) {
                    final Row vinRow = vinRows.next();
                    final Cell vinCell = vinRow.getCell(vinColumnIndex);
                    if(this.columnReaderHelper.isStringType(vinCell)) {
                        final Cell bodyIdCell = getBodyIdCellMap().get(vinCell.getStringCellValue());
                        if(bodyIdCell != null) {
                            bodyIdCell.setCellStyle(this.foundCellStyle);
                            final Row bodyIdRow = bodyIdCell.getRow();
                            int cellIndex = 1;
                            while(bodyIdRow.getCell(cellIndex) != null) {
                                ++cellIndex;
                            }
                            final Cell linkToVin = bodyIdRow.createCell(cellIndex, Cell.CELL_TYPE_STRING);
                            linkToVin.setCellValue(linkToCell(vinCell));
                            final XSSFHyperlink cellHyperlink = this.createHelper.createHyperlink(Hyperlink.LINK_FILE);
                            cellHyperlink.setAddress(campaignSource.getName() + "#" + linkToCell(vinCell));
                            cellHyperlink.setLabel(vinSheet.getSheetName());
                            linkToVin.setHyperlink(cellHyperlink);
                        }
                    }
                }
            }
        } catch(InvalidFormatException | IOException e) {
            throw new ReadDataException(e);
        }

    }

    private String linkToCell(final Cell vinCell) {
        return "'" + vinCell.getSheet().getSheetName() + "'!" + COLUMN_INDEXES[vinCell.getColumnIndex()]
                + (vinCell.getRowIndex() + OFFSET);
    }

    @Override
    public Map<String, Cell> getBodyIdCellMap() {
        return this.bodyIdCellMap;
    }

    public void setColumnReaderHelper(final ColumnReaderHelper columnReaderHelper) {
        this.columnReaderHelper = columnReaderHelper;
    }

}
