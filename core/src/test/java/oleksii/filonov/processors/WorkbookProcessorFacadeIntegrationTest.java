package oleksii.filonov.processors;

import static oleksii.filonov.TestConstants.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import oleksii.filonov.readers.ColumnReaderHelper;

import oleksii.filonov.settings.PropertiesLoader;
import oleksii.filonov.settings.Settings;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;

public class WorkbookProcessorFacadeIntegrationTest {

	private static final String DESC_10C116 = "ЗАМЕНА КЛЕММ КАБЕЛЯ АКБ(TSB No. In English : HCE11-11-P180-RBMDENFDLMTQCMHRNF)";
    private static final String DESC_10C150 = "УСТРАНЕНИЕ ШУМА ОТ ПЕРЕДНЕГО СИДЕНЬЯ С РУЧНОЙ РЕГУЛИРОВКОЙ(TSB No. In English : HCE11-91-P560-RBMDVF)";
    private static final int VIN_LIST_COLUMN_INDEX = 5;

    private DataProcessorFacade dataProcessorFacade;

	private ColumnReaderHelper columnReaderHelper;

	@Before
	public void setUp() throws IOException {
		final WorkbookProcessorFacade workbookProcessorFacade = new WorkbookProcessorFacade();
		dataProcessorFacade = workbookProcessorFacade;
		columnReaderHelper = new ColumnReaderHelper();
	}

	@Test
	public void createResultFileWithDefaultSettings() throws InvalidFormatException, IOException {
        final Settings settings = PropertiesLoader.loadDefaultProperties();
        final FilesToProcess filesToProcess = new FilesToProcess();
        filesToProcess.setClientsFile(CLIENT_FILE2);
        filesToProcess.setCampaignFile(CAMPAIGN_FILE2);
        filesToProcess.setResultFile(LINKED_RESULT_PATH2.toFile());
		dataProcessorFacade.createResultFile(settings, filesToProcess);

        verifyResultsWhenDefaultSettingsLoaded();
	}

    private void verifyResultsWhenDefaultSettingsLoaded() throws IOException, InvalidFormatException {
        final Iterator<Row> clientIterator = getClientsRowIterator(LINKED_RESULT_PATH2.toFile());
        final Cell cell_KMHSH81XDBU763142 = columnReaderHelper.findCell(clientIterator, "KMHSH81XDBU763142");
        final Cell cell_KMHSH81XDBU763142_vinLIst = columnReaderHelper.findCellFrom(cell_KMHSH81XDBU763142,
                clientIterator, "10C116");
        assertThat(cell_KMHSH81XDBU763142_vinLIst.getColumnIndex(), equalTo(VIN_LIST_COLUMN_INDEX));
        final Cell cell_KMHSH81XDBU763142_description = columnReaderHelper.findCellFrom(cell_KMHSH81XDBU763142_vinLIst,
                clientIterator, DESC_10C116);
        assertThat(cell_KMHSH81XDBU763142_description.getRowIndex(), equalTo(cell_KMHSH81XDBU763142.getRowIndex()));
    }

    private Iterator<Row> getClientsRowIterator(final File file) throws IOException, InvalidFormatException {
        final Workbook workbookForVerification = WorkbookFactory.create(file);
        final Sheet verifyClientSheet = workbookForVerification.getSheetAt(0);
        return verifyClientSheet.rowIterator();
    }

    @Test
    public void createResultFileWithReloadedSettings() throws IOException, InvalidFormatException {
        final Settings settings = PropertiesLoader.loadPropertiesFrom(ALTERNATIVE_SETTINGS_PATH);
        final FilesToProcess filesToProcess = new FilesToProcess();
        filesToProcess.setClientsFile(CLIENT_FILE);
        filesToProcess.setCampaignFile(CAMPAIGN_FILE);
        filesToProcess.setResultFile(LINKED_RESULT_PATH.toFile());
        dataProcessorFacade.createResultFile(settings, filesToProcess);

        verifyResultsWhenExplicitlySetSettingsLoaded();
    }

    private void verifyResultsWhenExplicitlySetSettingsLoaded() throws IOException, InvalidFormatException {
        final Iterator<Row> clientIterator = getClientsRowIterator(LINKED_RESULT_PATH.toFile());
        final Cell cell_10c150_firstOccurrence = columnReaderHelper.findCell(clientIterator, "10C150");
        assertThat(cell_10c150_firstOccurrence.getColumnIndex(), equalTo(6));
        assertThat(columnReaderHelper.findCellFrom(cell_10c150_firstOccurrence, clientIterator, DESC_10C150)
                .getColumnIndex(), equalTo(7));
    }
}
