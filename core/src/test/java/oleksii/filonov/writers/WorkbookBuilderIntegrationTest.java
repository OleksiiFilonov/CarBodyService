package oleksii.filonov.writers;

import static oleksii.filonov.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import oleksii.filonov.readers.CampaignProcessor;
import oleksii.filonov.readers.ColumnExcelReader;
import oleksii.filonov.readers.ColumnReaderHelper;
import oleksii.filonov.readers.VinListProcessor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ListMultimap;

public class WorkbookBuilderIntegrationTest {

    private static final String BODY_ID_MARKER = "Номер кузова";
	private static final String VIN_MARKER = "VIN";
    private static final int VIN_LINK_COLUMN_INDEX = 6;
	private static final int VIN_DESC_COLUMN_INDEX = 7;
	private static final Matcher<Integer> LINK_COL_INDEX = CoreMatchers.equalTo(VIN_LINK_COLUMN_INDEX);
	private static final Matcher<Integer> DESC_COL_INDEX = CoreMatchers.equalTo(VIN_DESC_COLUMN_INDEX);
	private static final String DESC_10C150 = "УСТРАНЕНИЕ ШУМА ОТ ПЕРЕДНЕГО СИДЕНЬЯ С РУЧНОЙ РЕГУЛИРОВКОЙ(TSB No. In English : HCE11-91-P560-RBMDVF)";
	private static final String DESC_10C116 = "ЗАМЕНА КЛЕММ КАБЕЛЯ АКБ(TSB No. In English : HCE11-11-P180-RBMDENFDLMTQCMHRNF)";
	private static final String DESC_10CR07 = "SOLARIS / ACCENT (RBr) ПЛАСТИКОВАЯ ШАЙБА ВЕДУЩЕГО ВАЛА (ОБЕ СТОРОНЫ) СНЯТИЕ";
	private static final String DESC_10CR08 = "SOLARIS / ACCENT (RBr) ЗАМЕНА РЕЙКИ РУЛЕВОГО УПРАВЛЕНИЯ С УСИЛИТЕЛЕМ В СБОРЕ";
    private static final String CAMPAIGN_NUMBER_TITLE = "Номер кампании";
    private static final String CAMPAIGN_DESCRIPTION_TITLE = "Описание";
    private static final int COLUMN_INDEX_DESCRIPTION_CLIENT2 = 6;

    private ColumnReaderHelper columnReaderHelper;
	private ColumnExcelReader columnExcelReader;
	private CampaignProcessor campaignProcessor;
	private VinListProcessor vinListProcessor;

	@Before
	public void setUp() throws InvalidFormatException, IOException {
		columnReaderHelper = new ColumnReaderHelper();
		campaignProcessor = new CampaignProcessor();
		campaignProcessor.setColumnReaderHelper(columnReaderHelper);
		columnExcelReader = new ColumnExcelReader();
		columnExcelReader.setColumnReaderHelper(columnReaderHelper);
		vinListProcessor = new VinListProcessor();
		vinListProcessor.setColumnReaderHelper(columnReaderHelper);
	}

	@Test
	public void addTasksToClientsWithoutOffset() throws IOException, InvalidFormatException {
		final Workbook clientWB = WorkbookFactory.create(CLIENT_FILE);
		final Sheet clientSheet = clientWB.getSheetAt(0);
		final Cell[] bodyIds = columnExcelReader.getColumnValues(clientSheet, BODY_ID_MARKER);
		final DataBuilder excelBuilder = new WorkbookBuilder();
		excelBuilder.useWorkbook(clientWB);
		excelBuilder.setPathToCampaignFile(CAMPAIGN_FILE.getName());
		final ListMultimap<String, Cell> linkedBodyIdWithCampaigns = campaignProcessor.linkBodyIdWithCampaigns(bodyIds,
				CAMPAIGN_FILE, VIN_MARKER);
		final Map<String, String> bodyIdDescriptionMap = vinListProcessor.mapVinListIdToDescription(CAMPAIGN_FILE,
                CAMPAIGN_NUMBER_TITLE, CAMPAIGN_DESCRIPTION_TITLE);
		excelBuilder.setVinListDescriptionMap(bodyIdDescriptionMap);
		excelBuilder.assignTasks(bodyIds, linkedBodyIdWithCampaigns);
		excelBuilder.saveToFile(LINKED_RESULT_PATH.toFile());
		verifyWithoutOffsetResults();
	}

