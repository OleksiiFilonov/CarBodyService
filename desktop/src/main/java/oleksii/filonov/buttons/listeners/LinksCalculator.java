package oleksii.filonov.buttons.listeners;

import static oleksii.filonov.model.RecordStatus.FOUND;
import static oleksii.filonov.model.RecordStatus.NOT_FOUND;

import java.util.List;

import oleksii.filonov.gui.ComponentsLocator;
import oleksii.filonov.gui.MainFileChooser;
import oleksii.filonov.gui.MainTable;
import oleksii.filonov.model.Record;
import oleksii.filonov.reader.CampaignProcessor;
import oleksii.filonov.reader.ColumnReaderHelper;

import com.google.common.collect.ListMultimap;

public class LinksCalculator {

	private static final String VIN_COLUMN_MARKER = "VIN";

	private String[] bodyIds;

	private ListMultimap<String, String> bodyIdLinks;

	public void calculate(final MainFileChooser fileChooser) {
		final CampaignProcessor processor = new CampaignProcessor();
		processor.setColumnReaderHelper(new ColumnReaderHelper());
		final MainTable table = ComponentsLocator.getInstanse().getTable();
		table.getModel().reinitializeRecords();
		final List<Record> records = table.getModel().getRecords();
		bodyIds = new String[records.size()];
		for (int i = 0; i < records.size(); i++) {
			bodyIds[i] = records.get(i).getBodyId();
		}
		bodyIdLinks = processor.linkBodyIdWithCampaigns(bodyIds, fileChooser.getCampaignFile(), VIN_COLUMN_MARKER);
		table.getModel().setLinkColumnCount(processor.getMaxReferenceNumber());
		for (final Record record : records) {
			final List<String> links = bodyIdLinks.get(record.getBodyId());
			if (links.isEmpty()) {
				record.setStatus(NOT_FOUND);
			} else {
				record.setStatus(FOUND);
				record.setReferences(links);
			}
		}
	}

	public String[] getBodyIds() {
		return bodyIds;
	}

	public ListMultimap<String, String> getBodyIdLinks() {
		return bodyIdLinks;
	}

}