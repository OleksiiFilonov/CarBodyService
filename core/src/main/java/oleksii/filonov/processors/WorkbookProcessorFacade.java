package oleksii.filonov.processors;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import oleksii.filonov.readers.CampaignProcessor;
import oleksii.filonov.readers.ColumnExcelReader;
import oleksii.filonov.readers.ColumnReaderHelper;
import oleksii.filonov.readers.ReadDataException;
import oleksii.filonov.readers.VinListProcessor;
import oleksii.filonov.settings.PropertiesLoader;
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
	private final Settings settings;

	public WorkbookProcessorFacade() throws IOException {
		final ColumnReaderHelper columnReaderHelper = new ColumnReaderHelper();
		columnExcelReader = new ColumnExcelReader();
		columnExcelReader.setColumnReaderHelper(columnReaderHelper);
		campaignProcessor = new CampaignProcessor();
		campaignProcessor.setColumnReaderHelper(columnReaderHelper);
		vinListProcessor = new VinListProcessor();
		vinListProcessor.setColumnReaderHelper(columnReaderHelper);
		final PropertiesLoader loader = new PropertiesLoader();
		loader.loadProperties();
		settings = loader.getSettings();

	}

	@Override
	public void createResultFile(final File clientsFile, final File campaignFile, final File resultFile) {
		try {
			final Workbook clientsWb = WorkbookFactory.create(clientsFile);
			final Sheet clientsSheet = clientsWb.getSheetAt(0);
			final Cell[] bodyIds = columnExcelReader
					.getColumnValues(clientsSheet, settings.getClientColumnBodyNumber());
			final DataBuilder excelBuilder = new WorkbookBuilder();
			excelBuilder.useWorkbook(clientsWb);
			excelBuilder.setPathToCampaignFile(campaignFile.getName());
			final ListMultimap<String, Cell> linkedBodyIdWithCampaigns = campaignProcessor.linkBodyIdWithCampaigns(
					bodyIds, campaignFile, settings.getCampaignColumnVinListIdTitle());
			final Map<String, String> bodyIdDescriptionMap = vinListProcessor.mapVinListIdToDescription(campaignFile,
					settings.getCampaignColumnNumberCampaignTitle(), settings.getCampaignColumnDescriptionTitle());
			excelBuilder.setVinListDescriptionMap(bodyIdDescriptionMap);
			excelBuilder.assignTasks(bodyIds, linkedBodyIdWithCampaigns);
			excelBuilder.saveToFile(resultFile);
		} catch (IOException | InvalidFormatException exc) {
			throw new ReadDataException("Exception happened while processing result file", exc);
		}
	}
}
