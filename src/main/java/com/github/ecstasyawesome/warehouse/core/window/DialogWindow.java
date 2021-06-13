package com.github.ecstasyawesome.warehouse.core.window;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public abstract class DialogWindow extends WindowNode {

  protected final Alert alert;

  public DialogWindow(AlertType alertType, String message) {
    alert = createAlert(alertType, message);
  }

  @Override
  public void close() {
    alert.close();
  }

  @Override
  public boolean isActive() {
    return alert.isShowing();
  }

  public Optional<ButtonType> showAndGet() {
    return alert.showAndWait();
  }

  private Alert createAlert(AlertType alertType, String message) {
    var alert = new Alert(alertType);
    alert.setTitle("Notification"); // TODO i18n
    alert.setHeaderText(null);
    var defaultMessage = "Message is not provided"; // TODO i18n
    var messageNullOrBlank = message == null || message.isBlank();
    alert.setContentText(messageNullOrBlank ? defaultMessage : message);
    return alert;
  }
}
