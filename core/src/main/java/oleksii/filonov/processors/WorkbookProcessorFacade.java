package oleksii.filonov.processors;

import java.io.IOException;
import java.util.Map;

import oleksii.filonov.readers.CampaignProcessor;
import oleksii.filonov.readers.ColumnExcelReader;
import oleksii.filonov.readers.ColumnReaderHelper;
import oleksii.filonov.readers.ReadDataException;
import oleksii.filonov.readers.VinListProcessor;
import oleksii.filonov.settings.Settings;
import oleksii.filonov.writers.DataBuilder;
import oleksii.filonov.writers.WorkbookBuilder;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.collect.ListMultimap;

public class WorkbookProcessorFacade implements DataProcessorFacade {

	private final CampaignProcessor campaignProcessor;
	private final VinListProcessor vinListProcessor;
	private final ColumnExcelReader columnExcelReader;

	public WorkbookProcessorFacade() throws IOException {
		final ColumnReaderHelper columnReaderHelper = new ColumnReaderHelper();
		columnExcelReader = new ColumnExcelReader();
		columnExcelReader.setColumnReaderHelper(columnReaderHelper);
		campaignProcessor = new CampaignProcessor();
		campaignProcessor.setColumnReaderHelper(columnReaderHelper);
		vinListProcessor = new VinListProcessor();
		vinListProcessor.setColumnReaderHelper(columnReaderHelper);
	}

	@Override
	public void createResultFile(Settings settings, FilesToProcess filesToProcess) {
		try {
			final Workbook clientsWb = WorkbookFactory.create(filesToProcess.getClientsFile());
			final Sheet clientsSheet = clientsWb.getSheetAt(0);
			final Cell[] bodyIds = columnExcelReader.extractCellsFromColumn(
                    settings.getClientColumnBodyNumber(), clientsSheet);
			final ListMultimap<String, Cell> linkedBodyIdWithCampaigns = campaignProcessor.linkBodyIdWithCampaigns(
					bodyIds, filesToProcess.getCampaignFile(), settings.getCampaignColumnVinListIdTitle());
            final Map<String, String> bodyIdDescriptionMap = vinListProcessor.mapVinListIdToDescription(filesToProcess.getCampaignFile(),
					settings.getCampaignColumnNumberCampaignTitle(), settings.getCampaignColumnDescriptionTitle());
            final DataBuilder excelBuilder = new WorkbookBuilder();
            excelBuilder.useWorkbook(clientsWb);
            excelBuilder.setPathToCampaignFile(filesToProcess.getCampaignFile().getName());
            excelBuilder.setVinListDescriptionMap(bodyIdDescriptionMap);
            excelBuilder.setTaskOffset(columnExcelReader.findDistanceToEndFrom(settings.getClientColumnBodyNumber(), clientsSheet));
			excelBuilder.assignTasks(bodyIds, linkedBodyIdWithCampaigns);
			excelBuilder.saveToFile(filesToProcess.getResultFile());
		} catch (IOException | InvalidFormatException exc) {
			throw new ReadDataException("Exception happened while processing result file", exc);
		}
	}

}
