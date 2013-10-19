package oleksii.filonov.writers;

import com.google.common.collect.ListMultimap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface DataBuilder {

    void useWorkbook(final Workbook clientWorkBook);

    void assignTasks(final Cell[] bodyIdCells, final ListMultimap<String, Cell> linksToBodies);

    void saveToFile(final File fileToSave) throws IOException;

    void setPathToCampaignFile(final String pathToCampaignFile);

    void setVinListDescriptionMap(final Map<String, String> vinListDescriptionMap);

    public void setTaskOffset(final int taskOffset);
}
