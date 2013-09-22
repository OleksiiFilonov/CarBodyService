package oleksii.filonov.writer;

import java.io.*;

import com.google.common.collect.*;
import org.apache.poi.openxml4j.exceptions.*;
import org.apache.poi.ss.usermodel.*;

public interface DataBuilder {

	void useWorkbook(Workbook clientWorkBook) throws IOException, InvalidFormatException;

    void assignTasks(Cell[] bodyIds, ListMultimap<String, Cell> linkedBodies);

    void saveToFile(File fileToSave) throws IOException;

    void setPathToCampaignFile(String pathToCampaignFile);

}
