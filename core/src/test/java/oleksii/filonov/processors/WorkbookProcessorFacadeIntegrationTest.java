package oleksii.filonov.processors;

import static oleksii.filonov.TestConstants.CAMPAIGN_FILE;
import static oleksii.filonov.TestConstants.CLIENT_FILE;
import static oleksii.filonov.TestConstants.LINKED_RESULT_PATH;

import org.junit.Before;
import org.junit.Test;

public class WorkbookProcessorFacadeIntegrationTest {

	private DataProcessorFacade dataProcessorFacade;

	@Before
	public void setUp() {
		final WorkbookProcessorFacade workbookProcessorFacade = new WorkbookProcessorFacade();
		dataProcessorFacade = workbookProcessorFacade;
	}

	@Test
	public void createResultFile() {
		dataProcessorFacade.createResultFile(CLIENT_FILE, CAMPAIGN_FILE, LINKED_RESULT_PATH.toFile());
	}
}
