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
    private static final String TEST_BODY_ID = "KH0000000001";
    private MainTableModel mainTableModel;

    @Before
    public void setUp() {
        this.mainTableModel = new MainTableModel();
    }

    @Test
    public void firstColumnIsEditable() {
        assertTrue(this.mainTableModel.isCellEditable(0, 0));
    }

    @Test
    public void secondColumnIsNotEditable() {
        assertFalse(this.mainTableModel.isCellEditable(0, 1));
    }

    @Test
    public void initialWithOneColumnName() {
        assertEquals(2, this.mainTableModel.getColumnCount());
    }

    @Test
    public void getNullValueOnBodyIdCell() {
        this.mainTableModel.getRecords().add(new Record());
        assertNull(this.mainTableModel.getValueAt(0, 0));
    }

    @Test
    public void getNullValueOnReferenceCell() {
        this.mainTableModel.getRecords().add(new Record());
        assertNull(this.mainTableModel.getValueAt(0, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void dataIsNotInitialize() {
        assertNull(this.mainTableModel.getValueAt(0, 0));
    }

    @Test
    public void givenTwoRecordsSet_ThenTableSizeIsTwo() {
        this.mainTableModel.getRecords().addAll(Lists.newArrayList(new Record(), new Record()));
        assertEquals(2, this.mainTableModel.getRowCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void linkIdColumnIsNotEditable() {
        this.mainTableModel.getRecords().add(new Record());
        this.mainTableModel.setValueAt(TEST_BODY_ID, 0, 1);
    }

    @Test
    public void whenBodyIdColumnChanged_ThenReferencesAreClearedAndStatusIsUndefined() {
        final Record testRecord = new Record();
        testRecord.getReferences().add(LINK_TO_PAGE);
        testRecord.setStatus(FOUND);
        this.mainTableModel.getRecords().add(testRecord);
        this.mainTableModel.setValueAt(TEST_BODY_ID, 0, 0);
        assertEquals(TEST_BODY_ID, testRecord.getBodyId());
        assertNull("The references were not reset", testRecord.findReference(0));
        assertThat(UNDEFINED, equalTo(testRecord.getStatus()));
    }

    @Test
    public void whenBodyIdColumnNotChanged_ThenReferencesNotClearedAndStatusNotChanged() {
        final Record testRecord = new Record(TEST_BODY_ID);
        testRecord.getReferences().add(LINK_TO_PAGE);
        testRecord.setStatus(FOUND);
        this.mainTableModel.getRecords().add(testRecord);
        this.mainTableModel.setValueAt(TEST_BODY_ID, 0, 0);
        assertEquals(TEST_BODY_ID, testRecord.getBodyId());
        assertThat("The link shouldn't be reseted", testRecord.findReference(0), sameInstance(LINK_TO_PAGE));
        assertThat(testRecord.getStatus(), sameInstance(FOUND));
    }

}