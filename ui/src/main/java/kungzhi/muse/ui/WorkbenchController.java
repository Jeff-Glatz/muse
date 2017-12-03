package kungzhi.muse.ui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.util.Duration;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.action.Action;
import org.springframework.stereotype.Controller;

import static javafx.util.Duration.ZERO;
import static javafx.util.Duration.millis;
import static javafx.util.Duration.seconds;

@Controller
public class WorkbenchController
        extends AbstractController
        implements Notifier {
    private Duration notificationDuration = seconds(5);

    @FXML
    private NotificationPane notificationPane;


    public Duration getNotificationDuration() {
        return notificationDuration;
    }

    public void setNotificationDuration(Duration notificationDuration) {
        this.notificationDuration = notificationDuration;
    }

    @Override
    public void show(String text) {
        Timeline timeline = createHideTimeline();
        notificationPane.show(text);
        timeline.play();
    }

    @Override
    public void show(String text, Node graphic, Action... actions) {
        Timeline timeline = createHideTimeline();
        notificationPane.show(text, graphic, actions);
        timeline.play();
    }

    @Override
    public void hide() {
        notificationPane.hide();
    }

    private Timeline createHideTimeline() {
        Timeline timeline = new Timeline(
                new KeyFrame(ZERO,
                        new KeyValue(notificationPane.opacityProperty(), 1.0)),
                new KeyFrame(millis(500),
                        new KeyValue(notificationPane.opacityProperty(), 0.0)));
        timeline.setDelay(notificationDuration);
        timeline.setOnFinished(e -> notificationPane.hide());
        return timeline;
    }
}
