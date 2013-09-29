package oleksii.filonov.readers;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;
import org.apache.poi.openxml4j.exceptions.*;
import org.apache.poi.ss.usermodel.*;

public class CampaignProcessor {

	private ColumnReaderHelper columnReaderHelper;

	public ListMultimap<String, Cell> linkBodyIdWithCampaigns(final Cell[] bodyIds, final File campaignFile,
			final String vinColumnMarker) {
		final String [] bodyIdsToProcess = new String[bodyIds.length];
        for (int i = 0; i < bodyIds.length; ++i) {
            bodyIdsToProcess[i] = bodyIds[i].getStringCellValue();
        }
        final ListMultimap<String, Cell> result = LinkedListMultimap.create(bodyIds.length);
		Arrays.sort(bodyIdsToProcess);
		try {
			final Workbook campaignWB = WorkbookFactory.create(campaignFile);
			final int numbersOfSheet = campaignWB.getNumberOfSheets();
			for (int sheetIndex = 1; sheetIndex < numbersOfSheet; sheetIndex++) {
				final Sheet vinSheet = campaignWB.getSheetAt(sheetIndex);
				final Iterator<Row> vinRows = vinSheet.rowIterator();
				final int vinColumnIndex = columnReaderHelper.findCell(vinRows, vinColumnMarker).getColumnIndex();
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
		} catch (InvalidFormatException | IOException e) {
			throw new ReadDataException("Exception occurred while opening campaign file", e);
		}
		return result;
	}

	public void setColumnReaderHelper(final ColumnReaderHelper columnReaderHelper) {
		this.columnReaderHelper = columnReaderHelper;
	}

}
