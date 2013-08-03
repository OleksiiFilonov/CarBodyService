package oleksii.filonov.reader;

import java.io.File;

public interface ColumnReader {

	String[] getUniqueColumnValues(File fileToRead, String columnMarker);

}
