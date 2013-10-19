package oleksii.filonov.readers;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class CampaignProcessor {

    private ColumnReaderHelper columnReaderHelper;

    public ListMultimap<String, Cell> linkBodyIdWithCampaigns(final Cell[] bodyIds, final File campaignFile,
                                                              final String vinColumnTitle) {
        final String[] bodyIdsToProcess = extractBodyIdValues(bodyIds);
        try {
            return linkBodyIdWithCampaigns(campaignFile, vinColumnTitle, bodyIdsToProcess);
        } catch (InvalidFormatException | IOException e) {
            throw new ReadDataException("Exception occurred while opening campaign file", e);
        }
    }

    private String[] extractBodyIdValues(final Cell[] bodyIds) {
        final String[] bodyIdsToProcess = new String[bodyIds.length];
        for (int i = 0; i < bodyIds.length; ++i) {
            bodyIdsToProcess[i] = bodyIds[i].getStringCellValue().trim();
        }
        Arrays.sort(bodyIdsToProcess);
        return bodyIdsToProcess;
    }

    private ListMultimap<String, Cell> linkBodyIdWithCampaigns(final File campaignFile, final String vinColumnTitle, final String[] bodyIdsToProcess) throws IOException, InvalidFormatException {
        final ListMultimap<String, Cell> result = LinkedListMultimap.create(bodyIdsToProcess.length);
        final Workbook campaignWB = WorkbookFactory.create(campaignFile);
        final int numbersOfSheet = campaignWB.getNumberOfSheets();
        for (int sheetIndex = 1; sheetIndex < numbersOfSheet; sheetIndex++) {
            final Sheet vinSheet = campaignWB.getSheetAt(sheetIndex);
            final Iterator<Row> vinRows = vinSheet.rowIterator();
            final int vinColumnIndex = columnReaderHelper.findCell(vinRows, vinColumnTitle).getColumnIndex();
            while (vinRows.hasNext()) {
                final Row vinRow = vinRows.next();
                final Cell vinCell = vinRow.getCell(vinColumnIndex);
                if (columnReaderHelper.isStringType(vinCell)) {
                    final int bodyIndex = Arrays.binarySearch(bodyIdsToProcess, vinCell.getStringCellValue());
                    if (bodyIndex > -1) {
                        final String foundBodyId = bodyIdsToProcess[bodyIndex];
                        result.put(foundBodyId, vinCell);
                    }
                }
            }
        }
        return result;
    }

    public void setColumnReaderHelper(final ColumnReaderHelper columnReaderHelper) {
        this.columnReaderHelper = columnReaderHelper;
    }

}
