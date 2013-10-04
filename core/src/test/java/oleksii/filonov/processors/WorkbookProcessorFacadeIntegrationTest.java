package oleksii.filonov.processors;

import static oleksii.filonov.TestConstants.CAMPAIGN_FILE2;
import static oleksii.filonov.TestConstants.CLIENT_FILE2;
import static oleksii.filonov.TestConstants.LINKED_RESULT_PATH2;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Iterator;

import oleksii.filonov.readers.ColumnReaderHelper;

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

	private DataProcessorFacade dataProcessorFacade;

	private ColumnReaderHelper columnReaderHelper;

	@Before
	public void setUp() throws IOException {
		final WorkbookProcessorFacade workbookProcessorFacade = new WorkbookProcessorFacade();
		dataProcessorFacade = workbookProcessorFacade;
		columnReaderHelper = new ColumnReaderHelper();
	}

	@Test
	public void createResultFile() throws InvalidFormatException, IOException {
		dataProcessorFacade.createResultFile(CLIENT_FILE2, CAMPAIGN_FILE2, LINKED_RESULT_PATH2.toFile());

		final Workbook workbookForVerification = WorkbookFactory.create(LINKED_RESULT_PATH2.toFile());
		final Sheet verifyClientSheet = workbookForVerification.getSheetAt(0);
		final Iterator<Row> clientIterator = verifyClientSheet.rowIterator();
		final Cell cell_KMHSH81XDBU763142 = columnReaderHelper.findCell(clientIterator, "KMHSH81XDBU763142");
		final Cell cell_KMHSH81XDBU763142_vinLIst = columnReaderHelper.findCellFrom(cell_KMHSH81XDBU763142,
				clientIterator, "10C116");
		final Cell cell_KMHSH81XDBU763142_description = columnReaderHelper.findCellFrom(cell_KMHSH81XDBU763142_vinLIst,
				clientIterator, DESC_10C116);
		assertThat(cell_KMHSH81XDBU763142_description.getRowIndex(), equalTo(cell_KMHSH81XDBU763142.getRowIndex()));
	}
}