	private void verifyWithoutOffsetResults() throws IOException, InvalidFormatException {
		final Workbook workbookForVerification = WorkbookFactory.create(LINKED_RESULT_PATH.toFile());
		final Sheet verifyClientSheet = workbookForVerification.getSheetAt(0);
		final Iterator<Row> clientIterator = verifyClientSheet.rowIterator();
		// check for cell type
		final Cell cell_10c150_firstOccurrence = columnReaderHelper.findCell(clientIterator, "10C150");
		assertThat(cell_10c150_firstOccurrence.getColumnIndex(), LINK_COL_INDEX);
		assertThat(columnReaderHelper.findCellFrom(cell_10c150_firstOccurrence, clientIterator, DESC_10C150)
				.getColumnIndex(), DESC_COL_INDEX);
		final Cell cell_10C116 = columnReaderHelper.findCell(clientIterator, "10C116");
		assertThat(cell_10C116.getColumnIndex(), LINK_COL_INDEX);
		final Cell descCell_10C116 = columnReaderHelper.findCellFrom(cell_10C116, clientIterator, DESC_10C116);
		assertThat(descCell_10C116.getColumnIndex(), DESC_COL_INDEX);
		final Cell cell_10C150_secondOccurrence = columnReaderHelper.findCellFrom(descCell_10C116, clientIterator,
				"10C150");
		assertThat(cell_10C150_secondOccurrence.getColumnIndex(), LINK_COL_INDEX);
		assertThat(columnReaderHelper.findCellFrom(cell_10C150_secondOccurrence, clientIterator, DESC_10C150)
				.getColumnIndex(), DESC_COL_INDEX);
		final Cell cell_10CR07 = columnReaderHelper.findCell(clientIterator, "10CR07");
		assertThat(cell_10CR07.getColumnIndex(), LINK_COL_INDEX);
		assertThat(columnReaderHelper.findCellFrom(cell_10CR07, clientIterator, DESC_10CR07).getColumnIndex(),
				DESC_COL_INDEX);
		final Cell cell_10CR08 = columnReaderHelper.findCell(clientIterator, "10CR08");
		assertThat(cell_10CR08.getColumnIndex(), LINK_COL_INDEX);
		assertThat(columnReaderHelper.findCellFrom(cell_10CR08, clientIterator, DESC_10CR08).getColumnIndex(),
				DESC_COL_INDEX);
		final Cell cell_20CR22 = columnReaderHelper.findCell(clientIterator, "20CR22");
		assertThat(cell_20CR22.getColumnIndex(), LINK_COL_INDEX);
		assertThat(columnReaderHelper.findCellFrom(cell_20CR22, clientIterator, "Ремонт Бензобака").getColumnIndex(),
				DESC_COL_INDEX);
	}

    @Test
    public void addTasksToClientsWithOffset() throws Exception {
        final Workbook clientWB = WorkbookFactory.create(CLIENT_FILE2);
        final Sheet clientSheet = clientWB.getSheetAt(0);
        final Cell[] bodyIds = columnExcelReader.getColumnValues(clientSheet, "VIN номер автомобиля");
        final DataBuilder excelBuilder = new WorkbookBuilder();
        excelBuilder.useWorkbook(clientWB);
        excelBuilder.setPathToCampaignFile(CAMPAIGN_FILE2.getName());
        final ListMultimap<String, Cell> linkedBodyIdWithCampaigns = campaignProcessor.linkBodyIdWithCampaigns(bodyIds,
                CAMPAIGN_FILE2, VIN_MARKER);
        final Map<String, String> bodyIdDescriptionMap = vinListProcessor.mapVinListIdToDescription(CAMPAIGN_FILE2,
                CAMPAIGN_NUMBER_TITLE, CAMPAIGN_DESCRIPTION_TITLE);
        excelBuilder.setVinListDescriptionMap(bodyIdDescriptionMap);
        excelBuilder.assignTasks(bodyIds, linkedBodyIdWithCampaigns);
        excelBuilder.saveToFile(LINKED_RESULT_PATH2.toFile());

        final Workbook workbookForVerification = WorkbookFactory.create(LINKED_RESULT_PATH2.toFile());
        final Sheet verifyClientSheet = workbookForVerification.getSheetAt(0);
        final Iterator<Row> clientIterator = verifyClientSheet.rowIterator();
        final Cell cell_KMHSH81XDBU763142_Desc = columnReaderHelper.findCell(clientIterator, "ЗАМЕНА КЛЕММ КАБЕЛЯ АКБ(TSB No. In English : HCE11-11-P180-RBMDENFDLMTQCMHRNF)");
        assertEquals(COLUMN_INDEX_DESCRIPTION_CLIENT2, cell_KMHSH81XDBU763142_Desc.getColumnIndex());
    }

    @Test
	@Ignore
	public void printResultFile() throws IOException, InvalidFormatException {
		final Workbook workbookForVerification = WorkbookFactory.create(LINKED_RESULT_PATH.toFile());
		final Sheet verifyClientSheet = workbookForVerification.getSheetAt(0);
		final Iterator<Row> clientIterator = verifyClientSheet.rowIterator();
		while (clientIterator.hasNext()) {
			final Row row = clientIterator.next();
			final Cell cell = row.getCell(6);
			if (cell != null) {
				final Hyperlink hyperlink = cell.getHyperlink();
				if (hyperlink != null) {
					System.out.println(hyperlink.getAddress());
				}
			}
		}
	}

}
