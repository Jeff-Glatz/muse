package kungzhi.muse.ui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.action.Action;

public interface Notifier {
    void show(String text);

    void show(String text, Node graphic, Action... actions);

    default void showInformation(String text, Action... actions) {
        show(text, new ImageView(new Image(getClass()
                        .getResourceAsStream("/org/controlsfx/dialog/dialog-information.png"))),
                actions);
    }

    default void showConfirm(String text, Action... actions) {
        show(text, new ImageView(new Image(getClass()
                        .getResourceAsStream("/org/controlsfx/dialog/dialog-confirm.png"))),
                actions);
    }

    default void showWarning(String text, Action... actions) {
        show(text, new ImageView(new Image(getClass()
                        .getResourceAsStream("/org/controlsfx/dialog/dialog-warning.png"))),
                actions);
    }

    default void showError(String text, Action... actions) {
        show(text, new ImageView(new Image(getClass()
                        .getResourceAsStream("/org/controlsfx/dialog/dialog-error.png"))),
                actions);
    }

    void hide();
}
