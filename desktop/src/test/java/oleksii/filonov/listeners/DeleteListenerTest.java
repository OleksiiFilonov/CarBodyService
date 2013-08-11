package oleksii.filonov.listeners;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;

import oleksii.filonov.gui.MainTable;
import oleksii.filonov.model.MainTableModel;
import oleksii.filonov.model.Record;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class DeleteListenerTest {

    private static final int FIRST_ROW = 0;
    private static final int SECOND_ROW = 1;
    private static final int FIRST_COLUMN = 0;

    private DeleteListener deleteListener;

    private final MainTableModel tableModel = new MainTableModel();
    @Spy
    private final MainTable table = new MainTable(this.tableModel);
    @Mock
    private ActionEvent event;

    @Before
    public void setUp() {
        this.tableModel.getRecords().clear();
        this.deleteListener = new DeleteListener(this.table);
    }

    @Test
    public void doNothingWhenNoOneRowInTable() {
        final int initialRowCount = this.table.getRowCount();

        this.deleteListener.actionPerformed(this.event);
        assertThat(this.table.getRowCount(), equalTo(initialRowCount));
    }

    @Test
    public void removeSelectedRow() {
        final Record initialRecord = new Record();
        this.tableModel.getRecords().add(initialRecord);
        final int initialRowCount = this.table.getRowCount();
        when(this.table.getSelectedRow()).thenReturn(FIRST_ROW);
        when(this.table.getSelectedRowCount()).thenReturn(1);

        this.deleteListener.actionPerformed(this.event);
        assertThat(this.table.getRowCount(), equalTo(initialRowCount - 1));
    }

    @Test
    public void removeSelectedDiapasoneFromSecondToFourthRows() {
        // add 4 records to table model
        final String firstBodyId = "1";
        final String fourthBodyId = "4";
        this.tableModel.getRecords()
                .addAll(Lists.newArrayList(new Record(firstBodyId), new Record("2"), new Record("3"), new Record(
                        fourthBodyId)));
        final int initialRowCount = this.table.getRowCount();
        when(this.table.getSelectedRow()).thenReturn(SECOND_ROW);
        when(this.table.getSelectedRowCount()).thenReturn(2);

        this.deleteListener.actionPerformed(this.event);
        assertThat(this.table.getRowCount(), equalTo(initialRowCount - 2));
        assertThat(this.table.getValueAt(FIRST_ROW, FIRST_COLUMN), equalTo(firstBodyId));
        assertThat(this.table.getValueAt(SECOND_ROW, FIRST_COLUMN), equalTo(fourthBodyId));
    }
}
