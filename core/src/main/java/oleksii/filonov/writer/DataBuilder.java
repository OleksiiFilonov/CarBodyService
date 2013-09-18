package oleksii.filonov.writer;

import com.google.common.collect.ListMultimap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;

public interface DataBuilder {

	void useWorkbook(Workbook clientWorkBook) throws IOException, InvalidFormatException;

    void assignTasks(Cell[] bodyIds, ListMultimap<String, String> linkedBodies);

    void saveToFile(File fileToSave) throws IOException;

    void setPathToCampaignFile(String pathToCampaignFile);


}
