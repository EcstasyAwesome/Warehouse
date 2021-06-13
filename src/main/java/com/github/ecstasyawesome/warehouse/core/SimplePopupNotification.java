package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.controller.PopupNotificationController;
import com.github.ecstasyawesome.warehouse.core.window.PopupNotification;
import com.github.ecstasyawesome.warehouse.core.window.StageBasedWindow;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimplePopupNotification extends PopupNotification {

  private final Popup popup;
  private final Stage owner;
  private final String message;
  private final Logger logger = LogManager.getLogger(SimplePopupNotification.class);

  public SimplePopupNotification(Stage owner, String message) {
    var fxml = getClass().getResource("/model/PopupNotification.fxml");
    var fxmlBundle = new FxmlBundle<PopupNotificationController>(fxml);
    var controller = fxmlBundle.getController();
    this.owner = owner;
    this.message = message;
    popup = createPopup(fxmlBundle.getParent());
    controller.apply(popup);
    controller.setMessage(message);
  }

  public SimplePopupNotification(StageBasedWindow owner, String message) {
    this(owner.getStage(), message);
  }

  @Override
  public void show() {
    if (owner.isShowing()) {
      logger.debug("Showed simple popup notification with text '{}'", message);
      popup.show(owner);
    }
  }

  @Override
  public void close() {
    popup.hide();
  }

  @Override
  public boolean isActive() {
    return popup.isShowing();
  }

  private Popup createPopup(Parent parent) {
    var popup = new Popup();
    popup.getContent().add(parent);
    popup.setAutoFix(true);
    popup.setX(Double.MAX_VALUE);
    popup.setY(Double.MAX_VALUE);
    popup.setOpacity(0D);
    applyAnimation(popup);
    return popup;
  }

  private void applyAnimation(Popup popup) {
    var appearValue = new KeyValue(popup.opacityProperty(), 1D);
    var disappearValue = new KeyValue(popup.opacityProperty(), 0D);

    var appearTime = new KeyFrame(Duration.millis(700), appearValue);
    var disappearTime = new KeyFrame(Duration.millis(700), disappearValue);

    var appearTimeLine = new Timeline(appearTime);
    appearTimeLine.setDelay(Duration.ZERO);
    appearTimeLine.setOnFinished(e -> {
      var disappearTimeLine = new Timeline(disappearTime);
      disappearTimeLine.setDelay(Duration.millis(4000));
      disappearTimeLine.setOnFinished(r -> popup.hide());
      disappearTimeLine.play();
    });
    appearTimeLine.playFromStart();
  }
}
