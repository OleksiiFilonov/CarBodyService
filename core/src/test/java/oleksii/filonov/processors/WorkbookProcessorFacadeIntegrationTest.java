package oleksii.filonov.processors;

import static oleksii.filonov.TestConstants.CAMPAIGN_FILE2;
import static oleksii.filonov.TestConstants.CLIENT_FILE2;
import static oleksii.filonov.TestConstants.LINKED_RESULT_PATH2;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class WorkbookProcessorFacadeIntegrationTest {

	private DataProcessorFacade dataProcessorFacade;

	@Before
	public void setUp() throws IOException {
		final WorkbookProcessorFacade workbookProcessorFacade = new WorkbookProcessorFacade();
		dataProcessorFacade = workbookProcessorFacade;
	}

	@Test
	public void createResultFile() {
		dataProcessorFacade.createResultFile(CLIENT_FILE2, CAMPAIGN_FILE2, LINKED_RESULT_PATH2.toFile());
	}
}
