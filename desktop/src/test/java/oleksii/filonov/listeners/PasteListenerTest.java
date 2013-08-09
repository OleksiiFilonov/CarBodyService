package oleksii.filonov.listeners;

import static org.mockito.Mockito.mock;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import oleksii.filonov.gui.MainTable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PasteListenerTest {

    private PasteListener pasteListener;
    @Mock
    private MainTable table;

    @Before
    public void setUp() {
        this.pasteListener = new PasteListener(this.table);
    }

    @Test
    public void whenCtrlVInvokedAndBufferEmpty_ThenValueIsChanged() {
        final ActionEvent pasteEvent = mock(ActionEvent.class);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable contents = mock(Transferable.class);
        final ClipboardOwner owner = mock(ClipboardOwner.class);
        clipboard.setContents(contents, owner);
        this.pasteListener.actionPerformed(pasteEvent);
    }

}
