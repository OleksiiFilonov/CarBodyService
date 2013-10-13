package oleksii.filonov.writers;

import java.io.*;
import java.util.Map;

import com.google.common.collect.*;
import org.apache.poi.openxml4j.exceptions.*;
import org.apache.poi.ss.usermodel.*;

public interface DataBuilder {

    void useWorkbook(final Workbook clientWorkBook) throws IOException, InvalidFormatException;

    void assignTasks(final Cell[] bodyIdCells,final ListMultimap<String, Cell> linksToBodies);

    void saveToFile(final File fileToSave) throws IOException;

    void setPathToCampaignFile(final String pathToCampaignFile);

    void setVinListDescriptionMap(final Map<String, String> vinListDescriptionMap);

    public void setTaskOffset(final int taskOffset);
}
