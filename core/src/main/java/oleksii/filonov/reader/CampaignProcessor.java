package oleksii.filonov.reader;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Not thread safe!!!
 */
public class CampaignProcessor {

	private ColumnReaderHelper columnReaderHelper;

	private int maxReferenceNumber;


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
				final int vinColumnIndex = columnReaderHelper.findColumnIndex(vinRows, vinColumnMarker);
				while (vinRows.hasNext()) {
					final Row vinRow = vinRows.next();
					final Cell vinCell = vinRow.getCell(vinColumnIndex);
					if (columnReaderHelper.isStringType(vinCell)) {
						final int bodyIndex = Arrays.binarySearch(bodyIdsToProcess, vinCell.getStringCellValue());
						if (bodyIndex > -1) {
							final String foundBodyId = bodyIdsToProcess[bodyIndex];
							result.put(foundBodyId, vinCell);
							maxReferenceNumber = Math.max(maxReferenceNumber, result.get(foundBodyId).size());
						}
					}
				}
			}
		} catch (InvalidFormatException | IOException e) {
			throw new ReadDataException(e);
		}
		return result;
	}

	public int getMaxReferenceNumber() {
		return maxReferenceNumber;
	}

	public void setColumnReaderHelper(final ColumnReaderHelper columnReaderHelper) {
		this.columnReaderHelper = columnReaderHelper;
	}

}
