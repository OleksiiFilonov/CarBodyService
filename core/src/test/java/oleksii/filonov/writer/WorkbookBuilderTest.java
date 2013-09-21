package oleksii.filonov.writer;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import oleksii.filonov.reader.CampaignProcessor;
import oleksii.filonov.reader.ColumnReaderHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import static oleksii.filonov.TestConstants.LINKED_RESULT_PATH;
import static oleksii.filonov.TestConstants.TARGET_RESOURCE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkbookBuilderTest {

    private static final String BODY_ID_FOUND = "bodyIdFound";
    private static final String LINK_1 = "Link1";
    private static final String LINK_2 = "Link2";
    private static final String PATH_TO_CAMPAIGN_FILE = "pathToCampaignFile";
    private static final int FOUN_BODY_COLUMN_INDEX = 7;
    private static final int FIRST_LINK_COLUMN_INDEX = 8;
    private static final int SECOND_LINK_COLUMN_INDEX = 9;
    private static final String ANOTHER_BODY_ID_FOUND = "anotherFoundBodyId";
    private static final String LINK_3 = "Link3";
    private static final int FOUND_ROW_INDEX = 7;
    private static final int ROW_INDEX_FOR_SECOND_LINK = FOUND_ROW_INDEX + 1;
    private static final int SHIFT_VALUE = 1;
    private static final String LINK_4 = "Link4";

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
        excelBuilder.useWorkbook(clientWorkbook);
        mockFoundCellWithThreeLinksBehaviour();
        mockAnotherFoundCellBehaviour();
        when(creationHelper.createHyperlink(Hyperlink.LINK_FILE))
                .thenReturn(firstHyperLink)
                .thenReturn(secondHyperLink)
                .thenReturn(fourthHyperLink)
                .thenReturn(thirdHyperLink);
        when(notFoundBodyIdCell.getStringCellValue()).thenReturn("bodyIdNotFound");
    }

    private void mockFoundCellWithThreeLinksBehaviour() {
        when(foundBodyIdCell.getStringCellValue()).thenReturn(BODY_ID_FOUND);
        when(foundBodyIdCell.getRow()).thenReturn(foundRow);
        when(foundBodyIdCell.getColumnIndex()).thenReturn(FOUN_BODY_COLUMN_INDEX);
        when(foundRow.getSheet()).thenReturn(clientSheet);
        when(foundRow.getRowNum()).thenReturn(FOUND_ROW_INDEX);
        when(foundRow.createCell(FIRST_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING)).thenReturn(firstLinkToVinCell);
        when(clientSheet.createRow(FOUND_ROW_INDEX)).thenReturn(rowForHyperLink2);
        when(clientSheet.createRow(ROW_INDEX_FOR_SECOND_LINK)).thenReturn(rowForHyperLink4);
        when(rowForHyperLink2.createCell(FIRST_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING)).thenReturn(secondLinkToVin);
        when(rowForHyperLink4.createCell(FIRST_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING)).thenReturn(fourthLinkToVin);
    }

    private void mockAnotherFoundCellBehaviour() {
        when(foundAnotherBodyIdCell.getStringCellValue()).thenReturn(ANOTHER_BODY_ID_FOUND);
        when(foundAnotherBodyIdCell.getRow()).thenReturn(anotherFoundRow);
        when(foundAnotherBodyIdCell.getColumnIndex()).thenReturn(FOUN_BODY_COLUMN_INDEX);
        when(anotherFoundRow.getSheet()).thenReturn(clientSheet);
        when(anotherFoundRow.createCell(FIRST_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING)).thenReturn(linkToAnotherVinCell);
    }

    @Test
	public void formResultFileWithLinks() throws IOException, InvalidFormatException {
		final Cell[] bodyIds = new Cell[] {foundBodyIdCell, notFoundBodyIdCell, foundAnotherBodyIdCell};
		final ListMultimap<String, String> bodyIdLinks = LinkedListMultimap.create();
        bodyIdLinks.put(BODY_ID_FOUND, LINK_1);
        bodyIdLinks.put(BODY_ID_FOUND, LINK_2);
        bodyIdLinks.put(ANOTHER_BODY_ID_FOUND, LINK_3);
        bodyIdLinks.put(BODY_ID_FOUND, LINK_4);
		excelBuilder.assignTasks(bodyIds, bodyIdLinks);
        verifyCreatedDocument();
	}

    private void verifyCreatedDocument() throws IOException {
        verify(firstLinkToVinCell).setCellValue(LINK_1);
        verify(firstHyperLink).setAddress(PATH_TO_CAMPAIGN_FILE + "#" + LINK_1);
        verify(clientSheet).shiftRows(eq(FOUND_ROW_INDEX), anyInt(), eq(SHIFT_VALUE));
        verify(secondLinkToVin).setCellValue(LINK_2);
        verify(secondHyperLink).setAddress(PATH_TO_CAMPAIGN_FILE + "#" + LINK_2);
        verify(clientSheet).shiftRows(eq(ROW_INDEX_FOR_SECOND_LINK), anyInt(), eq(SHIFT_VALUE));
        verify(fourthLinkToVin).setCellValue(LINK_4);
        verify(fourthHyperLink).setAddress(PATH_TO_CAMPAIGN_FILE + "#" + LINK_4);
        verify(notFoundBodyIdCell, never()).getRow();
        verify(linkToAnotherVinCell).setCellValue(LINK_3);
        verify(thirdHyperLink).setAddress(PATH_TO_CAMPAIGN_FILE + "#" + LINK_3);
        verify(foundRow, never()).createCell(SECOND_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING);
        verify(anotherFoundRow, never()).createCell(SECOND_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING);
        excelBuilder.saveToFile(LINKED_RESULT_PATH.toFile());
        verify(clientWorkbook).write(any(FileOutputStream.class));
    }

}
