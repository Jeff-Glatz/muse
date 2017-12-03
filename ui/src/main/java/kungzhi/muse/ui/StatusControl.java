package kungzhi.muse.ui;

import javafx.scene.Node;

public interface StatusControl {
    void status(String text);
    void status(Node node);
    void status(double progress);
}
