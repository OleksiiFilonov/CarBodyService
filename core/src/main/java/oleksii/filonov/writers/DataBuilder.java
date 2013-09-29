package oleksii.filonov.writers;

import java.io.*;
import java.util.Map;

import com.google.common.collect.*;
import org.apache.poi.openxml4j.exceptions.*;
import org.apache.poi.ss.usermodel.*;

public interface DataBuilder {

	void useWorkbook(Workbook clientWorkBook) throws IOException, InvalidFormatException;

    void assignTasks(Cell[] bodyIdCells, ListMultimap<String, Cell> linksToBodies);

    void saveToFile(File fileToSave) throws IOException;

    void setPathToCampaignFile(String pathToCampaignFile);

    void setVinListDescriptionMap(Map<String, String> vinListDescriptionMap);
}
