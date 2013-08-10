package oleksii.filonov.listeners;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
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
public class PasteListenerTest {

    private static final int SECOND_COLUMN = 1;
    private static final int FIRST_ROW = 0;
    private static final int FIRST_COLUMN = 0;
    private static final String ONE_LINE_BUFFER = "KH0000000002";
    private static final String BODY_ID_FIRST_LINE = "KH0000000003";
    private static final String BODY_ID_SECOND_LINE = "KH0000000004";
    private static final String THREE_LINE_BUFFER = "\n" + BODY_ID_FIRST_LINE + "\n\n" + BODY_ID_SECOND_LINE;
    private static final String INITIAL_BODY_ID = "KH0000000001";
    private PasteListener pasteListener;

    private final MainTableModel tableModel = new MainTableModel();

    @Spy
    private final MainTable table = new MainTable(this.tableModel);;

    private final Record initialRecord = new Record(INITIAL_BODY_ID);

    @Before
    public void setUp() {
        this.tableModel.getRecords().clear();
        this.tableModel.getRecords().add(this.initialRecord);
        this.pasteListener = new PasteListener(this.table);
    }

    @Test
    public void InsertOneStringInBuffer_ThenValueOfSelectedBodyIdChanged() {
        final ActionEvent pasteEvent = mock(ActionEvent.class);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final StringSelection clipbordString = new StringSelection(ONE_LINE_BUFFER);
        clipboard.setContents(clipbordString, clipbordString);
        when(this.table.getSelectedColumn()).thenReturn(FIRST_COLUMN);
        when(this.table.getSelectedRow()).thenReturn(FIRST_ROW);
        this.pasteListener.actionPerformed(pasteEvent);
        final String bodyId = this.tableModel.getRecords().get(0).getBodyId();
        assertThat(bodyId, equalTo(ONE_LINE_BUFFER));
    }

    @Test
    public void insertOneStringAndSelecteCellIsNotBodyId_ThenNothingChanged() {
        final ActionEvent pasteEvent = mock(ActionEvent.class);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final StringSelection clipbordString = new StringSelection(ONE_LINE_BUFFER);
        clipboard.setContents(clipbordString, clipbordString);
        when(this.table.getSelectedColumn()).thenReturn(SECOND_COLUMN);
        when(this.table.getSelectedRow()).thenReturn(FIRST_ROW);
        final int initRowCount = this.table.getRowCount();

        this.pasteListener.actionPerformed(pasteEvent);
        assertThat("The row count should remain the same", this.table.getRowCount(), equalTo(initRowCount));
        final String bodyId = this.tableModel.getRecords().get(0).getBodyId();
        assertThat(bodyId, equalTo(INITIAL_BODY_ID));
    }

    @Test
    public void Insert3StringsWithEmptyLine_ThenSelectedCellReplacedAnd1RowAdded() {
        final ActionEvent pasteEvent = mock(ActionEvent.class);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final StringSelection clipbordString = new StringSelection(THREE_LINE_BUFFER);
        clipboard.setContents(clipbordString, clipbordString);
        when(this.table.getSelectedColumn()).thenReturn(FIRST_COLUMN);
        when(this.table.getSelectedRow()).thenReturn(FIRST_ROW);
        final int initRowCount = this.table.getRowCount();
        this.pasteListener.actionPerformed(pasteEvent);
        assertThat("The row hasn't been inserted in the table", this.table.getRowCount(), equalTo(initRowCount + 1));
        final String firstRowbodyId = this.tableModel.getRecords().get(0).getBodyId();
        assertThat(firstRowbodyId, equalTo(BODY_ID_FIRST_LINE));
        final String secondRowbodyId = this.tableModel.getRecords().get(1).getBodyId();
        assertThat(secondRowbodyId, equalTo(BODY_ID_SECOND_LINE));
    }

}
