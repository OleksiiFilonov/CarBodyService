package oleksii.filonov.writer;

import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;

public interface DataBuilder {

	void createDocument();

	void createLinkedSheetWithName(String string);

	void writeBodyIdsColumnToLinkedSheet(String bodyColumnMarker, String[] bodyIds);

	void saveToFile(File fileToSave) throws IOException;

	public Cell[] getBodyIdCells();

	void linkExistingBodyIds(String vinColumnMarker, File campaignSource);

}
