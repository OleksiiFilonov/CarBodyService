package oleksii.filonov.processors;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ListMultimap;
import oleksii.filonov.readers.*;
import oleksii.filonov.writers.DataBuilder;
import oleksii.filonov.writers.WorkbookBuilder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class WorkbookProcessorFacade implements DataProcessorFacade {

    private static final String BODY_ID_COLUMN_TITLE = "Номер кузова";
    private static final String VIN_COLUMN_TITLE = "VIN";
    private static final String CAMPAIGN_ID_COLUMN_TITLE = "Номер кампании";
    private static final String DESCRIPTION_COLUMN_TITLE = "Описание";
    private final CampaignProcessor campaignProcessor;
    private final VinListProcessor vinListProcessor;
    private ColumnExcelReader columnExcelReader;

    public WorkbookProcessorFacade() {
        ColumnReaderHelper columnReaderHelper = new ColumnReaderHelper();
        columnExcelReader = new ColumnExcelReader();
        columnExcelReader.setColumnReaderHelper(columnReaderHelper);
        campaignProcessor = new CampaignProcessor();
        campaignProcessor.setColumnReaderHelper(columnReaderHelper);
        vinListProcessor = new VinListProcessor();
        vinListProcessor.setColumnReaderHelper(columnReaderHelper);

    }

    @Override
    public void createResultFile(final File clientsFile, final File campaignFile, final File resultFile) {
        try {
            final Workbook clientsWb = WorkbookFactory.create(clientsFile);
            final Sheet clientsSheet = clientsWb.getSheetAt(0);
            final Cell[] bodyIds = columnExcelReader.getColumnValues(clientsSheet, BODY_ID_COLUMN_TITLE);
            DataBuilder excelBuilder = new WorkbookBuilder();
            excelBuilder.useWorkbook(clientsWb);
            excelBuilder.setPathToCampaignFile(campaignFile.getName());
            final ListMultimap<String, Cell> linkedBodyIdWithCampaigns = campaignProcessor.linkBodyIdWithCampaigns(
                    bodyIds, campaignFile, VIN_COLUMN_TITLE);
            final Map<String, String> bodyIdDescriptionMap = vinListProcessor.mapVinListIdToDescription(
                    campaignFile, CAMPAIGN_ID_COLUMN_TITLE, DESCRIPTION_COLUMN_TITLE);
            excelBuilder.setVinListDescriptionMap(bodyIdDescriptionMap);
            excelBuilder.assignTasks(bodyIds, linkedBodyIdWithCampaigns);
            excelBuilder.saveToFile(resultFile);
        } catch (IOException | InvalidFormatException exc) {
            throw new ReadDataException("Exception happened while processing result file", exc);
        }
    }
}
