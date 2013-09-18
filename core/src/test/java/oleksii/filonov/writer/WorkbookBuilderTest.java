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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkbookBuilderTest {

    private static final String LINKED_SHEET_NAME = "Body";
	private static final String BODY_ID_MARKER = "Номер кузова";
	private static final String VIN_MARKER = "VIN";

    private static final String REAL_BODY_ID_FIRST_SHEET_ROW_ONE = "KMHEC41BABA263951";
    private static final String BODY_ID_FOUND = "bodyIdFound";
    private static final String LINK_1 = "Link1";
    private static final String LINK_2 = "Link2";
    private static final String PATH_TO_CAMPAIGN_FILE = "pathToCampaignFile";
    private static final int FOUN_BODY_COLUMN_INDEX = 7;
    private static final int FIRST_LINK_COLUMN_INDEX = 8;
    private static final int SECOND_LINK_COLUMN_INDEX = 9;

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
        when(foundBodyIdCell.getStringCellValue()).thenReturn(BODY_ID_FOUND);
        when(foundBodyIdCell.getRow()).thenReturn(foundRow);
        when(foundBodyIdCell.getColumnIndex()).thenReturn(FOUN_BODY_COLUMN_INDEX);
        when(foundRow.createCell(FIRST_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING)).thenReturn(firstLinkToVinCell);
        when(foundRow.createCell(SECOND_LINK_COLUMN_INDEX, Cell.CELL_TYPE_STRING)).thenReturn(secondLinkToVin);
        when(creationHelper.createHyperlink(Hyperlink.LINK_FILE)).thenReturn(firstHyperLink).thenReturn(secondHyperLink);
        when(notFoundBodyIdCell.getStringCellValue()).thenReturn("bodyIdNotFound");
    }

    @Test
	public void formResultFileWithLinks() throws IOException, InvalidFormatException {
		final Cell[] bodyIds = new Cell[] {foundBodyIdCell, notFoundBodyIdCell};
		final ListMultimap<String, String> bodyIdLinks = LinkedListMultimap.create();
        bodyIdLinks.put(BODY_ID_FOUND, LINK_1);
        bodyIdLinks.put(BODY_ID_FOUND, LINK_2);
		excelBuilder.assignTasks(bodyIds, bodyIdLinks);
        verify(firstLinkToVinCell).setCellValue(LINK_1);
        verify(secondLinkToVin).setCellValue(LINK_2);
        verify(firstHyperLink).setAddress(PATH_TO_CAMPAIGN_FILE + "#" + LINK_1);
        verify(secondHyperLink).setAddress(PATH_TO_CAMPAIGN_FILE + "#" + LINK_2);
		excelBuilder.saveToFile(LINKED_RESULT_PATH.toFile());
        verify(clientWorkbook).write(any(FileOutputStream.class));
	}

}
