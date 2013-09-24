package oleksii.filonov.reader;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class VinListProcessor {

    public Map<String, String> mapVinListIdToDescription(File campaignFile) {
        try {
            final Workbook campaignWB = WorkbookFactory.create(campaignFile);
            final Sheet vinListSheet = campaignWB.getSheetAt(0);
        } catch (IOException  | InvalidFormatException e) {
            throw new ReadDataException("Exception occurred while opening campaign file", e);
        }
        return new HashMap<>();
    }
}
