package kungzhi.muse.platform;

import kungzhi.muse.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MuseIOTest
        extends UnitTest {

    @Mock
    private ConnectionListener mockListener;

    private MuseIO museIO;

    @BeforeEach
    void setUp() {
        museIO = new MuseIO();
        museIO.addConnectionListener(mockListener);
    }

    @Test
    public void shouldFireInitialPairingEventWhenFalse() {
        assertThat(museIO.connected)
                .isNull();

        museIO.fireConnected(false);

        assertThat(museIO.connected)
                .isFalse();

        verify(mockListener)
                .onConnected(false);
    }

    @Test
    public void shouldFireInitialPairingEventWhenTrue() {
        assertThat(museIO.connected)
                .isNull();

        museIO.fireConnected(true);

        assertThat(museIO.connected)
                .isTrue();

        verify(mockListener)
                .onConnected(true);
    }

    @Test
    public void shouldNotFirePairingEventWhenStateDoesNotChange() {
        museIO.fireConnected(true);
        museIO.fireConnected(true);

        verify(mockListener, times(1))
                .onConnected(true);
    }

    @Test
    public void shouldFirePairingEventWhenStateDoesChange() {
        museIO.fireConnected(true);
        museIO.fireConnected(false);

        verify(mockListener, times(1))
                .onConnected(true);
        verify(mockListener, times(1))
                .onConnected(false);
    }
}
