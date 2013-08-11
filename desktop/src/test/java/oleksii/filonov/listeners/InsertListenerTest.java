package oleksii.filonov.listeners;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;

import oleksii.filonov.gui.MainTable;
import oleksii.filonov.model.MainTableModel;
import oleksii.filonov.model.Record;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InsertListenerTest {

    private static final int FIRST_ROW = 0;
    private static final int SECOND_ROW = 1;
    private static final int FIRST_COLUMN = 0;
    private static final String INITIAL_BODY_ID = "KH0000000001";

    private InsertListener insertListener;

    private final MainTableModel tableModel = new MainTableModel();
    @Spy
    private final MainTable table = new MainTable(this.tableModel);

    @Before
    public void setUp() {
        this.tableModel.getRecords().clear();
        final Record initialRecord = new Record(INITIAL_BODY_ID);
        this.tableModel.getRecords().add(initialRecord);
        this.tableModel.getRecords().add(initialRecord);
        this.insertListener = new InsertListener(this.table);
    }

    @Test
    public void insertRowAfterSelected() {
        final ActionEvent event = mock(ActionEvent.class);
        final int initialRowCount = this.table.getRowCount();
        when(this.table.getSelectedColumn()).thenReturn(FIRST_COLUMN);
        when(this.table.getSelectedRow()).thenReturn(FIRST_ROW);
        this.insertListener.actionPerformed(event);
        assertThat("The row hasn't been inserted", this.table.getRowCount(), equalTo(initialRowCount + 1));
        assertThat(this.table.getValueAt(SECOND_ROW, FIRST_COLUMN), equalTo(null));
    }

}
