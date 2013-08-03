package oleksii.filonov.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

public class ColumnExcelReaderTest {

	private static final String[] FILE_TO_READ = new String[] { "src", "test", "resources", "Clients.xls" };

	private static final String WRONG_COLUMN_MARKER = "noSuchColumnName";

	private final ColumnReader columnExcelReader = new ColumnExcelReader();

	private static String BODY_ID_MARKER = "Номер кузова";

	@Test
	public void testReadFromXLSXClientsFile() throws IOException, InvalidFormatException {
		final String[] uniqueBodyIds = columnExcelReader.getUniqueColumnValues(Paths.get(".", FILE_TO_READ).toFile(),
				BODY_ID_MARKER);
		assertEquals("The first body id is not valid", "KMHBT51DBBU022069", uniqueBodyIds[0]);
		assertEquals("The tenth body id is not valid", "KMHST81CADU139882", uniqueBodyIds[9]);
		assertEquals("The last body id is not valid", "KMHEC41BBCA350931", uniqueBodyIds[uniqueBodyIds.length - 1]);
	}

	@Test
	public void dontFindTheMarkerColumn() {
		final String columnToRead = WRONG_COLUMN_MARKER;
		try {
			columnExcelReader.getUniqueColumnValues(Paths.get(".", FILE_TO_READ).toFile(), columnToRead);
			fail(String.format("The test shouldn't find the column \"%s\" in source file", columnToRead));
		} catch (final ReadDataException exc) {
			System.out.println(exc.getMessage());
		}
	}
}
