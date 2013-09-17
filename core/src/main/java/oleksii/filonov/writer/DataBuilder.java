package oleksii.filonov.writer;

import com.google.common.collect.ListMultimap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public interface DataBuilder {

	void createDocument(File clientsFile) throws IOException, InvalidFormatException;

	void createLinkedSheetWithName(String string);

	void writeBodyIdsColumnToLinkedSheet(String bodyColumnMarker, String[] bodyIds);

	void saveToFile(File fileToSave) throws IOException;

    void linkExistingBodyIds(ListMultimap<String, String> linkedBodies, String pathToCampaignFile);
}
