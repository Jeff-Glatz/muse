package kungzhi.muse.test;

import org.junit.jupiter.api.BeforeEach;

import static org.mockito.MockitoAnnotations.initMocks;

public abstract class UnitTest {

    @BeforeEach
    void initialize() {
        initMocks(this);
    }
}
