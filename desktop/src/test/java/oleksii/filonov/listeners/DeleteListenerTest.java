package oleksii.filonov.listeners;

import java.awt.event.ActionEvent;

import oleksii.filonov.gui.MainTable;
import oleksii.filonov.model.MainTableModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeleteListenerTest {

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
    public void deleteSelectedRow() {
        this.deleteListener.actionPerformed(this.event);
    }

}
