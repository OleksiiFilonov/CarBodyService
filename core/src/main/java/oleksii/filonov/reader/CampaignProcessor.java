package oleksii.filonov.reader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * Not thread safe!!!
 */
public class CampaignProcessor {

	private ColumnReaderHelper columnReaderHelper;

	private int maxReferenceNumber;

	private static final int OFFSET = 1;
	private static char[] COLUMN_INDEXES = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'Y', 'V', 'W', 'X', 'Y', 'Z' };

	public ListMultimap<String, String> linkBodyIdWithCampaigns(final String[] bodyIds, final File campaignFile,
			final String vinColumnMarker) {
		final ListMultimap<String, String> result = LinkedListMultimap.create(bodyIds.length);
		Arrays.sort(bodyIds);
		try {
			final Workbook compaignWB = WorkbookFactory.create(campaignFile);
			final int numbersOfSheet = compaignWB.getNumberOfSheets();
			for (int sheetIndex = 1; sheetIndex < numbersOfSheet; sheetIndex++) {
				final Sheet vinSheet = compaignWB.getSheetAt(sheetIndex);
				final Iterator<Row> vinRows = vinSheet.rowIterator();
				final int vinColumnIndex = columnReaderHelper.findColumnIndex(vinRows, vinColumnMarker);
				while (vinRows.hasNext()) {
					final Row vinRow = vinRows.next();
					final Cell vinCell = vinRow.getCell(vinColumnIndex);
					if (columnReaderHelper.isStringType(vinCell)) {
						final int bodyIndex = Arrays.binarySearch(bodyIds, vinCell.getStringCellValue());
						if (bodyIndex > -1) {
							final String foundBodyId = bodyIds[bodyIndex];
							result.put(foundBodyId, linkToCell(vinCell));
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

	private String linkToCell(final Cell vinCell) {
		return "'" + vinCell.getSheet().getSheetName() + "'!" + COLUMN_INDEXES[vinCell.getColumnIndex()]
				+ (vinCell.getRowIndex() + OFFSET);
	}

	public int getMaxReferenceNumber() {
		return maxReferenceNumber;
	}

	public void setColumnReaderHelper(final ColumnReaderHelper columnReaderHelper) {
		this.columnReaderHelper = columnReaderHelper;
	}

	public ColumnReaderHelper getColumnReaderHelper() {
		return columnReaderHelper;
	}

}
