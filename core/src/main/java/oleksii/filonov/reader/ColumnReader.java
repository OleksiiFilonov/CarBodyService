package oleksii.filonov.reader;

import org.apache.poi.ss.usermodel.Sheet;

public interface ColumnReader {

	String[] getUniqueColumnValues(Sheet sheetToRead, String columnMarker);

}
