package oleksii.filonov.model;

import static oleksii.filonov.model.RecordStatus.FOUND;
import static oleksii.filonov.model.RecordStatus.UNDEFINED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class MainTableModelTest {

	private static final String LINK_TO_PAGE = "linkToPage";
	private static final String INITIAL_BODY_ID = "KH0000000001";
	private static final String CHANGED_BODY_ID = "KH0000000002";
	private MainTableModel mainTableModel;

	@Before
	public void setUp() {
		mainTableModel = new MainTableModel();
	}

	@Test
	public void firstColumnIsEditable() {
		assertTrue(mainTableModel.isCellEditable(0, 0));
	}

	@Test
	public void secondColumnIsNotEditable() {
		assertFalse(mainTableModel.isCellEditable(0, 1));
	}

	@Test
	public void initialWithOneColumnName() {
		assertEquals(2, mainTableModel.getColumnCount());
	}

	@Test
	public void getNullValueOnBodyIdCell() {
		mainTableModel.getRecords().add(new Record());
		assertNull(mainTableModel.getValueAt(0, 0));
	}

	@Test
	public void getNullValueOnReferenceCell() {
		mainTableModel.getRecords().add(new Record());
		assertNull(mainTableModel.getValueAt(0, 1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void dataIsNotInitialize() {
		assertNull(mainTableModel.getValueAt(0, 0));
	}

	@Test
	public void givenTwoRecordsSet_ThenTableSizeIsTwo() {
		mainTableModel.getRecords().addAll(Lists.newArrayList(new Record(), new Record()));
		assertEquals(2, mainTableModel.getRowCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void linkIdColumnIsNotEditable() {
		mainTableModel.getRecords().add(new Record());
		mainTableModel.setValueAt(INITIAL_BODY_ID, 0, 1);
	}

	@Test
	public void whenBodyIdColumnChanged_ThenReferencesAreClearedAndStatusIsUndefined() {
		final Record testRecord = createRecord(INITIAL_BODY_ID, LINK_TO_PAGE, FOUND);
		mainTableModel.getRecords().add(testRecord);
		mainTableModel.setValueAt(CHANGED_BODY_ID, 0, 0);
		assertEquals(CHANGED_BODY_ID, testRecord.getBodyId());
		assertNull("The references were not reset", testRecord.findReference(0));
		assertThat(UNDEFINED, equalTo(testRecord.getStatus()));
	}

	@Test
	public void whenBodyIdColumnNotChanged_ThenReferencesNotClearedAndStatusNotChanged() {
		final Record testRecord = createRecord(INITIAL_BODY_ID, LINK_TO_PAGE, FOUND);
		mainTableModel.getRecords().add(testRecord);
		mainTableModel.setValueAt(INITIAL_BODY_ID, 0, 0);
		assertEquals(INITIAL_BODY_ID, testRecord.getBodyId());
		assertThat("The link shouldn't be reseted", testRecord.findReference(0), sameInstance(LINK_TO_PAGE));
		assertThat(testRecord.getStatus(), sameInstance(FOUND));
	}

	@Test
	public void addRow() {
		final int initialRowCount = mainTableModel.getRowCount();
		mainTableModel.addRow(new Record(INITIAL_BODY_ID), 0);
		final int insertRowIndex = initialRowCount;
		assertThat(mainTableModel.getRowCount(), equalTo(initialRowCount + 1));
		assertThat(mainTableModel.getValueAt(insertRowIndex, 0), equalTo(INITIAL_BODY_ID));
	}

	@Test
	public void addThreeColumns() {
		final int initialColumnCount = mainTableModel.getColumnCount();
		mainTableModel.setLinkColumnCount(3);
		assertThat(mainTableModel.getColumnCount(), equalTo(initialColumnCount + 2));
	}

	@Test
	public void removeColumn() {
		final int initialColumnCount = mainTableModel.getColumnCount();
		mainTableModel.setLinkColumnCount(2);
		mainTableModel.removeColumn();
		assertThat(mainTableModel.getColumnCount(), equalTo(initialColumnCount));
	}

	@Test
	public void removeColumnIfOnlyMoreThenBodyAndOneLinkExist() {
		final int initialColumnCount = mainTableModel.getColumnCount();
		mainTableModel.removeColumn();
		assertThat(mainTableModel.getColumnCount(), equalTo(initialColumnCount));
	}

	private Record createRecord(final String bodyId, final String link, final RecordStatus status) {
		final Record record = new Record();
		record.setBodyId(bodyId);
		record.addReference(link);
		record.setStatus(status);
		return record;
	}

}
