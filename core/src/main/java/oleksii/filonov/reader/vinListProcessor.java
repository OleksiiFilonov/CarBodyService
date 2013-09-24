package oleksii.filonov.reader;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

public class VinListProcessor {

    private ColumnReaderHelper columnReaderHelper;

    public Map<String, String> mapVinListIdToDescription(final File campaignFile, final String vinColumnTitle, final String descriptionTitle) {
        final Map<String, String> result = new HashMap<>();
        try {
            final Workbook campaignWB = WorkbookFactory.create(campaignFile);
            final Sheet vinListSheet = campaignWB.getSheetAt(0);
            final Iterator<Row> vinListIterator = vinListSheet.rowIterator();
            final Cell vinTitleCell = columnReaderHelper.findCell(vinListIterator, vinColumnTitle);
            final int vinColumnIndex = vinTitleCell.getColumnIndex();
            final int descriptionColumnIndex = columnReaderHelper.findCellFrom(vinTitleCell, vinListIterator, descriptionTitle).getColumnIndex();
            while (vinListIterator.hasNext()) {
                final Row vinListRow = vinListIterator.next();
                final Cell vinListCell = vinListRow.getCell(vinColumnIndex);
                Cell descriptionCell = vinListRow.getCell(descriptionColumnIndex);
                if(vinListCell != null && descriptionCell != null) {
                    result.put(vinListCell.getStringCellValue().trim(), descriptionCell.getStringCellValue().trim());
                }
            }
        } catch (IOException  | InvalidFormatException e) {
            throw new ReadDataException("Exception occurred while opening campaign file", e);
        }
        return result;
    }

    public void setColumnReaderHelper(final ColumnReaderHelper columnReaderHelper) {
        this.columnReaderHelper = columnReaderHelper;
    }
}
