package oleksii.filonov.writer;

import java.io.*;
import java.util.Map;

import com.google.common.collect.*;
import org.apache.poi.openxml4j.exceptions.*;
import org.apache.poi.ss.usermodel.*;

public interface DataBuilder {

	void useWorkbook(Workbook clientWorkBook) throws IOException, InvalidFormatException;

    void assignTasks(Cell[] bodyIdCells, final Map<String, String> bodyIdDescriptionMap, ListMultimap<String, Cell> linksToBodies);

    void saveToFile(File fileToSave) throws IOException;

    void setPathToCampaignFile(String pathToCampaignFile);

}
