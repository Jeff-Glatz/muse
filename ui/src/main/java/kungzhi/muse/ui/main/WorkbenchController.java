package kungzhi.muse.ui.main;

import javafx.fxml.FXML;
import javafx.scene.Node;
import kungzhi.muse.ui.common.NotificationControl;
import kungzhi.muse.ui.common.StatusControl;
import kungzhi.muse.ui.common.AbstractController;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.StatusBar;
import org.controlsfx.control.action.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static javafx.application.Platform.runLater;

@Controller
public class WorkbenchController
        extends AbstractController
        implements NotificationControl, StatusControl {
    private final ScheduledExecutorService executor;
    private long notificationDuration = 5000;

    @FXML
    private NotificationPane notificationPane;

    @FXML
    private StatusBar statusBar;

    @Autowired
    public WorkbenchController(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public long getNotificationDuration() {
        return notificationDuration;
    }

    public void setNotificationDuration(long notificationDuration) {
        this.notificationDuration = notificationDuration;
    }

    @Override
    public void notification(String text) {
        notificationPane.show(text);
        executor.schedule(() -> runLater(this::hideNotifications),
                notificationDuration, MILLISECONDS);
    }

    @Override
    public void notification(String text, Node graphic, Action... actions) {
        notificationPane.show(text, graphic, actions);
        executor.schedule(() -> runLater(this::hideNotifications),
                notificationDuration, MILLISECONDS);
    }

    @Override
    public void hideNotifications() {
        notificationPane.hide();
        notificationPane.setText(null);
        notificationPane.setGraphic(null);
        notificationPane.getActions()
                .clear();
    }

    @Override
    public void status(String text) {
        statusBar.setText(text);
    }

    @Override
    public void status(Node node) {
        statusBar.setGraphic(node);
    }

    @Override
    public void status(double progress) {
        statusBar.setProgress(progress);
    }
}
