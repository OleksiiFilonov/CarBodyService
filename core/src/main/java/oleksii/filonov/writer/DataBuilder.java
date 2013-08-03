package oleksii.filonov.writer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

public interface DataBuilder {

	void createDocument();

	void createMainSheetWithName(String string);

	void writeBodyIdsColumnToMainPage(String[] bodyIds);

	void saveToFile(File resultFile) throws IOException;

	Map<String, Cell> getBodyIdCellMap();

	void linkExistingBodyIds(File file);

}
