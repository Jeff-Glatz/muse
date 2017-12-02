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
    private MusePairingListener mockListener;

    private MuseIO museIO;

    @BeforeEach
    void setUp() {
        museIO = new MuseIO();
        museIO.addMusePairingListener(mockListener);
    }

    @Test
    public void shouldFireInitialPairingEventWhenFalse() {
        assertThat(museIO.paired)
                .isNull();

        museIO.fireMusePaired(false);

        assertThat(museIO.paired)
                .isFalse();

        verify(mockListener)
                .onMusePaired(false);
    }

    @Test
    public void shouldFireInitialPairingEventWhenTrue() {
        assertThat(museIO.paired)
                .isNull();

        museIO.fireMusePaired(true);

        assertThat(museIO.paired)
                .isTrue();

        verify(mockListener)
                .onMusePaired(true);
    }

    @Test
    public void shouldNotFirePairingEventWhenStateDoesNotChange() {
        museIO.fireMusePaired(true);
        museIO.fireMusePaired(true);

        verify(mockListener, times(1))
                .onMusePaired(true);
    }

    @Test
    public void shouldFirePairingEventWhenStateDoesChange() {
        museIO.fireMusePaired(true);
        museIO.fireMusePaired(false);

        verify(mockListener, times(1))
                .onMusePaired(true);
        verify(mockListener, times(1))
                .onMusePaired(false);
    }
}
