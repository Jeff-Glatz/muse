package kungzhi.muse.ui.common;

import javafx.scene.Node;

public interface StatusControl {
    void status(String text);
    void status(Node node);
    void status(double progress);
}
