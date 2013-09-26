package oleksii.filonov.writer;

import static oleksii.filonov.TestConstants.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.*;
import oleksii.filonov.reader.*;
import org.apache.poi.openxml4j.exceptions.*;
import org.apache.poi.ss.usermodel.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkbookBuilderTest {

    private static final String BODY_ID_FOUND = "bodyIdFound";
    private static final String PATH_TO_CAMPAIGN_FILE = "pathToCampaignFile";
    private static final int FOUND_BODY_COLUMN_INDEX = 7;
    private static final int FIRST_LINK_COLUMN_INDEX = 8;
    private static final int SECOND_LINK_COLUMN_INDEX = 9;
    private static final String ANOTHER_BODY_ID_FOUND = "anotherFoundBodyId";
    private static final int FOUND_ROW_INDEX = 7;
    private static final int SHIFT_VALUE = 1;
    private static final String CLIENT_SHEET_NAME = "clientSheetName";
    private static final int BODY_ID_COLUMN_INDEX = 1;
    private static final int VIN_LIST_1_ROW_INDEX = 1;
    private static final int VIN_LIST_2_ROW_INDEX = 2;
    private static final int VIN_LIST_3_ROW_INDEX = 3;
    private static final int VIN_LIST_4_ROW_INDEX = 4;
    private static final String LINK_1_BODY_ID_SHEET_NAME = "link1bodyIdSheetName";
    private static final String LINK_2_BODY_ID_SHEET_NAME = "link2bodyIdSheetName";
    private static final String LINK_3_BODY_ID_SHEET_NAME = "link3bodyIdSheetName";
    private static final String LINK_4_BODY_ID_SHEET_NAME = "link4bodyIdSheetName";
    private static final int EXCEL_ROW_OFFSET = 1;

    private ColumnReaderHelper columnReaderHelper;
	private CampaignProcessor campaignProcessor;

	private WorkbookBuilder excelBuilder;
    @Mock
    private Workbook clientWorkbook;
    @Mock
    private CreationHelper creationHelper;
    @Mock
    private CellStyle foundCellStyle;
    @Mock
    private CellStyle linkCellStyle;
    @Mock
    private Cell foundBodyIdCell;
    @Mock
    private Cell notFoundBodyIdCell;
    @Mock
    private Row foundRow;
    @Mock
    private Cell firstLinkToVinCell;
    @Mock
    private Hyperlink firstHyperLink;
    @Mock
    private Cell secondLinkToVin;
    @Mock
    private Hyperlink secondHyperLink;
    @Mock
    private Hyperlink thirdHyperLink;
    @Mock
    private Cell foundAnotherBodyIdCell;
    @Mock
    private Row anotherFoundRow;
    @Mock
    private Cell linkToAnotherVinCell;
    @Mock
    private Sheet clientSheet;
    @Mock
    private Row rowForHyperLink2;
    @Mock
    private Row rowForHyperLink4;
    @Mock
    private Hyperlink fourthHyperLink;
    @Mock
    private Cell fourthLinkToVin;
    @Mock
    private Cell foundBodyIdOnVinList1Cell;
    @Mock
    private Cell foundBodyIdOnVinList2Cell;
    @Mock
    private Cell foundBodyIdOnVinList3Cell;
    @Mock
    private Cell foundBodyIdOnVinList4Cell;
    @Mock
    private Sheet link1bodyIdSheet;
    @Mock
    private Sheet link2bodyIdSheet;
    @Mock
    private Sheet link3bodyIdSheet;
    @Mock
    private Sheet link4bodyIdSheet;


    @Before
	public void setUp() throws IOException, InvalidFormatException {
        if(!Files.exists(TARGET_RESOURCE)) {
            Files.createDirectory(TARGET_RESOURCE);
        }
        excelBuilder = new WorkbookBuilder();
        excelBuilder.setPathToCampaignFile(PATH_TO_CAMPAIGN_FILE);
		columnReaderHelper = new ColumnReaderHelper();
		campaignProcessor = new CampaignProcessor();
		campaignProcessor.setColumnReaderHelper(columnReaderHelper);
        populateMocks();
	}

    private void populateMocks() throws IOException, InvalidFormatException {
        when(clientWorkbook.getCreationHelper()).thenReturn(creationHelper);
        when(clientWorkbook.createCellStyle() ).thenReturn(foundCellStyle).thenReturn(linkCellStyle);
        when(clientWorkbook.getSheetAt(0)).thenReturn(clientSheet);
        when(clientSheet.getSheetName()).thenReturn(CLIENT_SHEET_NAME);
        excelBuilder.useWorkbook(clientWorkbook);
        mockFoundCellWithThreeLinksBehaviour();
        mockAnotherFoundCellBehaviour();
        when(creationHelper.createHyperlink(Hyperlink.LINK_FILE))
                .thenReturn(firstHyperLink)
                .thenReturn(secondHyperLink)
                .thenReturn(fourthHyperLink)
                .thenReturn(thirdHyperLink);
        mockFoundBodyIdCellsOnVinLists();
        when(notFoundBodyIdCell.getStringCellValue()).thenReturn("bodyIdNotFound");
    }

    private void mockFoundBodyIdCellsOnVinLists() {
        mockBodyIdCell(foundBodyIdOnVinList1Cell, VIN_LIST_1_ROW_INDEX, link1bodyIdSheet);
        when(link1bodyIdSheet.getSheetName()).thenReturn(LINK_1_BODY_ID_SHEET_NAME);
        mockBodyIdCell(foundBodyIdOnVinList2Cell, VIN_LIST_2_ROW_INDEX, link2bodyIdSheet);
        when(link2bodyIdSheet.getSheetName()).thenReturn(LINK_2_BODY_ID_SHEET_NAME);
        mockBodyIdCell(foundBodyIdOnVinList3Cell, VIN_LIST_3_ROW_INDEX, link3bodyIdSheet);
        when(link3bodyIdSheet.getSheetName()).thenReturn(LINK_3_BODY_ID_SHEET_NAME);
        mockBodyIdCell(foundBodyIdOnVinList4Cell, VIN_LIST_4_ROW_INDEX, link4bodyIdSheet);
        when(link4bodyIdSheet.getSheetName()).thenReturn(LINK_4_BODY_ID_SHEET_NAME);
    }

    private void mockBodyIdCell(final Cell bodyIdCell, int bodyIdRowIndex, Sheet bodyIdSheet) {
        when(bodyIdCell.getSheet()).thenReturn(bodyIdSheet);
        when(bodyIdCell.getColumnIndex()).thenReturn(BODY_ID_COLUMN_INDEX);
        when(bodyIdCell.getRowIndex()).thenReturn(bodyIdRowIndex);
    }

    private void mockFoundCellWithThreeLinksBehaviour() {
        when(foundBodyIdCell.getStringCellValue()).thenReturn(BODY_ID_FOUND);
        when(foundBodyIdCell.getRow()).thenReturn(foundRow);
        when(foundBodyIdCell.getColumnIndex()).thenReturn(FOUND_BODY_COLUMN_INDEX);
        when(foundRow.getSheet()).thenReturn(clientSheet);
        when(foundRow.getRowNum()).thenReturn(FOUND_ROW_INDEX);
        when(foundRow.createCell(FIRST_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING)).thenReturn(firstLinkToVinCell);
        when(clientSheet.createRow(FOUND_ROW_INDEX+1)).thenReturn(rowForHyperLink2);
        when(clientSheet.createRow(FOUND_ROW_INDEX+2)).thenReturn(rowForHyperLink4);
        when(rowForHyperLink2.createCell(FIRST_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING)).thenReturn(secondLinkToVin);
        when(rowForHyperLink4.createCell(FIRST_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING)).thenReturn(fourthLinkToVin);
    }

    private void mockAnotherFoundCellBehaviour() {
        when(foundAnotherBodyIdCell.getStringCellValue()).thenReturn(ANOTHER_BODY_ID_FOUND);
        when(foundAnotherBodyIdCell.getRow()).thenReturn(anotherFoundRow);
        when(foundAnotherBodyIdCell.getColumnIndex()).thenReturn(FOUND_BODY_COLUMN_INDEX);
        when(anotherFoundRow.getSheet()).thenReturn(clientSheet);
        when(anotherFoundRow.createCell(FIRST_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING)).thenReturn(linkToAnotherVinCell);
    }

    @Test
	public void formResultFileWithLinks() throws IOException, InvalidFormatException {
		final Cell[] bodyIds = new Cell[] {foundBodyIdCell, notFoundBodyIdCell, foundAnotherBodyIdCell};
		final ListMultimap<String, Cell> bodyIdLinks = LinkedListMultimap.create();
        bodyIdLinks.put(BODY_ID_FOUND, foundBodyIdOnVinList1Cell);
        bodyIdLinks.put(BODY_ID_FOUND, foundBodyIdOnVinList2Cell);
        bodyIdLinks.put(ANOTHER_BODY_ID_FOUND, foundBodyIdOnVinList3Cell);
        bodyIdLinks.put(BODY_ID_FOUND, foundBodyIdOnVinList4Cell);
        Map<String, String> bodyIdDescriptionMap = new HashMap<>();
		excelBuilder.assignTasks(bodyIds, bodyIdDescriptionMap, bodyIdLinks);
        verifyCreatedDocument();
	}

    private void verifyCreatedDocument() throws IOException {
        verify(firstLinkToVinCell).setCellValue(LINK_1_BODY_ID_SHEET_NAME);
        verify(firstHyperLink).setAddress(PATH_TO_CAMPAIGN_FILE + "#'" + LINK_1_BODY_ID_SHEET_NAME + "'!B" + (VIN_LIST_1_ROW_INDEX + EXCEL_ROW_OFFSET));
        verify(clientSheet).shiftRows(eq(FOUND_ROW_INDEX + 1), anyInt(), eq(SHIFT_VALUE));
        verify(secondLinkToVin).setCellValue(LINK_2_BODY_ID_SHEET_NAME);
        verify(secondHyperLink).setAddress(PATH_TO_CAMPAIGN_FILE + "#'" + LINK_2_BODY_ID_SHEET_NAME + "'!B" + (VIN_LIST_2_ROW_INDEX + EXCEL_ROW_OFFSET));
        verify(clientSheet).shiftRows(eq(FOUND_ROW_INDEX + 2), anyInt(), eq(SHIFT_VALUE));
        verify(fourthLinkToVin).setCellValue(LINK_4_BODY_ID_SHEET_NAME);
        verify(fourthHyperLink).setAddress(PATH_TO_CAMPAIGN_FILE + "#'" + LINK_4_BODY_ID_SHEET_NAME + "'!B" + (VIN_LIST_4_ROW_INDEX + EXCEL_ROW_OFFSET));
        verify(notFoundBodyIdCell, never()).getRow();
        verify(linkToAnotherVinCell).setCellValue(LINK_3_BODY_ID_SHEET_NAME);
        verify(thirdHyperLink).setAddress(PATH_TO_CAMPAIGN_FILE + "#'" + LINK_3_BODY_ID_SHEET_NAME + "'!B" + (VIN_LIST_3_ROW_INDEX + EXCEL_ROW_OFFSET));
        verify(foundRow, never()).createCell(SECOND_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING);
        verify(anotherFoundRow, never()).createCell(SECOND_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING);
        excelBuilder.saveToFile(LINKED_RESULT_PATH.toFile());
        verify(clientWorkbook).write(any(FileOutputStream.class));
    }

}
