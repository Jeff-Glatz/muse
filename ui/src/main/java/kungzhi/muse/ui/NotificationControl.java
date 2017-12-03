package kungzhi.muse.ui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.action.Action;

public interface NotificationControl {
    void notification(String text);

    void notification(String text, Node graphic, Action... actions);

    default void information(String text, Action... actions) {
        notification(text, new ImageView(new Image(getClass()
                        .getResourceAsStream("/org/controlsfx/dialog/dialog-information.png"))),
                actions);
    }

    default void confirm(String text, Action... actions) {
        notification(text, new ImageView(new Image(getClass()
                        .getResourceAsStream("/org/controlsfx/dialog/dialog-confirm.png"))),
                actions);
    }

    default void warning(String text, Action... actions) {
        notification(text, new ImageView(new Image(getClass()
                        .getResourceAsStream("/org/controlsfx/dialog/dialog-warning.png"))),
                actions);
    }

    default void error(String text, Action... actions) {
        notification(text, new ImageView(new Image(getClass()
                        .getResourceAsStream("/org/controlsfx/dialog/dialog-error.png"))),
                actions);
    }

    void hideNotifications();
}
