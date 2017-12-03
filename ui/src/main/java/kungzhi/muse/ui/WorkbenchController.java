package kungzhi.muse.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.action.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Controller
public class WorkbenchController
        extends AbstractController
        implements Notifier {
    private final ScheduledExecutorService executor;
    private long notificationDuration = 5000;

    @FXML
    private NotificationPane notificationPane;

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
    public void show(String text) {
        notificationPane.show(text);
        executor.schedule(this::hide, notificationDuration, MILLISECONDS);
    }

    @Override
    public void show(String text, Node graphic, Action... actions) {
        notificationPane.show(text, graphic, actions);
        executor.schedule(notificationPane::hide, notificationDuration, MILLISECONDS);
    }

    @Override
    public void hide() {
        notificationPane.hide();
        notificationPane.setGraphic(null);
    }
}
