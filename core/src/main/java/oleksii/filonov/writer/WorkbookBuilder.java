package oleksii.filonov.writer;

import com.google.common.collect.ListMultimap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WorkbookBuilder implements DataBuilder {

	private Workbook clientWorkbook;
	private CellStyle foundCellStyle;
    private CellStyle linkCellStyle;
	private CreationHelper creationHelper;
    private String pathToCampaignFile;

    @Override
	public void useWorkbook(Workbook clientWorkbook) throws IOException, InvalidFormatException {
		this.clientWorkbook = clientWorkbook;
		creationHelper = this.clientWorkbook.getCreationHelper();
		initFoundCellStyle();
        initLinkCellStyle();
	}

	@Override
	public void saveToFile(final File fileToSave) throws IOException {
		final FileOutputStream recordStream = new FileOutputStream(fileToSave);
		clientWorkbook.write(recordStream);
		recordStream.close();
	}

    @Override
    public void setPathToCampaignFile(String pathToCampaignFile) {
        this.pathToCampaignFile = pathToCampaignFile;
    }

    @Override
	public void assignTasks(final Cell[] bodyIdCells, final ListMultimap<String, String> linkedBodies) {
		for (final Cell bodyIdCell : bodyIdCells) {
			final List<String> links = linkedBodies.get(bodyIdCell.getStringCellValue());
			if (!links.isEmpty()) {
                final Row bodyIdRow = bodyIdCell.getRow();
                bodyIdCell.setCellStyle(foundCellStyle);
				writeLinksForFoundCell(bodyIdCell.getColumnIndex(), links, bodyIdRow);
				int cellIndex = 1;
				while (bodyIdRow.getCell(cellIndex) != null) {
					++cellIndex;
				}
			}
		}
	}

	private void writeLinksForFoundCell(final int bodyIdColumnIndex, final List<String> links, final Row bodyIdRow) {
		for (int i = 0; i < links.size(); i++) {
			final Cell linkToVin = bodyIdRow.createCell(bodyIdColumnIndex + i + 1, Cell.CELL_TYPE_STRING);
			linkToVin.setCellValue(links.get(i));
			final Hyperlink cellHyperlink = creationHelper.createHyperlink(Hyperlink.LINK_FILE);
			cellHyperlink.setAddress(pathToCampaignFile + "#" + links.get(i));
			cellHyperlink.setLabel(links.get(i));
			linkToVin.setHyperlink(cellHyperlink);
            linkToVin.setCellStyle(linkCellStyle);
		}
	}

	private void initLinkCellStyle() {
		linkCellStyle = clientWorkbook.createCellStyle();
        linkCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        linkCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	}

    private void initFoundCellStyle() {
		foundCellStyle = clientWorkbook.createCellStyle();
        foundCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		foundCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	}

}
